package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Value;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Serialized;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class AreaAgentAssignment {

  @JsonProperty("AID")
  int areaId;
  @JsonProperty("ANAME")
  String areaName;
  @JsonProperty("PAID")
  int parentId;
  @JsonProperty("HLVL")
  String hierarchyLevel;
  @JsonProperty("AGLNAME")
  String agentLastName;
  @JsonProperty("AGFNAME")
  String agentFirstName;
  @JsonProperty("AGEML")
  String agentEmail;

  public AreaAgentAssignment(
      @JsonProperty("AID") int areaId,
      @JsonProperty("ANAME") String areaName,
      @JsonProperty("PAID") int parentId,
      @JsonProperty("HLVL") String hierarchyLevel,
      @JsonProperty("AGLNAME") String agentLastName,
      @JsonProperty("AGFNAME") String agentFirstName,
      @JsonProperty("AGEML") String agentEmail
  ) {
    this.areaId = areaId;
    this.areaName = areaName;
    this.parentId = parentId;
    this.hierarchyLevel = hierarchyLevel;
    this.agentLastName = agentLastName;
    this.agentFirstName = agentFirstName;
    this.agentEmail = agentEmail;
  }

  static Serde<AreaAgentAssignment> jsonSerde() {
    return Serdes.serdeFrom(new JsonPojoSerializer<>(), new JsonGenericTypeDeserializer<>(
        TypeFactory.defaultInstance().constructType(AreaAgentAssignment.class)));
  }

  static Serialized<Integer, AreaAgentAssignment> serializedWithKey() {
    return Serialized.with(Serdes.Integer(), jsonSerde());
  }

}