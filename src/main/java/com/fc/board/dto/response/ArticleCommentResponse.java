package com.fc.board.dto.response;

import com.fc.board.dto.ArticleCommentDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ArticleCommentResponse {
    private final Long id;
    private final String content;
    private final LocalDateTime createdAt;
    private final String email;
    private final String nickname;
    private final String userId;
    private final Long parentCommentId;
    private final Set<ArticleCommentResponse> childComments;

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String email, String nickname, String userId) {
        return ArticleCommentResponse.of(id, content, createdAt, email, nickname, userId, null);
    }

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String email, String nickname, String userId, Long parentCommentId) {
        Comparator<ArticleCommentResponse> childCommentComparator = Comparator
                .comparing(ArticleCommentResponse::getCreatedAt)
                .thenComparingLong(ArticleCommentResponse::getId);
        return new ArticleCommentResponse(id, content, createdAt, email, nickname, userId, parentCommentId, new TreeSet<>(childCommentComparator));
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.getUserAccountDto().getNickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.getUserAccountDto().getUserId();
        }

        return ArticleCommentResponse.of(
                dto.getId(),
                dto.getContent(),
                dto.getCreatedAt(),
                dto.getUserAccountDto().getEmail(),
                nickname,
                dto.getUserAccountDto().getUserId(),
                dto.getParentCommentId()
        );
    }

    public boolean hasParentComment() {
        return parentCommentId != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleCommentResponse)) return false;
        ArticleCommentResponse that = (ArticleCommentResponse) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getContent(), that.getContent()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getNickname(), that.getNickname()) && Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getParentCommentId(), that.getParentCommentId()) && Objects.equals(getChildComments(), that.getChildComments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getContent(), getCreatedAt(), getEmail(), getNickname(), getUserId(), getParentCommentId(), getChildComments());
    }
}
