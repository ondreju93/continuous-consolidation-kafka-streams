package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class AreaAgentAssignmentKey {

  @JsonProperty("AID")
  int areaId;

  public AreaAgentAssignmentKey(@JsonProperty("AID") int areaId) {
    this.areaId = areaId;
  }
}
