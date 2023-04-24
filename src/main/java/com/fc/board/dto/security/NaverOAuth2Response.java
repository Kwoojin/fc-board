package com.fc.board.dto.security;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @see <a href="https://developers.naver.com/docs/login/devguide/devguide.md#3-4-5-%EC%A0%91%EA%B7%BC-%ED%86%A0%ED%81%B0%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%98%EC%97%AC-%ED%94%84%EB%A1%9C%ED%95%84-api-%ED%98%B8%EC%B6%9C%ED%95%98%EA%B8%B0"> https://developers.naver.com </a>
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverOAuth2Response {
    /**
     * type : String
     * required : Y
     * description : API 호출 결과 코드
     */
    private final String resultCode;
    /**
     * type : String
     * required : Y
     * description : 호출 결과 메시지
     */
    private final String message;

    private final NaverResponse response;

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public static class NaverResponse {
        /**
         * type : String
         * required : Y
         * description : 동일인 식별 정보
         * 동일인 식별 정보는 네이버 아이디마다 고유하게 발급되는 값
         */
        private final String id;
        /**
         * type : String
         * required : Y
         * description : 사용자 별명
         */
        private final String nickName;
        /**
         * type : String
         * required : Y
         * description : 사용자 이름
         */
        private final String name;
        /**
         * type : String
         * required : Y
         * description : 사용자 메일 주소
         */
        private final String email;
        /**
         * type : String
         * required : Y
         * description : 성별
         * F: 여성, M: 남성, U: 확인불가
         */
        private final String gender;
        /**
         * type : String
         * required : Y
         * description : 사용자 연령대
         */
        private final String age;
        /**
         * type : String
         * required : Y
         * description : 사용자 생일 (MM-DD)
         */
        private final String birthday;
        /**
         * type : String
         * required : Y
         * description : 사용자 프로필 사진 URL
         */
        private final String profileImage;
        /**
         * type : String
         * required : Y
         * description : 출생연도
         */
        private final String birthYear;
        /**
         * type : String
         * required : Y
         * description : 휴대전화번호
         */
        private final String mobile;

        private final String mobileE164;

        public static NaverResponse from(Map<String, Object> attributes) {
            return new NaverResponse(
                    String.valueOf(attributes.get("id")),
                    String.valueOf(attributes.get("nickname")),
                    String.valueOf(attributes.get("name")),
                    String.valueOf(attributes.get("email")),
                    String.valueOf(attributes.get("gender")),
                    String.valueOf(attributes.get("age")),
                    String.valueOf(attributes.get("birthday")),
                    String.valueOf(attributes.get("profile_image")),
                    String.valueOf(attributes.get("birthyear")),
                    String.valueOf(attributes.get("mobile")),
                    String.valueOf(attributes.get("mobile_e164"))
            );
        }


    }


    public static NaverOAuth2Response from(Map<String, Object> attributes) {
        return new NaverOAuth2Response(
                String.valueOf(attributes.get("resultcode")),
                String.valueOf(attributes.get("message")),
                NaverResponse.from((Map<String, Object>) attributes.get("response"))
        );
    }

    public String getId() {
        return this.response.getId();
    }

    public String getEmail() {
        return this.response.getEmail();
    }

    public String getMobile() {
        return this.response.getMobileE164();
    }


}
