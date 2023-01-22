package com.fc.board.dto;

import com.fc.board.domain.Hashtag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HashtagDto {

    private final Long id;
    private final String hashtagName;
    private final LocalDateTime createdAt;
    private final String createdBy;
    private final LocalDateTime modifiedAt;
    private final String modifiedBy;

    public static HashtagDto of(String hashtagName) {
        return new HashtagDto(null, hashtagName, null, null, null, null);
    }

    public static HashtagDto of(Long id, String hashtagName, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new HashtagDto(id, hashtagName, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    public static HashtagDto from(Hashtag entity) {
        return new HashtagDto(
                entity.getId(),
                entity.getHashtagName(),
                entity.getCreatedAt(),
                entity.getCreatedBy(),
                entity.getModifiedAt(),
                entity.getModifiedBy()
        );
    }

    public Hashtag toEntity() {
        return Hashtag.of(getHashtagName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashtagDto)) return false;
        HashtagDto that = (HashtagDto) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getHashtagName(), that.getHashtagName()) && Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getCreatedBy(), that.getCreatedBy()) && Objects.equals(getModifiedAt(), that.getModifiedAt()) && Objects.equals(getModifiedBy(), that.getModifiedBy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getHashtagName(), getCreatedAt(), getCreatedBy(), getModifiedAt(), getModifiedBy());
    }
}
