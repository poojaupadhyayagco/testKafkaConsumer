package agco.configuration.kafka;

import agco.DeviceReferenceData;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.LoggingErrorHandler;

@Configuration
@EnableKafka
public class KafkaConsumerConfiguration {

  @Autowired
  private KafkaProperties kafkaProperties;

  @Bean
  @ConditionalOnMissingBean(ConsumerFactory.class)
  public ConsumerFactory<String, DeviceReferenceData> consumerFactory() {
    KafkaAvroDeserializer valueDeserializer = new KafkaAvroDeserializer();
    valueDeserializer.configure(generateAvroDeserealizationProperties(
        kafkaProperties.getProperties()), false);
    StringDeserializer keyDeserializer = new StringDeserializer();
    return new DefaultKafkaConsumerFactory(kafkaProperties.buildConsumerProperties(),
        keyDeserializer, valueDeserializer);
  }

  /**
   * Build ContainerFactory for Telemetry DataFrame events on Kafka.
   *
   * @return Telemetry ConsumerFactory for Kafka
   */
  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, DeviceReferenceData>
  kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, DeviceReferenceData> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.setErrorHandler(new LoggingErrorHandler());

    return factory;
  }

  private Map<String, Object> generateAvroDeserealizationProperties(Map<String,
      String> kafkaPropertiesMap) {
    Map<String, Object> kafkaAvroDesProperties = new HashMap<>();

    kafkaAvroDesProperties.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
        kafkaPropertiesMap.get(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG));

    kafkaAvroDesProperties.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

    kafkaAvroDesProperties.put(KafkaAvroDeserializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE,
        kafkaPropertiesMap.get(KafkaAvroDeserializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE));

    kafkaAvroDesProperties.put(KafkaAvroDeserializerConfig.USER_INFO_CONFIG,
        kafkaPropertiesMap.get(KafkaAvroDeserializerConfig.USER_INFO_CONFIG));

    return kafkaAvroDesProperties;
  }
}
