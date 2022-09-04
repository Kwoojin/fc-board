package com.fc.board.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.fc.board.domain.QArticle.*;

public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryCustomImpl(EntityManager em) {
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<String> findAllDistinctHashtags() {

        return queryFactory
                .select(article.hashtag)
                .distinct()
                .from(article)
                .where(article.hashtag.isNotNull())
                .fetch()
                ;



    }
}
