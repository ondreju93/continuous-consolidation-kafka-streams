package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Aggregator;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;

@Data
@Slf4j
public class AreaAgentAssignmentGrouped {

  static final Aggregator<Integer, AreaAgentAssignment, AreaAgentAssignmentGrouped> adder =
      (key, value, aggregate) -> {
        log.info("Adding to aggregate: [key={}, value={}]", key, value);
        aggregate.records.put(value.getAreaId(), value);
        return aggregate;
      };

  static final Aggregator<Integer, AreaAgentAssignment, AreaAgentAssignmentGrouped> subtractor =
      (key, value, aggregate) -> {
        log.info("Removing from aggregate: [key={}, value={}]", key, value);
        aggregate.records.remove(value.getAreaId());
        return aggregate;
      };
  private final Map<Integer, AreaAgentAssignment> records = new LinkedHashMap<>();

  static final Materialized<Integer, AreaAgentAssignmentGrouped, KeyValueStore<Bytes, byte[]>> materializedAs(
      String name) {
    return Materialized.<Integer, AreaAgentAssignmentGrouped, KeyValueStore<Bytes, byte[]>>as(name)
        .withKeySerde(Serdes.Integer()).withValueSerde(jsonSerde());
  }

  private static Serde<AreaAgentAssignmentGrouped> jsonSerde() {
    return Serdes.serdeFrom(new JsonPojoSerializer<>(), new JsonGenericTypeDeserializer<>(
        TypeFactory.defaultInstance().constructType(AreaAgentAssignmentGrouped.class)));
  }

  public Collection<KeyValue<Integer, AreaAgentAssignment>> splitRecords() {
    return records.entrySet().stream()
        .map(entry -> new KeyValue<>(entry.getKey(), entry.getValue()))
        .collect(Collectors.toList());
  }
}
