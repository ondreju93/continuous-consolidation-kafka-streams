package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

public class JsonPojoSerializer<T> implements Serializer<T> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void configure(Map<String, ?> map, boolean b) {
    // intentionally empty
  }

  @Override
  public byte[] serialize(String topic, T data) {
    if (data == null) {
      return new byte[0];
    } else {
      try {
        return objectMapper.writeValueAsBytes(data);
      } catch (Exception e) {
        throw new SerializationException("Error serializing JSON message", e);
      }
    }
  }

  @Override
  public void close() {
    // intentionally empty
  }
}
