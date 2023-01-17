package com.fc.board.dto.response;

import com.fc.board.dto.ArticleDto;
import com.fc.board.dto.HashtagDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Set<String> hashtags;
    private final LocalDateTime createdAt;
    private final String email;
    private final String nickname;

    public static ArticleResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleResponse(id, title, content, hashtags, createdAt, email, nickname);
    }

    public static ArticleResponse from(ArticleDto dto) {
        String nickname = dto.getUserAccountDto().getNickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.getUserAccountDto().getUserId();
        }

        return new ArticleResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getContent(),
                dto.getHashtagDtos().stream()
                        .map(HashtagDto::getHashtagName)
                        .collect(toUnmodifiableSet()),
                dto.getCreatedAt(),
                dto.getUserAccountDto().getEmail(),
                nickname
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleResponse)) return false;
        ArticleResponse that = (ArticleResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getTitle(), that.getTitle()) && Objects.equals(getContent(), that.getContent()) && Objects.equals(getHashtags(), that.getHashtags()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getNickname(), that.getNickname());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getContent(), getHashtags(), getCreatedAt(), getEmail(), getNickname());
    }
}
