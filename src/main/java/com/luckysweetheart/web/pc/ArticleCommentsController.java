package com.luckysweetheart.web.pc;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.Paged;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.query.ArticleCommentsQuery;
import com.luckysweetheart.dal.query.condition.ConditionParam;
import com.luckysweetheart.dal.query.field.ArticleCommentsQueryField;
import com.luckysweetheart.dal.query.order.OrderParam;
import com.luckysweetheart.dto.ArticleCommentsDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.service.ArticleCommentsService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping("/queryComments")
    @ResponseBody
    public ResultInfo<List<ArticleCommentsDTO>> queryComments(Long articleId, Integer itemPage) {
        ResultInfo<List<ArticleCommentsDTO>> resultInfo = new ResultInfo<>();
        ArticleCommentsQuery articleCommentsQuery = new ArticleCommentsQuery();

        if (articleId == null) {
            return resultInfo.fail("id不能为空");
        }
        itemPage = itemPage == null ? 1 : itemPage;
        Paged paged = new Paged();
        paged.setPage(itemPage);

        List<ConditionParam<ArticleCommentsQueryField>> conditionParams = new ArrayList<>();
        List<OrderParam<ArticleCommentsQueryField>> orderParams = new ArrayList<>();

        conditionParams.add(new ConditionParam<ArticleCommentsQueryField>(ArticleCommentsQueryField.ARTICLE_ID, articleId, ConditionParam.OPERATION_EQ));

        conditionParams.add(new ConditionParam<ArticleCommentsQueryField>(ArticleCommentsQueryField.DELETE_STATUS, Const.DELETE_STATUS_NO, ConditionParam.OPERATION_EQ));

        orderParams.add(new OrderParam<ArticleCommentsQueryField>(ArticleCommentsQueryField.CREATE_TIME, OrderParam.ORDER_TYPE_DESC));

        articleCommentsQuery.setPaged(paged);

        articleCommentsQuery.setOrderParams(orderParams);
        articleCommentsQuery.setConditionParams(conditionParams);
        ResultInfo<PagedResult<ArticleCommentsDTO>> result = articleCommentsService.query(articleCommentsQuery);
        if (result.isSuccess() && result.getData().getSize() > 0) {
            return resultInfo.success(result.getData().getResults());
        }
        return resultInfo.fail(result.getMsg());
    }
}
