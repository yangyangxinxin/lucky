package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.Article;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public interface ArticleDao extends PagingAndSortingRepository<Article,Long> {

   /*  修改
    @Modifying
    @Query(value = "update Article set name=?1 where name=?2")
    void update(Article article);
    */



}
