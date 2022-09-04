package com.fc.board.repository.querydsl;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<String> findAllDistinctHashtags();
}
