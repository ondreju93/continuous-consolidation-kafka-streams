package pl.apurtak.examples.continuousconsolidation;

import static pl.apurtak.examples.continuousconsolidation.AreaAgentAssignment.serializedWithKey;
import static pl.apurtak.examples.continuousconsolidation.ConsolidationTopics.LEGACY_AREA;
import static pl.apurtak.examples.continuousconsolidation.ConsolidationTopics.LEGACY_AREA_AGENT_ASSIGNMENTS_RAW;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsolidationRunner implements ApplicationRunner {

  private static final Materialized<Integer, AreaAgentAssignment, KeyValueStore<Bytes, byte[]>> NON_ROOT_AREAS = Materialized.<Integer, AreaAgentAssignment, KeyValueStore<Bytes, byte[]>>as(
      "NonRootAreas").withKeySerde(Serdes.Integer())
      .withValueSerde(AreaAgentAssignment.jsonSerde());
  private static final String NON_ROOT_AREAS_BY_PARENT = "NonRootAreasByParent";
  private final KafkaStreamsConfig kafkaStreamsConfig;
  // TODO root area ids to configuration
  private final Collection<Integer> rootAreaIds = new HashSet<>(Arrays.asList(11, 22, 33));

  @Override
  public void run(ApplicationArguments args) {
    Topology topology = createAreaConsolidationTopology();
    KafkaStreams streams = new KafkaStreams(topology,
        kafkaStreamsConfig.withAppId("areaConsolidation"));
    streams.start();
  }

  private Topology createAreaConsolidationTopology() {
    final StreamsBuilder builder = new StreamsBuilder();

    val areasChangeStreams = builder
        .stream(LEGACY_AREA_AGENT_ASSIGNMENTS_RAW.getName(),
            LEGACY_AREA_AGENT_ASSIGNMENTS_RAW.getConsumed())
        .map((key, value) -> new KeyValue<>(
            value.getPayload().getAfter().getAreaId(),
            value.getPayload().getAfter()
        ))
        .branch(this::isRootArea, (x, y) -> true);

    val rootAreasChangeStream = areasChangeStreams[0]
        .peek((key, value) -> log.info("Processing root area: [areaId={}]", key));

    val nonRootAreasTable = areasChangeStreams[1]
        .peek((key, value) -> log.info(
            "Non-root area will wait for its parent to be published: [areaId={}, parentId={}]", key,
            value.getParentId()))
        .groupByKey(serializedWithKey())
        .reduce(new TakeNewer<>(), NON_ROOT_AREAS);

    val nonRootAreasByParent = nonRootAreasTable
        .groupBy(((key, value) -> new KeyValue<>(value.getParentId(), value)), serializedWithKey())
        .aggregate(
            AreaAgentAssignmentGrouped::new,
            AreaAgentAssignmentGrouped.adder,
            AreaAgentAssignmentGrouped.subtractor,
            AreaAgentAssignmentGrouped.materializedAs(NON_ROOT_AREAS_BY_PARENT)
        );

    val publishedAreaEvents = builder
        .stream(LEGACY_AREA.getName(), LEGACY_AREA.getConsumed())
        .groupByKey(Event.serializedWithKey(Area.class))
        .reduce(new TakeNewer<>(), Event.materializedAs("PublishedAreaEvents", Area.class));

    val areasWithPublishedParents = nonRootAreasByParent.join(
        publishedAreaEvents,
        (groupedChildren, parent) -> groupedChildren,
        AreaAgentAssignmentGrouped.materializedAs("AreasWithPublishedParents")
    );

    areasWithPublishedParents
        .toStream()
        .flatMap((key, value) -> value.splitRecords())
        .merge(rootAreasChangeStream)
        .mapValues((readOnlyKey, value) -> new Event<>("AreaCreated", new Area(value)))
        .to(LEGACY_AREA.getName(), LEGACY_AREA.getProduced());

    return builder.build();
  }

  private boolean isRootArea(Integer areaId,
      AreaAgentAssignment areaAgentAssignment) {
    return rootAreaIds.contains(areaId);
  }
}
