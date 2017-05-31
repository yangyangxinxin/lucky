package com.luckysweetheart.service;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.dao.ArticleDao;
import com.luckysweetheart.dal.entity.Article;
import com.luckysweetheart.dto.ArticleDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class ArticleService extends BaseService {

    @Resource
    private ArticleDao articleDao;

    public ResultInfo<Long> create(ArticleDTO articleDTO) throws BusinessException {
        ResultInfo<Long> resultInfo = new ResultInfo<>();
        try {
            Assert.isTrue(articleDTO != null, "文章对象不能为空");
            Article article = new Article();
            BeanCopierUtils.copy(articleDTO, article);
            article = articleDao.save(article);
            return resultInfo.success(article.getArticleId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    public ResultInfo<Void> delete(Long articleId, Long userId) throws BusinessException {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            Assert.notNull(articleId, "要删除的id不能为空");
            Assert.notNull(userId, "用户未登录");
            Article article = articleDao.findOne(articleId);
            Assert.notNull(article, "该文章不存在！");
            if (!userId.equals(article.getOwnerUserId())) {
                throw new BusinessException("该文章不属于你");
            }
            article.setDeleteStatus(Const.DELETE_STATUS_YES);
            return resultInfo.success();
        } catch (Exception e) {
            throw new BusinessException("删除文章出现异常：" + e.getMessage());//回滚
        }
    }

    public Page<Article> findByPage(int itemPage) {
        //排序对象
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(itemPage, 10, sort);
        return articleDao.findAll(pageable);
    }



}
