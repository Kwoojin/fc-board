package com.fc.board.dto;


import com.fc.board.domain.Article;
import com.fc.board.domain.UserAccount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ArticleDto {
    private final Long id;
    private final UserAccountDto userAccountDto;
    private final String title;
    private final String content;
    private final Set<HashtagDto> hashtagDtos;
    private final LocalDateTime createdAt;
    private final String createdBy;
    private final LocalDateTime modifiedAt;
    private final String modifiedBy;

    public static ArticleDto of(UserAccountDto userAccountDto, String title, String content, Set<HashtagDto> hashtagDtos) {
        return new ArticleDto(null, userAccountDto, title, content, hashtagDtos, null, null, null, null);
    }

    public static ArticleDto of(Long id, UserAccountDto userAccountDto, String title, String content, Set<HashtagDto> hashtagDtos, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new ArticleDto(id, userAccountDto, title, content, hashtagDtos, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static ArticleDto from(Article entity) {
        return new ArticleDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtags().stream()
                        .map(HashtagDto::from)
                        .collect(toUnmodifiableSet()),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public Article toEntity(UserAccount userAccount) {
        return Article.of(
                userAccount,
                title,
                content
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleDto)) return false;
        ArticleDto dto = (ArticleDto) o;
        return Objects.equals(getId(), dto.getId()) && Objects.equals(getUserAccountDto(), dto.getUserAccountDto()) && Objects.equals(getTitle(), dto.getTitle()) && Objects.equals(getContent(), dto.getContent()) && Objects.equals(getHashtagDtos(), dto.getHashtagDtos()) && Objects.equals(getCreatedAt(), dto.getCreatedAt()) && Objects.equals(getCreatedBy(), dto.getCreatedBy()) && Objects.equals(getModifiedAt(), dto.getModifiedAt()) && Objects.equals(getModifiedBy(), dto.getModifiedBy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserAccountDto(), getTitle(), getContent(), getHashtagDtos(), getCreatedAt(), getCreatedBy(), getModifiedAt(), getModifiedBy());
    }
}
