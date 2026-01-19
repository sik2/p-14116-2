package com.back.boundedContext.cash.in;

import com.back.boundedContext.cash.app.CashFacade;
import com.back.global.kafka.KafkaTopics;
import com.back.shared.cash.event.CashMemberCreatedEvent;
import com.back.shared.market.event.MarketOrderPaymentRequestedEvent;
import com.back.shared.member.event.MemberJoinedEvent;
import com.back.shared.member.event.MemberModifiedEvent;
import com.back.shared.payout.event.PayoutCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CashKafkaListener {
    private final CashFacade cashFacade;

    @KafkaListener(topics = KafkaTopics.MEMBER_JOINED, groupId = "cash-service")
    @Transactional
    public void handleMemberJoined(MemberJoinedEvent event) {
        log.info("Received MemberJoinedEvent: {}", event.getMember().getUsername());
        cashFacade.syncMember(event.getMember());
    }

    @KafkaListener(topics = KafkaTopics.MEMBER_MODIFIED, groupId = "cash-service")
    @Transactional
    public void handleMemberModified(MemberModifiedEvent event) {
        log.info("Received MemberModifiedEvent: {}", event.getMember().getUsername());
        cashFacade.syncMember(event.getMember());
    }

    @KafkaListener(topics = KafkaTopics.CASH_MEMBER_CREATED, groupId = "cash-service")
    @Transactional
    public void handleCashMemberCreated(CashMemberCreatedEvent event) {
        log.info("Received CashMemberCreatedEvent: {}", event.getMember().getUsername());
        cashFacade.createWallet(event.getMember());
    }

    @KafkaListener(topics = KafkaTopics.MARKET_ORDER_PAYMENT_REQUESTED, groupId = "cash-service")
    @Transactional
    public void handleMarketOrderPaymentRequested(MarketOrderPaymentRequestedEvent event) {
        log.info("Received MarketOrderPaymentRequestedEvent for order: {}", event.getOrder().getId());
        cashFacade.completeOrderPayment(event.getOrder(), event.getPgPaymentAmount());
    }

    @KafkaListener(topics = KafkaTopics.PAYOUT_COMPLETED, groupId = "cash-service")
    @Transactional
    public void handlePayoutCompleted(PayoutCompletedEvent event) {
        log.info("Received PayoutCompletedEvent: {}", event.getPayout().getId());
        cashFacade.completePayout(event.getPayout());
    }
}