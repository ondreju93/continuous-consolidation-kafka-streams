package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.kafka.common.serialization.Serdes.WrapperSerde;

public class AreaAgentAssignmentKeyJsonSerde extends WrapperSerde<AreaAgentAssignmentKey> {

  public AreaAgentAssignmentKeyJsonSerde() {
    super(new JsonPojoSerializer<>(),
        new JsonGenericTypeDeserializer<>(
            TypeFactory.defaultInstance().constructType(AreaAgentAssignmentKey.class)));
  }
}