package com.luckysweetheart.web.pc;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.Paged;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.query.ArticleCommentsQuery;
import com.luckysweetheart.dal.query.ArticleQuery;
import com.luckysweetheart.dal.query.condition.ConditionParam;
import com.luckysweetheart.dal.query.field.ArticleQueryField;
import com.luckysweetheart.dal.query.order.OrderParam;
import com.luckysweetheart.dto.ArticleDTO;
import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.service.ArticleCommentsService;
import com.luckysweetheart.service.ArticleService;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangxin on 2017/6/8.
 */
@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleCommentsService articleCommentsService;

    @Resource
    private UserService userService;

    @RequestMapping("/create")
    public String create() {
        return "/article/create";
    }

    @RequestMapping("/doCreate")
    @ResponseBody
    public ResultInfo<Long> doCreate(String title, String content) {
        ResultInfo<Long> resultInfo = new ResultInfo<>();

        UserDTO userDTO = userService.findById(getLoginUserId());
        if (userDTO == null) {
            return resultInfo.fail("用户不存在");
        }

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setContent(content);
        articleDTO.setOwnerUserId(getLoginUserId());
        articleDTO.setCreateTime(new Date());
        articleDTO.setTitle(title);
        articleDTO.setDeleteStatus(Const.DELETE_STATUS_NO);
        articleDTO.setLikeCount(0L);
        articleDTO.setViewCount(0L);
        articleDTO.setCommentsCount(0L);
        articleDTO.setAuthor(userDTO.getUsername());

        try {
            return articleService.create(articleDTO);
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }

    }

    @RequestMapping("/detail")
    public String detail(Long articleId) {
        /*ResultInfo<ArticleDTO> resultInfo = articleService.findOne(articleId);
        if (resultInfo.isSuccess()) {
            setAttribute("article", resultInfo.getData());
        }*/
        return "/article/detail";
    }

    @RequestMapping("/getDetail")
    @ResponseBody
    public ResultInfo<ArticleDTO> getDetail(Long articleId) {
        return articleService.findOne(articleId);
    }

    @RequestMapping("/list")
    public String list(Integer itemPage) {
        itemPage = itemPage == null ? 1 : itemPage;
        Paged paged = new Paged();
        paged.setPage(itemPage);

        ArticleQuery articleQuery = new ArticleQuery();

        List<ConditionParam<ArticleQueryField>> conditionParams = new ArrayList<>();
        List<OrderParam<ArticleQueryField>> orderParams = new ArrayList<>();

        conditionParams.add(new ConditionParam<ArticleQueryField>(ArticleQueryField.DELETE_STATUS, Const.DELETE_STATUS_NO, ConditionParam.OPERATION_EQ));
        //conditionParams.add(new ConditionParam<ArticleQueryField>(ArticleQueryField.USER_ID, getLoginUserId(), ConditionParam.OPERATION_EQ));

        orderParams.add(new OrderParam<ArticleQueryField>(ArticleQueryField.CREATE_TIME, OrderParam.ORDER_TYPE_DESC));

        articleQuery.setPaged(paged);
        articleQuery.setConditionParams(conditionParams);
        articleQuery.setOrderParams(orderParams);

        ResultInfo<PagedResult<ArticleDTO>> resultInfo = articleService.query(articleQuery);
        if (resultInfo.isSuccess()) {
            PagedResult<ArticleDTO> pagedResult = resultInfo.getData();
            setAttribute("articles", pagedResult.getResults());
            setAttribute("paged", pagedResult.getPaged());
            setAttribute("size", pagedResult.getSize());
        } else {
            setAttribute("size", 0);
        }
        return "/article/list";
    }

    @RequestMapping("/editPage")
    public String editPage(Long articleId) {
        return "/article/editPage";
    }

    @RequestMapping("/modify")
    @ResponseBody
    public ResultInfo<Void> modify(String content, String title, Long articleId) {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            return articleService.modify(getLoginUserId(), content, title, articleId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }

    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo<Void> delete(Long articleId) {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            return articleService.delete(articleId, getLoginUserId());
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }

}
