package com.back.boundedContext.cash.in;


import com.back.boundedContext.cash.app.CashFacade;
import com.back.boundedContext.cash.domain.CashLog;
import com.back.boundedContext.cash.domain.CashMember;
import com.back.boundedContext.cash.domain.Wallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@Slf4j
public class CashDataInit {
    private final CashDataInit self;
    private final CashFacade cashFacade;

    public CashDataInit(
            @Lazy CashDataInit self,
            CashFacade cashFacade
    ) {
        this.self = self;
        this.cashFacade = cashFacade;
    }

    @Bean
    @Order(2)
    public ApplicationRunner cashDataInitApplicationRunner() {
        return args -> {
            self.makeBaseCredits();
        };
    }

    @Transactional
    public void makeBaseCredits() {
        // 독립 실행 시 CashMember가 없을 수 있으므로 체크
        var user1MemberOpt = cashFacade.findMemberByUsername("user1");
        if (user1MemberOpt.isEmpty()) {
            log.info("CashMember not found. Skipping cash data init. (Member sync required via event)");
            return;
        }

        CashMember user1Member = user1MemberOpt.get();
        CashMember user2Member = cashFacade.findMemberByUsername("user2").orElse(null);

        var user1WalletOpt = cashFacade.findWalletByHolder(user1Member);
        if (user1WalletOpt.isEmpty()) return;

        Wallet user1Wallet = user1WalletOpt.get();
        if (user1Wallet.hasBalance()) return;

        user1Wallet.credit(150_000, CashLog.EventType.충전__무통장입금);
        user1Wallet.credit(100_000, CashLog.EventType.충전__무통장입금);
        user1Wallet.credit(50_000, CashLog.EventType.충전__무통장입금);

        if (user2Member != null) {
            var user2WalletOpt = cashFacade.findWalletByHolder(user2Member);
            if (user2WalletOpt.isPresent()) {
                user2WalletOpt.get().credit(150_000, CashLog.EventType.충전__무통장입금);
            }
        }
    }
}