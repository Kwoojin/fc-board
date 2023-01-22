package com.fc.board.service;

import com.fc.board.domain.Article;
import com.fc.board.domain.Hashtag;
import com.fc.board.domain.UserAccount;
import com.fc.board.dto.ArticleDto;
import com.fc.board.dto.ArticleWithCommentsDto;
import com.fc.board.repository.ArticleRepository;
import com.fc.board.domain.constant.SearchType;
import com.fc.board.repository.HashtagRepository;
import com.fc.board.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static org.springframework.util.StringUtils.*;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final HashtagService hashtagService;
    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;
    private final HashtagRepository hashtagRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (hasText(searchKeyword) == false) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        switch (searchType) {
            case TITLE :
                return articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT :
                return articleRepository.findByContentContaining(searchKeyword, pageable).map(ArticleDto::from);
            case ID :
                return articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME :
                return articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG :
                return articleRepository.findByHashtagNames(
                                Arrays.stream(searchKeyword.split(" ")).collect(toList()),
                                pageable
                        )
                        .map(ArticleDto::from);
        }
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " +articleId));
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId());
        Set<Hashtag> hashtags = renewHashtagsFromContent(dto.getContent());

        Article article = dto.toEntity(userAccount);
        article.addHashtags(hashtags);
        articleRepository.save(article);
    }

    public void deleteArticle(long articleId, String userId) {
        Article article = articleRepository.getReferenceById(articleId);
        Set<Long> hashtagIds = article.getHashtags().stream()
                .map(Hashtag::getId)
                .collect(toUnmodifiableSet());

        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
        articleRepository.flush();

        hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId());

            if (article.getUserAccount().equals(userAccount)) {
                if (dto.getTitle() != null) { article.setTitle(dto.getTitle()); }
                if (dto.getContent() != null) { article.setContent(dto.getContent()); }

                // 중복이 되는 부분이 있어, 효율성에서 떨어질 순 있지만, 리팩토링 여지를 남겨 둔다.
                Set<Long> hashtagIds = article.getHashtags().stream()
                        .map(Hashtag::getId)
                        .collect(toUnmodifiableSet());
                article.clearHashtags();
                articleRepository.flush();

                hashtagIds.forEach(hashtagService::deleteHashtagWithoutArticles);

                Set<Hashtag> hashtags = renewHashtagsFromContent(dto.getContent());
                article.addHashtags(hashtags);
            }
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 수정하는데 필요한 정보를 찾을 수 없습니다 - {}", e.getLocalizedMessage());
        }
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtagName, Pageable pageable) {
        if(hasText(hashtagName) == false) {
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtagNames(List.of(hashtagName), pageable)
                .map(ArticleDto::from);
    }

    @Transactional(readOnly = true)
    public List<String> getHashtags() {     // TODO: HashtagService 로 이동을 고려해보자
        return hashtagRepository.findAllHashtagNames();
    }

    private Set<Hashtag> renewHashtagsFromContent(String content) {
        Set<String> hashtagNamesInContent = hashtagService.parseHashtagNames(content);
        Set<Hashtag> hashtags = hashtagService.findHashtagsByNames(hashtagNamesInContent);
        Set<String> existingHashtagNames = hashtags.stream()
                .map(Hashtag::getHashtagName)
                .collect(toUnmodifiableSet());

        hashtagNamesInContent.forEach(newHashtagName -> {
            if (!existingHashtagNames.contains(newHashtagName)) {
                hashtags.add(Hashtag.of(newHashtagName));
            }
        });

        return hashtags;
    }
}
