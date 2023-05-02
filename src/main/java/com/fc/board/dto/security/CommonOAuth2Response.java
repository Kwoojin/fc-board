package com.fc.board.dto.security;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

@Getter
public class CommonOAuth2Response {

    @SuppressWarnings("SpellCheckingInspection")
    public enum OAuth2Provider {

        kakao {
            public CommonOAuth2Response convert(Map<String, Object> attributes){
                KakaoOAuth2Response kakao = KakaoOAuth2Response.from(attributes);

                return CommonOAuth2Response.builder()
                        .id(kakao.getId().toString())
                        .email(kakao.getEmail())
                        .nickname(kakao.getNickname())
                        .build();
            }
        },

        facebook {
            public CommonOAuth2Response convert(Map<String, Object> attributes) {
                FacebookOAuth2Response facebook = FacebookOAuth2Response.from(attributes);

                return CommonOAuth2Response.builder()
                        .id(facebook.getId())
                        .email(facebook.getEmail())
                        .nickname(facebook.getName())
                        .build();
            }
        },

        naver {
            public CommonOAuth2Response convert(Map<String, Object> attributes) {
                NaverOAuth2Response naver = NaverOAuth2Response.from(attributes);

                return CommonOAuth2Response.builder()
                        .id(naver.getId())
                        .email(naver.getEmail())
                        .nickname(naver.getResponse().getNickName())
                        .build();
            }
        }
        ;

        public abstract CommonOAuth2Response convert(Map<String, Object> attributes);
    }

    private final String id;
    private final String email;
    private final String nickname;

    @Builder
    private CommonOAuth2Response(String id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }

    public static OAuth2Provider getProvider(String registrationId) {
        return Arrays.stream(OAuth2Provider.values())
                .filter(provider -> provider.name().equals(registrationId))
                .findAny()
                .orElseThrow();
    }

}
