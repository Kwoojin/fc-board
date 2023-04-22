package com.fc.board.dto.security;

import com.fc.board.dto.UserAccountDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardPrincipal implements UserDetails, OAuth2User, OidcUser {

    private final String username;
    private final String password;
    Collection<? extends GrantedAuthority> authorities;
    private final String email;
    private final String nickname;
    private final String memo;
    private final Map<String, Object> oAuth2Attributes;

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
        return of(username, password, email, nickname, memo, Map.of());
    }

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo, Map<String, Object> oAuth2Attributes) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new BoardPrincipal(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                email,
                nickname,
                memo,
                oAuth2Attributes
        );
    }

    public static BoardPrincipal from(UserAccountDto dto) {
        return BoardPrincipal.of(
            dto.getUserId(),
            dto.getUserPassword(),
            dto.getEmail(),
            dto.getNickname(),
            dto.getMemo()
        );
    }

    public UserAccountDto toDto() {
        return UserAccountDto.of(
                username,
                password,
                email,
                nickname,
                memo
        );
    }

    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Override public Map<String, Object> getAttributes() { return oAuth2Attributes; }
    @Override public String getName() { return username; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoardPrincipal)) return false;
        BoardPrincipal that = (BoardPrincipal) o;
        return Objects.equals(getUsername(), that.getUsername()) && Objects.equals(getPassword(), that.getPassword()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getNickname(), that.getNickname()) && Objects.equals(getMemo(), that.getMemo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getEmail(), getNickname(), getMemo());
    }

    @Override
    public Map<String, Object> getClaims() {
        return null;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return null;
    }

    @Override
    public OidcIdToken getIdToken() {
        return null;
    }

    public enum RoleType {
        USER("ROLE_USER");

        @Getter private final String name;

        RoleType(String name) {
            this.name = name;
        }
    }
}
