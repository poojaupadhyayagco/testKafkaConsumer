package agco.services.listener;

import agco.DeviceReferenceData;
import agco.configuration.ServiceConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceReferenceDataConsumer {

  @KafkaListener(
      topics = {ServiceConstants.DEVICE_REF_TOPIC},
      clientIdPrefix = "telemetry-topics",
      containerFactory = "kafkaListenerContainerFactory"
  )
  public void listen(
      ConsumerRecord<String, DeviceReferenceData> cr, @Payload DeviceReferenceData payload) {

    log.info("Message received: {}", payload);

  }
}
