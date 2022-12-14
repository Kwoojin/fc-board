package com.fc.board.service;

import com.fc.board.domain.Article;
import com.fc.board.domain.UserAccount;
import com.fc.board.dto.ArticleDto;
import com.fc.board.dto.ArticleWithCommentsDto;
import com.fc.board.repository.ArticleRepository;
import com.fc.board.domain.constant.SearchType;
import com.fc.board.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if (StringUtils.hasText(searchKeyword) == false) {
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        switch (searchType) {
            case TITLE :
                return articleRepository.findByTitleContainingIgnoreCase(searchKeyword, pageable).map(ArticleDto::from);
            case CONTENT :
                return articleRepository.findByContentContainingIgnoreCase(searchKeyword, pageable).map(ArticleDto::from);
            case ID :
                return articleRepository.findByUserAccount_UserIdContainingIgnoreCase(searchKeyword, pageable).map(ArticleDto::from);
            case NICKNAME :
                return articleRepository.findByUserAccount_NicknameContainingIgnoreCase(searchKeyword, pageable).map(ArticleDto::from);
            case HASHTAG :
                return articleRepository.findByHashtag("#" + searchKeyword, pageable).map(ArticleDto::from);
        }
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("???????????? ???????????? - articleId: " +articleId));
    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("???????????? ???????????? - articleId: " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId());
        articleRepository.save(dto.toEntity(userAccount));
    }

    public void updateArticle(Long articleId, ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(articleId);
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.getUserAccountDto().getUserId());

            if (article.getUserAccount().equals(userAccount)) {
                if (StringUtils.hasText(dto.getTitle())) { article.setTitle(dto.getTitle()); }
                if (StringUtils.hasText(dto.getContent())) { article.setContent(dto.getContent()); }
                article.setHashtag(dto.getHashtag());
            }
        } catch (EntityNotFoundException e) {
            log.warn("????????? ???????????? ??????. ???????????? ??????????????? ????????? ????????? ?????? ??? ???????????? - {}", e.getLocalizedMessage());
        }
    }

    public void deleteArticle(long articleId, String userId) {
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticlesViaHashtag(String hashtag, Pageable pageable) {
        if(StringUtils.hasText(hashtag) == false) {
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtag(hashtag, pageable).map(ArticleDto::from);
    }

    @Transactional(readOnly = true)
    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }
}
