package com.back.boundedContext.payout.in;

import com.back.boundedContext.payout.app.PayoutFacade;
import com.back.global.kafka.KafkaTopics;
import com.back.shared.market.event.MarketOrderPaymentCompletedEvent;
import com.back.shared.member.event.MemberJoinedEvent;
import com.back.shared.member.event.MemberModifiedEvent;
import com.back.shared.payout.event.PayoutCompletedEvent;
import com.back.shared.payout.event.PayoutMemberCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PayoutKafkaListener {
    private final PayoutFacade payoutFacade;

    @KafkaListener(topics = KafkaTopics.MEMBER_JOINED, groupId = "payout-service")
    @Transactional
    public void handleMemberJoined(MemberJoinedEvent event) {
        log.info("Received MemberJoinedEvent: {}", event.getMember().getUsername());
        payoutFacade.syncMember(event.getMember());
    }

    @KafkaListener(topics = KafkaTopics.MEMBER_MODIFIED, groupId = "payout-service")
    @Transactional
    public void handleMemberModified(MemberModifiedEvent event) {
        log.info("Received MemberModifiedEvent: {}", event.getMember().getUsername());
        payoutFacade.syncMember(event.getMember());
    }

    @KafkaListener(topics = KafkaTopics.PAYOUT_MEMBER_CREATED, groupId = "payout-service")
    @Transactional
    public void handlePayoutMemberCreated(PayoutMemberCreatedEvent event) {
        log.info("Received PayoutMemberCreatedEvent: {}", event.getMember().getUsername());
        payoutFacade.createPayout(event.getMember().getId());
    }

    @KafkaListener(topics = KafkaTopics.MARKET_ORDER_PAYMENT_COMPLETED, groupId = "payout-service")
    @Transactional
    public void handleMarketOrderPaymentCompleted(MarketOrderPaymentCompletedEvent event) {
        log.info("Received MarketOrderPaymentCompletedEvent for order: {}", event.getOrder().getId());
        payoutFacade.addPayoutCandidateItems(event.getOrder());
    }

    @KafkaListener(topics = KafkaTopics.PAYOUT_COMPLETED, groupId = "payout-service")
    @Transactional
    public void handlePayoutCompleted(PayoutCompletedEvent event) {
        log.info("Received PayoutCompletedEvent: {}", event.getPayout().getId());
        payoutFacade.createPayout(event.getPayout().getPayeeId());
    }
}