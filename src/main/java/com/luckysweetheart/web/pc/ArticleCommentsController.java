package com.luckysweetheart.web.pc;

import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.query.ArticleCommentsQuery;
import com.luckysweetheart.dto.ArticleCommentsDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.service.ArticleCommentsService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/6/14.
 */
@Controller
@RequestMapping("/articleComments")
public class ArticleCommentsController extends BaseController {

    @Resource
    private ArticleCommentsService articleCommentsService;

    @RequestMapping("/addComments")
    @ResponseBody
    public ResultInfo<Long> addComments(String content, Long articleId) {
        try {
            return articleCommentsService.addComments(content, getLoginUserId(), articleId);
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
            return getFailResult(e.getMessage(), Long.class);
        }
    }

    public ResultInfo<PagedResult<ArticleCommentsDTO>> queryComments(Long articleId){
        ArticleCommentsQuery articleCommentsQuery = new ArticleCommentsQuery();

        if(articleId == null){

        }

        return articleCommentsService.query(articleCommentsQuery);
    }
}
