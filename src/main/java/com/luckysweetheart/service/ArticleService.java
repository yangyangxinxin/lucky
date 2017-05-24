package com.luckysweetheart.service;

import com.luckysweetheart.dal.dao.ArticleDao;
import com.luckysweetheart.dal.entity.Article;
import com.luckysweetheart.dto.ArticleDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.ResultInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class ArticleService extends BaseService {

    @Resource
    private ArticleDao articleDao;

    public ResultInfo<Long> create(ArticleDTO articleDTO) {
        ResultInfo<Long> resultInfo = new ResultInfo<>();
        try {
            Assert.isTrue(articleDTO != null, "文章对象不能为空");
            Article article = new Article();
            BeanCopierUtils.copy(articleDTO,article);
            article = articleDao.save(article);
            return resultInfo.success(article.getArticleId());
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new BusinessException(e.getMessage());
        }
    }

    public ResultInfo<Void> delete(Long articleId,Long userId){
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try{
            Assert.notNull(articleId,"要删除的id不能为空");
            Assert.notNull(userId,"用户未登录");
            Article article =  articleDao.findOne(articleId);
            Assert.notNull(article,"该文章不存在！");
            if(!userId.equals(article.getOwnerUserId())){
                throw new BusinessException("该文章不属于你");
            }
            article.setDeleteStatus(Article.DELETE_STATUS_YES);
        }catch (Exception e){

        }
        // todo
        return null;
    }

}
