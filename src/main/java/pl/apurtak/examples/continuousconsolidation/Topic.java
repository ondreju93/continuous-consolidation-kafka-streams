package pl.apurtak.examples.continuousconsolidation;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

@Data
@RequiredArgsConstructor
public class Topic<K, V> {

  private final String name;
  private final Consumed<K, V> consumed;
  private final Produced<K, V> produced;

  public Topic(String name, Serde<K> keySerde, Serde<V> valueSerde) {
    this(
        name, Consumed.<K, V>with(keySerde, valueSerde), Produced.<K, V>with(keySerde, valueSerde)
    );
  }
}
