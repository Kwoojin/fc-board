package com.fc.board.dto.security;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * @see <a href="https://developers.facebook.com/docs/graph-api/reference/user/?locale=ko_KR"> https://developers.facebook.com </a>
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FacebookOAuth2Response {

    /**
     *  type : numeric string
     *  description : 앱 사용자의 앱 범위 사용자 ID입니다. 이 ID는 앱에 고유하며 다른 앱에서 사용할 수 없습니다.
     */
    private final String id;
    /**
     *  type : String
     *  description : 전체 이름
     */
    private final String name;
    /**
     *  type : String
     *  description : 프로필에 나열된 사용자의 기본 이메일 주소입니다.
     * 유효한 이메일 주소를 사용할 수 없는 경우 이 필드는 반환되지 않습니다.
     */
    private final String email;
    /**
     *  type : String
     *  description : 이 사람이 선택한 성별 male 또는 female.
     * 성별이 사용자 정의 값으로 설정된 경우 이 값은 선호하는 대명사를 기반으로 합니다.
     * 선호하는 대명사가 중성일 경우 생략
     */
//    private final String gender;
    /**
     *  type : String
     *  description : 그 사람의 생일입니다. 와 같은 고정 형식 문자열 MM/DD/YYYY입니다.
     * 그러나 사람들은 월과 일과 별도로 태어난 연도를 볼 수 있는 사람을 제어할 수 있으므로
     * 이 문자열은 연도(YYYY) 또는 월 + 일(MM/DD)만 될 수 있습니다.
     */
//    private final String birthday;

    public static FacebookOAuth2Response from(Map<String, Object> attributes) {
        return new FacebookOAuth2Response(
                String.valueOf(attributes.get("id")),
                String.valueOf(attributes.get("name")),
                String.valueOf(attributes.get("email"))
//                String.valueOf(attributes.get("gender"))
//                String.valueOf(attributes.get("birthday"))
        );
    }
}
