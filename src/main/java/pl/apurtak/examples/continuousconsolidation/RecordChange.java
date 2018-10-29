package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value
public class RecordChange<T> {
  Payload<T> payload;
}
