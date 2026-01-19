package com.back.global.eventPublisher;

import com.back.global.kafka.KafkaEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;
    private final KafkaEventPublisher kafkaEventPublisher;

    public void publish(Object event) {
        // 로컬 이벤트 발행 (같은 JVM 내)
        applicationEventPublisher.publishEvent(event);
        // Kafka 이벤트 발행 (다른 서비스로)
        kafkaEventPublisher.publish(event);
    }

    public void publishLocal(Object event) {
        applicationEventPublisher.publishEvent(event);
    }

    public void publishKafka(Object event) {
        kafkaEventPublisher.publish(event);
    }
}
