package pl.apurtak.examples.continuousconsolidation;

import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.Value;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.state.KeyValueStore;

@Value
public class Event<T> {

  String name;
  T body;

  static <T> Serde<Event<T>> serdeForBodyType(Class<T> bodyClass) {
    return Serdes.serdeFrom(new JsonPojoSerializer<>(),
        new JsonGenericTypeDeserializer<>(
            TypeFactory.defaultInstance().constructParametricType(Event.class, bodyClass)));
  }

  static <T> Serialized<Integer, Event<T>> serializedWithKey(Class<T> bodyClass) {
    return Serialized.with(Serdes.Integer(), serdeForBodyType(bodyClass));
  }

  static <T> Materialized<Integer, Event<T>, KeyValueStore<Bytes, byte[]>> materializedAs(
      String storeName, Class<T> bodyClass) {
    return Materialized.<Integer, Event<T>, KeyValueStore<Bytes, byte[]>>as(storeName)
        .withKeySerde(Serdes.Integer()).withValueSerde(serdeForBodyType(bodyClass));
  }
}
