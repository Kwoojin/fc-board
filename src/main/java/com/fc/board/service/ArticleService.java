package com.fc.board.service;

import com.fc.board.domain.Article;
import com.fc.board.dto.ArticleDto;
import com.fc.board.dto.ArticleWithCommentsDto;
import com.fc.board.repository.ArticleRepository;
import com.fc.board.domain.type.SearchType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

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
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " +articleId));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.getId());
            if (StringUtils.hasText(dto.getTitle())) { article.setTitle(dto.getTitle()); }
            if (StringUtils.hasText(dto.getContent())) { article.setContent(dto.getContent()); }
            article.setHashtag(dto.getHashtag());
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다 - dto : {}", dto);
        }
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
