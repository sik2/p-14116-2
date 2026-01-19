package com.back.shared.member.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class MemberApiClient {
    private final RestClient restClient;
    private final Random random = new Random();

    private static final List<String> FALLBACK_TIPS = List.of(
            "비밀번호는 주기적으로 변경하세요.",
            "2단계 인증을 활성화하세요.",
            "공용 와이파이 사용 시 주의하세요.",
            "의심스러운 링크는 클릭하지 마세요."
    );

    public MemberApiClient(@Value("${custom.global.internalBackUrl}") String internalBackUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(internalBackUrl + "/api/v1/member")
                .build();
    }

    public String getRandomSecureTip() {
        try {
            return restClient.get()
                    .uri("/members/randomSecureTip")
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.debug("Member API 호출 실패, fallback 사용: {}", e.getMessage());
            return FALLBACK_TIPS.get(random.nextInt(FALLBACK_TIPS.size()));
        }
    }
}

