package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.kafka.common.serialization.Serdes.WrapperSerde;

public class AreaAgentAssignmentJsonSerde extends WrapperSerde<RecordChange<AreaAgentAssignment>> {

  public AreaAgentAssignmentJsonSerde() {
    super(
        new JsonPojoSerializer<>(),
        new JsonGenericTypeDeserializer<>(
            TypeFactory.defaultInstance().constructParametricType(
                RecordChange.class, AreaAgentAssignment.class
            )
        )
    );
  }
}
