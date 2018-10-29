package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class JsonGenericTypeDeserializer<T> implements Deserializer<T> {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final JavaType type;

  public JsonGenericTypeDeserializer(JavaType type) {
    this.type = type;
  }

  @Override
  public void configure(Map<String, ?> props, boolean isKey) {
    // intentionally empty
  }

  @Override
  public T deserialize(String topic, byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      return null;
    }

    T data;
    try {
      data = objectMapper.readerFor(type).readValue(bytes);
    } catch (Exception e) {
      throw new SerializationException(e);
    }

    return data;
  }

  @Override
  public void close() {
    // intentionally empty
  }
}

