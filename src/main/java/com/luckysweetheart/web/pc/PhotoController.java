package com.luckysweetheart.web.pc;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.Paged;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.query.PhotoQuery;
import com.luckysweetheart.dal.query.condition.ConditionParam;
import com.luckysweetheart.dal.query.field.PhotoQueryField;
import com.luckysweetheart.dal.query.order.OrderParam;
import com.luckysweetheart.dto.PhotoDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.service.PhotoService;
import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.FileUtil;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Created by yangxin on 2017/5/25.
 */
@RequestMapping("/photo")
@Controller
public class PhotoController extends BaseController {

    @Resource
    private StoreService storeService;

    @Resource
    private PhotoService photoService;

    @RequestMapping("/uploadPage")
    public String uploadPage() {
        return "/photo/uploadPage";
    }

    @RequestMapping(value = "/doUpload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(MultipartFile file) {
        try {
            return photoService.create(file, getLoginUserId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo<>().fail(e.getMessage());
        }
    }

    @RequestMapping(value = "/doUploadMultipart", method = RequestMethod.POST)
    @ResponseBody
    public Object upload() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            List<MultipartFile> files = getMultipartFile("file");
            if (files != null && files.size() > 0) {
                for (MultipartFile file : files) {
                    Map<String, Object> map = new HashMap<>();
                    ResultInfo<Long> resultInfo = photoService.create(file, getLoginUserId());
                    if (resultInfo.isSuccess()) {
                        map.put("success", true);
                        map.put("data", resultInfo.getData());
                    } else {
                        map.put("success", false);
                        map.put("msg", resultInfo.getMsg());
                    }
                    list.add(map);
                }
            }
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo<>().fail(e.getMessage());
        }
    }

    @RequestMapping("/detail/{photoId}")
    public String detail(@PathVariable Long photoId) {
        try {
            ResultInfo<PhotoDTO> resultInfo = photoService.detail(photoId);
            if (resultInfo.isSuccess() && resultInfo.getData() != null) {
                setAttribute("photo", resultInfo.getData());
            } else {
                setAttribute("msg", resultInfo.getMsg());
            }
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
            setAttribute("msg", e.getMessage());
        }
        return "/photo/detail";
    }

    /**
     * 只是跳转页面，数据由js获取
     * @param itemPage
     * @return
     */
    @RequestMapping("/list")
    public String list(Integer itemPage) {
        /*Paged paged = new Paged();
        itemPage = itemPage == null || itemPage == 0 ? 1 : itemPage;
        paged.setPage(itemPage);
        PhotoQuery photoQuery = new PhotoQuery();
        List<ConditionParam<PhotoQueryField>> conditionParams = new ArrayList<>();
        List<OrderParam<PhotoQueryField>> orderParams = new ArrayList<>();

        conditionParams.add(new ConditionParam<PhotoQueryField>(PhotoQueryField.DELETE_STATUS, Const.DELETE_STATUS_NO, ConditionParam.OPERATION_EQ));
        //conditionParams.add(new ConditionParam<PhotoQueryField>(PhotoQueryField.USER_ID, getLoginUserId(), ConditionParam.OPERATION_EQ));

        orderParams.add(new OrderParam<PhotoQueryField>(PhotoQueryField.CREATE_TIME, OrderParam.ORDER_TYPE_DESC));

        photoQuery.setPaged(paged);
        photoQuery.setOrderParams(orderParams);
        photoQuery.setConditionParams(conditionParams);

        ResultInfo<PagedResult<PhotoDTO>> resultInfo = photoService.query(photoQuery);
        if (resultInfo.isSuccess() && resultInfo.getData() != null) {
            setAttribute("photos", resultInfo.getData().getResults());
            setAttribute("paged", resultInfo.getData().getPaged());
            setAttribute("size", resultInfo.getData().getSize());
        } else {
            setAttribute("size", 0);
        }*/
        return "/photo/list";
    }

    @RequestMapping("/queryPage")
    @ResponseBody
    public ResultInfo<Map<String,Object>> queryList(Integer itemPage){
        ResultInfo<Map<String,Object>> resultInfo = new ResultInfo<>();
        Paged paged = new Paged();
        itemPage = itemPage == null || itemPage == 0 ? 1 : itemPage;
        paged.setPage(itemPage);
        PhotoQuery photoQuery = new PhotoQuery();
        List<ConditionParam<PhotoQueryField>> conditionParams = new ArrayList<>();
        List<OrderParam<PhotoQueryField>> orderParams = new ArrayList<>();

        conditionParams.add(new ConditionParam<PhotoQueryField>(PhotoQueryField.DELETE_STATUS, Const.DELETE_STATUS_NO, ConditionParam.OPERATION_EQ));
        //conditionParams.add(new ConditionParam<PhotoQueryField>(PhotoQueryField.USER_ID, getLoginUserId(), ConditionParam.OPERATION_EQ));

        orderParams.add(new OrderParam<PhotoQueryField>(PhotoQueryField.CREATE_TIME, OrderParam.ORDER_TYPE_DESC));

        photoQuery.setPaged(paged);
        photoQuery.setOrderParams(orderParams);
        photoQuery.setConditionParams(conditionParams);

        ResultInfo<PagedResult<PhotoDTO>> resultInfo1 = photoService.query(photoQuery);

        if(resultInfo1.isSuccess()){
            PagedResult<PhotoDTO> result = resultInfo1.getData();
            Integer totalPage = result.getPaged().getPages();
            Map<String,Object> map = new HashMap<>();
            map.put("list",result.getResults());
            map.put("totalPage",totalPage);
            return resultInfo.success(map);
        }

        return resultInfo.fail(resultInfo1.getMsg());


    }

    /**
     * 删除
     *
     * @param photoId
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public ResultInfo<Void> delete(Long photoId) {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            return photoService.delete(photoId, getLoginUserId());
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }

}
