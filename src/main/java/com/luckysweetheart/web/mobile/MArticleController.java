package com.luckysweetheart.web.mobile;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.Paged;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.query.ArticleQuery;
import com.luckysweetheart.dal.query.condition.ConditionParam;
import com.luckysweetheart.dal.query.field.ArticleQueryField;
import com.luckysweetheart.dal.query.order.OrderParam;
import com.luckysweetheart.dto.ArticleDTO;
import com.luckysweetheart.service.ArticleService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxin on 2017/7/18.
 */
@Controller
@RequestMapping("/m/article")
public class MArticleController extends BaseController {

    @Resource
    private ArticleService articleService;

    @RequestMapping("/list")
    public String list() {
        return "/mobile/article/list";
    }

    @RequestMapping("/queryByPaged")
    @ResponseBody
    public ResultInfo<List<ArticleDTO>> queryByPaged(Integer itemPage) {
        ResultInfo<List<ArticleDTO>> resultInfo = new ResultInfo<>();
        Paged paged = new Paged();
        List<ConditionParam<ArticleQueryField>> conditionParams = new ArrayList<>();
        List<OrderParam<ArticleQueryField>> orderParams = new ArrayList<>();

        conditionParams.add(new ConditionParam<ArticleQueryField>(ArticleQueryField.DELETE_STATUS, Const.DELETE_STATUS_NO, ConditionParam.OPERATION_EQ));

        orderParams.add(new OrderParam<ArticleQueryField>(ArticleQueryField.CREATE_TIME, OrderParam.ORDER_TYPE_DESC));

        paged.setPage(itemPage);

        ArticleQuery articleQuery = new ArticleQuery();

        articleQuery.setPaged(paged);
        articleQuery.setOrderParams(orderParams);
        articleQuery.setConditionParams(conditionParams);

        ResultInfo<PagedResult<ArticleDTO>> result = articleService.query(articleQuery);
        if (result.isSuccess() && result.getData() != null) {
            List<ArticleDTO> articleDTOS = result.getData().getResults();
            return resultInfo.success(articleDTOS);
        }
        return resultInfo.fail(result.getMsg());
    }

    @RequestMapping("/detail/{articleId}")
    public String detailPage(@PathVariable Long articleId) {
        setAttribute("articleId", articleId);
        return "/mobile/article/detail";
    }

    @RequestMapping("/queryDetail")
    @ResponseBody
    public ResultInfo<ArticleDTO> queryDetail(Long articleId) {
        ResultInfo<ArticleDTO> resultInfo = new ResultInfo<>();
        try {
            return articleService.findOne(articleId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }


}
