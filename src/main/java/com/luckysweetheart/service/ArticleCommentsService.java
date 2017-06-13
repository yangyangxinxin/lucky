package com.luckysweetheart.service;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.dao.ArticleCommentsDao;
import com.luckysweetheart.dal.dao.ArticleDao;
import com.luckysweetheart.dal.dao.UserDao;
import com.luckysweetheart.dal.entity.Article;
import com.luckysweetheart.dal.entity.ArticleComments;
import com.luckysweetheart.dal.entity.User;
import com.luckysweetheart.dal.query.ArticleCommentsQuery;
import com.luckysweetheart.dto.ArticleCommentsDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.ResultInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangxin on 2017/6/13.
 */
@Service
public class ArticleCommentsService extends ParameterizedBaseService<ArticleComments, Long> {

    @Resource
    private ArticleDao articleDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ArticleCommentsDao articleCommentsDao;

    /**
     * 添加评论
     *
     * @param content
     * @param userId
     * @param articleId
     * @return
     * @throws BusinessException
     */
    public ResultInfo<Long> addComments(String content, Long userId, Long articleId) throws BusinessException {
        ResultInfo<Long> resultInfo = new ResultInfo<>();
        try {
            specialVal(content, "评论内容不符合规范");
            notNull(userId, "用户id不能为空");
            notNull(articleId, "文章id不能为空");

            User user = userDao.get(userId);
            notNull(user, "该用户不存在！");

            Article article = articleDao.get(articleId);
            notNull(article, "文章不存在！");

            ArticleComments articleComments = new ArticleComments();
            articleComments.setArticleId(articleId);
            articleComments.setContent(content);
            articleComments.setCreateTime(new Date());
            articleComments.setDeleteStatus(Const.DELETE_STATUS_NO);
            articleComments.setUserId(userId);

            Long commentCount = article.getCommentsCount() == null ? 0L : article.getCommentsCount();

            article.setCommentsCount(++commentCount);
            articleDao.update(article);

            Long pk = articleCommentsDao.save(articleComments);

            return resultInfo.success(pk);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    public ResultInfo<PagedResult<ArticleCommentsDTO>> query(ArticleCommentsQuery articleCommentsQuery) {
        ResultInfo<PagedResult<ArticleCommentsDTO>> result = new ResultInfo<>();
        try {
            PagedResult<ArticleComments> pagedResult = super.query(articleCommentsQuery);
            if (pagedResult != null && pagedResult.getSize() > 0) {
                List<ArticleComments> comments = pagedResult.getResults();

                PagedResult<ArticleCommentsDTO> dtoPagedResult = new PagedResult<>();
                dtoPagedResult.setPaged(pagedResult.getPaged());

                List<ArticleCommentsDTO> articleCommentsDTOS = new ArrayList<>();

                for (ArticleComments articleComments : comments) {
                    ArticleCommentsDTO articleCommentsDTO = new ArticleCommentsDTO();
                    BeanCopierUtils.copy(articleComments, articleCommentsDTO);
                    articleCommentsDTOS.add(articleCommentsDTO);
                }

                dtoPagedResult.setResults(articleCommentsDTOS);
                return result.success(dtoPagedResult);
            }
            return result.fail("查询记录为空");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return result.fail(e.getMessage());
        }
    }

}
