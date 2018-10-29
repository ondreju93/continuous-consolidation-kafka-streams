package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.stream.Stream;

public enum Operation {
  CREATE("c"),
  UPDATE("u"),
  DELETE("d");

  private final String shortName;

  Operation(String shortName) {
    this.shortName = shortName;
  }

  @JsonCreator
  static Operation fromShortName(String shortName) {
    return Stream.of(values())
        .filter(operation -> shortName.equalsIgnoreCase(operation.shortName))
        .findAny()
        .orElseThrow(IllegalArgumentException::new);
  }
}
