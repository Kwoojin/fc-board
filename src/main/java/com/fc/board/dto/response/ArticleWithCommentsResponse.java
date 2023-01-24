package com.fc.board.dto.response;

import com.fc.board.dto.ArticleCommentDto;
import com.fc.board.dto.ArticleWithCommentsDto;
import com.fc.board.dto.HashtagDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

import static java.util.stream.Collectors.*;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ArticleWithCommentsResponse {
    
    private final Long id;
    private final String title;
    private final String content;
    private final Set<String> hashtags;
    private final LocalDateTime createdAt;
    private final String email;
    private final String nickname;
    private final String userId;
    private final Set<ArticleCommentResponse> articleCommentsResponse;

    public static ArticleWithCommentsResponse of(Long id, String title, String content, Set<String> hashtags, LocalDateTime createdAt, String email, String nickname, String userId, Set<ArticleCommentResponse> articleCommentResponses) {
        return new ArticleWithCommentsResponse(id, title, content, hashtags, createdAt, email, nickname, userId, articleCommentResponses);
    }

    public static ArticleWithCommentsResponse from(ArticleWithCommentsDto dto) {
        String nickname = dto.getUserAccountDto().getNickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.getUserAccountDto().getUserId();
        }

        return new ArticleWithCommentsResponse(
                dto.getId(),
                dto.getTitle(),
                dto.getContent(),
                dto.getHashtagDtos().stream()
                        .map(HashtagDto::getHashtagName)
                        .collect(toUnmodifiableSet())
                ,
                dto.getCreatedAt(),
                dto.getUserAccountDto().getEmail(),
                nickname,
                dto.getUserAccountDto().getUserId(),
                organizeChildComments(dto.getArticleCommentDtos())
        );
    }


    private static Set<ArticleCommentResponse> organizeChildComments(Set<ArticleCommentDto> dtos) {
        Map<Long, ArticleCommentResponse> map = dtos.stream()
                .map(ArticleCommentResponse::from)
                .collect(toMap(ArticleCommentResponse::getId, Function.identity()));

        map.values().stream()
                .filter(ArticleCommentResponse::hasParentComment)
                .forEach(comment -> {
                    ArticleCommentResponse parentComment = map.get(comment.getParentCommentId());
                    parentComment.getChildComments().add(comment);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(toCollection(() ->
                        new TreeSet<>(Comparator
                                .comparing(ArticleCommentResponse::getCreatedAt)
                                .reversed()
                                .thenComparingLong(ArticleCommentResponse::getId)
                        )
                ));
    }
}
