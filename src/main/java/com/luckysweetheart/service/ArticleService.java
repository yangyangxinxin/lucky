package com.luckysweetheart.service;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.dao.ArticleDao;
import com.luckysweetheart.dal.entity.Article;
import com.luckysweetheart.dal.query.ArticleQuery;
import com.luckysweetheart.dto.ArticleDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.DateUtil;
import com.luckysweetheart.utils.ResultInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class ArticleService extends ParameterizedBaseService<Article, Long> {

    @Resource
    private ArticleDao articleDao;

    public ResultInfo<Long> create(ArticleDTO articleDTO) throws BusinessException {
        ResultInfo<Long> resultInfo = new ResultInfo<>();
        try {
            Assert.isTrue(articleDTO != null, "文章对象不能为空");
            isTrue(StringUtils.isNotBlank(articleDTO.getTitle()), "标题不能为空");
            isTrue(StringUtils.isNotBlank(articleDTO.getContent()), "内容不能为空");
            Article article = new Article();
            BeanCopierUtils.copy(articleDTO, article);
            Long pk = articleDao.save(article);
            return resultInfo.success(pk);
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
            Article article = articleDao.get(articleId);
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

    /**
     * 修改文章
     *
     * @param userId
     * @param content
     * @param title
     * @param articleId
     * @return
     * @throws BusinessException
     */
    public ResultInfo<Void> modify(Long userId, String content, String title, Long articleId) throws BusinessException {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            notNull(userId, "用户id不能为空");
            notNull(content, "内容不能为空");
            notNull(title, "标题不能为空");
            notNull(articleId, "要修改的文章id不能为空");

            Article article = articleDao.get(articleId);

            notNull(article, "该文章不存在");
            isTrue(article.getOwnerUserId().equals(userId), "该文章不属于你");

            article.setContent(content);
            article.setTitle(title);
            article.setUpdateTime(new Date());

            articleDao.update(article);

            return resultInfo.success();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    public ResultInfo<ArticleDTO> findOne(Long articleId) {
        ResultInfo<ArticleDTO> resultInfo = new ResultInfo<>();
        try {
            notNull(articleId, "文章id不能为空");
            Article article = articleDao.get(articleId);
            notNull(article, "文章不存在");
            ArticleDTO articleDTO = new ArticleDTO();
            BeanCopierUtils.copy(article, articleDTO);
            articleDTO.setCreateTimeFormat(DateUtil.formatDate(articleDTO.getCreateTime()));
            articleDTO.setUpdateTimeFormat(DateUtil.formatDate(articleDTO.getUpdateTime()));
            //articleDTO.setContent(HtmlUtils.htmlEscape(articleDTO.getContent()));
            return resultInfo.success(articleDTO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }

    /**
     * 根据各种条件查询文章
     *
     * @param articleQuery
     * @return
     */
    public ResultInfo<PagedResult<ArticleDTO>> query(ArticleQuery articleQuery) {
        ResultInfo<PagedResult<ArticleDTO>> resultInfo = new ResultInfo<>();
        try {
            PagedResult<Article> pagedResult = super.query(articleQuery);

            if (pagedResult != null && pagedResult.getSize() > 0) {

                PagedResult<ArticleDTO> dtoPagedResult = new PagedResult<>();

                List<ArticleDTO> articleDTOS = new ArrayList<>();
                for (Article article : pagedResult.getResults()) {
                    ArticleDTO articleDTO = new ArticleDTO();
                    BeanCopierUtils.copy(article, articleDTO);
                    articleDTOS.add(articleDTO);
                }
                dtoPagedResult.setPaged(pagedResult.getPaged());
                dtoPagedResult.setResults(articleDTOS);
                return resultInfo.success(dtoPagedResult);
            }
            return resultInfo.fail("查询失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }


}
