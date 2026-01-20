package com.back.global.kafka;

import com.back.shared.cash.event.CashMemberCreatedEvent;
import com.back.shared.cash.event.CashOrderPaymentFailedEvent;
import com.back.shared.cash.event.CashOrderPaymentSucceededEvent;
import com.back.shared.market.event.MarketMemberCreatedEvent;
import com.back.shared.market.event.MarketOrderPaymentCompletedEvent;
import com.back.shared.market.event.MarketOrderPaymentRequestedEvent;
import com.back.shared.member.event.MemberJoinedEvent;
import com.back.shared.member.event.MemberModifiedEvent;
import com.back.shared.payout.event.PayoutCompletedEvent;
import com.back.shared.payout.event.PayoutMemberCreatedEvent;
import com.back.shared.post.event.PostCommentCreatedEvent;
import com.back.shared.post.event.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(Object event) {
        String topic = resolveTopicName(event);
        if (topic != null) {
            kafkaTemplate.send(topic, event);
            log.debug("Published event to topic {}: {}", topic, event.getClass().getSimpleName());
        } else {
            log.warn("Unknown event type: {}", event.getClass().getName());
        }
    }

    private String resolveTopicName(Object event) {
        return switch (event) {
            case MemberJoinedEvent e -> KafkaTopics.MEMBER_JOINED;
            case MemberModifiedEvent e -> KafkaTopics.MEMBER_MODIFIED;
            case PostCreatedEvent e -> KafkaTopics.POST_CREATED;
            case PostCommentCreatedEvent e -> KafkaTopics.POST_COMMENT_CREATED;
            case MarketMemberCreatedEvent e -> KafkaTopics.MARKET_MEMBER_CREATED;
            case MarketOrderPaymentRequestedEvent e -> KafkaTopics.MARKET_ORDER_PAYMENT_REQUESTED;
            case MarketOrderPaymentCompletedEvent e -> KafkaTopics.MARKET_ORDER_PAYMENT_COMPLETED;
            case CashMemberCreatedEvent e -> KafkaTopics.CASH_MEMBER_CREATED;
            case CashOrderPaymentSucceededEvent e -> KafkaTopics.CASH_ORDER_PAYMENT_SUCCEEDED;
            case CashOrderPaymentFailedEvent e -> KafkaTopics.CASH_ORDER_PAYMENT_FAILED;
            case PayoutMemberCreatedEvent e -> KafkaTopics.PAYOUT_MEMBER_CREATED;
            case PayoutCompletedEvent e -> KafkaTopics.PAYOUT_COMPLETED;
            default -> null;
        };
    }
}
