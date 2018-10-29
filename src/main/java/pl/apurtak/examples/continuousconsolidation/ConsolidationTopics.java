package pl.apurtak.examples.continuousconsolidation;

import org.apache.kafka.common.serialization.Serdes;

public class ConsolidationTopics {

  static Topic<String, RecordChange<AreaAgentAssignment>> LEGACY_AREA_AGENT_ASSIGNMENTS_RAW = new Topic<>(
      "dbserver1.insurance.AREA_AGENT_ASSIGNMENTS",
      /*new AreaAgentAssignmentKeyJsonSerde()*/ Serdes.String(),
      new AreaAgentAssignmentJsonSerde());

  static Topic<Integer, Event<Area>> LEGACY_AREA = new Topic<>("LegacyArea",
      Serdes.Integer(), Event.serdeForBodyType(Area.class));

  private ConsolidationTopics() {
    // hidden
  }
}
