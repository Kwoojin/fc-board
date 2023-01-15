package com.fc.board.dto.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@SuppressWarnings("unchecked") // TODO: Map -> Object 변환 로직이 있어 제네릭 타입 캐스팅 문제를 무시한다. 더 좋은 방법이 있다면 고려할 수 있음.
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class KakaoOAuth2Response {

    private final Long id;
    private final LocalDateTime connectedAt;
    private final Map<String, Object> properties;
    private final KakaoAccount kakaoAccount;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class KakaoAccount {
        private final Boolean profileNicknameNeedsAgreement;
        private final Profile profile;
        private final Boolean hasEmail;
        private final Boolean emailNeedsAgreement;
        private final Boolean isEmailValid;
        private final Boolean isEmailVerified;
        private final String email;

        @AllArgsConstructor(access = AccessLevel.PRIVATE)
        @Getter
        public static class Profile {

            private final String nickname;

            public static Profile from(Map<String, Object> attributes) {
                return new Profile(String.valueOf(attributes.get("nickname")));
            }
        }

        public static KakaoAccount from(Map<String, Object> attributes) {
            return new KakaoAccount(
                    Boolean.valueOf(String.valueOf(attributes.get("profile_nickname_needs_agreement"))),
                    Profile.from((Map<String, Object>) attributes.get("profile")),
                    Boolean.valueOf(String.valueOf(attributes.get("has_email"))),
                    Boolean.valueOf(String.valueOf(attributes.get("email_needs_agreement"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_valid"))),
                    Boolean.valueOf(String.valueOf(attributes.get("is_email_verified"))),
                    String.valueOf(attributes.get("email"))
            );
        }

        public String getNickname() { return getProfile().getNickname(); }
    }

    public static KakaoOAuth2Response from(Map<String, Object> attributes) {
        return new KakaoOAuth2Response(
                Long.valueOf(String.valueOf(attributes.get("id"))),
                LocalDateTime.parse(
                        String.valueOf(attributes.get("connected_at")),
                        DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault())
                ),
                (Map<String, Object>) attributes.get("properties"),
                KakaoAccount.from((Map<String, Object>) attributes.get("kakao_account"))
        );
    }

    public String getEmail() { return getKakaoAccount().getEmail(); }
    public String getNickname() { return getKakaoAccount().getNickname(); }

}
