package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.Photo;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yangxin on 2017/5/26.
 */
@Repository
public interface PhotoDao extends PagingAndSortingRepository<Photo, Long> {
}
