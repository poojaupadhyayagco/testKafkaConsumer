package agco;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class Consumer {

  @Value("${topic.name}")
  private String topicName;

  @KafkaListener(topics = "users", groupId = "group_id")
  public void consume(ConsumerRecord<String, DeviceReferenceData> record) {
    log.info(String.format("Consumed message -> %s", record.value()));
  }
}
