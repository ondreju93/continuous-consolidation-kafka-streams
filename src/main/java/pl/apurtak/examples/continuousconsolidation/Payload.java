package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload<T> {

  @JsonProperty("before")
  T before;
  @JsonProperty("after")
  T after;
  @JsonProperty("op")
  Operation operation;

  public Payload(@JsonProperty("before") T before, @JsonProperty("after") T after,
      @JsonProperty("op") Operation operation) {
    this.before = before;
    this.after = after;
    this.operation = operation;
  }
}
