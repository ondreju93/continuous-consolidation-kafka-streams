package pl.apurtak.examples.continuousconsolidation;

import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaStreamsConfig {

  @Value("${kafka.bootstrap.servers}")
  private String kafkaBootstrapServers;

  @Value("${kafka.commit.interval.ms}")
  private String kafkaCommitIntervalMs;

  public Properties withAppId(final String appId) {
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, appId);
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
        AreaAgentAssignmentKeyJsonSerde.class.getName());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
        AreaAgentAssignmentJsonSerde.class.getName());
    props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);
    props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, kafkaCommitIntervalMs);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return props;
  }
}
