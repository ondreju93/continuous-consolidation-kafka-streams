package pl.apurtak.examples.continuousconsolidation;

public class TakeNewer<T> implements org.apache.kafka.streams.kstream.Reducer<T> {

  @Override
  public T apply(T value1, T value2) {
    return value2;
  }
}
