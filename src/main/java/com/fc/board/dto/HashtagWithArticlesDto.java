package com.fc.board.dto;

import com.fc.board.domain.Article;
import com.fc.board.domain.Hashtag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HashtagWithArticlesDto {

    private final Long id;
    private final Set<ArticleDto> articleDtos;
    private final String hashtagName;
    private final LocalDateTime createdAt;
    private final String createdBy;
    private final LocalDateTime modifiedAt;
    private final String modifiedBy;

    public static HashtagWithArticlesDto of(Set<ArticleDto> articleDtos, String hashtagName) {
        return new HashtagWithArticlesDto(null, articleDtos, hashtagName, null, null, null, null);
    }

    public static HashtagWithArticlesDto of(Long id, Set<ArticleDto> articleDtos, String hashtagName, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new HashtagWithArticlesDto(id, articleDtos, hashtagName, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public HashtagWithArticlesDto from(Hashtag entity) {
        return new HashtagWithArticlesDto(
                entity.getId(),
                entity.getArticles().stream()
                        .map(ArticleDto::from)
                        .collect(toUnmodifiableSet()),
                entity.getHashtagName(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }
}
