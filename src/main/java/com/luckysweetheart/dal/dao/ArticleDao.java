package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public  interface ArticleDao extends PagingAndSortingRepository<Article, Long>  {


}
