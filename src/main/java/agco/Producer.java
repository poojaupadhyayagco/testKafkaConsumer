package agco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class Producer {
  @Value("${topic.name}")
  private String TOPIC;

  private final KafkaTemplate<String, DeviceReferenceData> kafkaTemplate;

  @Autowired
  public Producer(KafkaTemplate<String, DeviceReferenceData> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  void sendMessage(DeviceReferenceData deviceReferenceData) {
    this.kafkaTemplate.send(this.TOPIC, deviceReferenceData.getName(), deviceReferenceData);
    log.info(String.format("Produced user -> %s", deviceReferenceData));
  }
}