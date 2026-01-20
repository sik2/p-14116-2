package com.back.boundedContext.market.in;

import com.back.boundedContext.market.app.MarketFacade;
import com.back.global.kafka.KafkaTopics;
import com.back.shared.cash.event.CashOrderPaymentFailedEvent;
import com.back.shared.cash.event.CashOrderPaymentSucceededEvent;
import com.back.shared.market.event.MarketMemberCreatedEvent;
import com.back.shared.member.event.MemberJoinedEvent;
import com.back.shared.member.event.MemberModifiedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class MarketKafkaListener {
    private final MarketFacade marketFacade;

    @KafkaListener(topics = KafkaTopics.MEMBER_JOINED, groupId = "market-service")
    @Transactional
    public void handleMemberJoined(MemberJoinedEvent event) {
        log.info("Received MemberJoinedEvent: {}", event.member().username());
        marketFacade.syncMember(event.member());
    }

    @KafkaListener(topics = KafkaTopics.MEMBER_MODIFIED, groupId = "market-service")
    @Transactional
    public void handleMemberModified(MemberModifiedEvent event) {
        log.info("Received MemberModifiedEvent: {}", event.member().username());
        marketFacade.syncMember(event.member());
    }

    @KafkaListener(topics = KafkaTopics.MARKET_MEMBER_CREATED, groupId = "market-service")
    @Transactional
    public void handleMarketMemberCreated(MarketMemberCreatedEvent event) {
        log.info("Received MarketMemberCreatedEvent: {}", event.member().username());
        marketFacade.createCart(event.member());
    }

    @KafkaListener(topics = KafkaTopics.CASH_ORDER_PAYMENT_SUCCEEDED, groupId = "market-service")
    @Transactional
    public void handleCashOrderPaymentSucceeded(CashOrderPaymentSucceededEvent event) {
        log.info("Received CashOrderPaymentSucceededEvent for order: {}", event.order().id());
        int orderId = event.order().id();
        marketFacade.completeOrderPayment(orderId);
    }

    @KafkaListener(topics = KafkaTopics.CASH_ORDER_PAYMENT_FAILED, groupId = "market-service")
    @Transactional
    public void handleCashOrderPaymentFailed(CashOrderPaymentFailedEvent event) {
        log.info("Received CashOrderPaymentFailedEvent for order: {}", event.order().id());
        int orderId = event.order().id();
        marketFacade.cancelOrderRequestPayment(orderId);
    }
}