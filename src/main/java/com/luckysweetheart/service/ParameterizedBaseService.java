package com.luckysweetheart.service;

import com.luckysweetheart.common.Paged;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.dao.ParameterizedBaseDAO;
import com.luckysweetheart.dal.query.param.QueryConditionParam;
import com.luckysweetheart.dal.query.param.QueryOrderParam;
import com.luckysweetheart.dal.query.BaseQuery;
import com.luckysweetheart.dal.query.condition.ConditionParam;
import com.luckysweetheart.dal.query.field.QueryField;
import com.luckysweetheart.dal.query.order.OrderParam;
import com.luckysweetheart.utils.ValidateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
public abstract class ParameterizedBaseService<T, PK extends Serializable> extends ParameterizedBaseDAO<T, PK> {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 查询 返回带分页的结果集
     * @param query
     * @param <Q>
     * @return
     */
    public <Q extends BaseQuery> PagedResult<T> query(Q query) {
        Paged paged = query.getPaged();
        List<ConditionParam<QueryField>> conditionParams = query.getConditionParams();
        List<QueryConditionParam> queryConditionParams = null;
        if (null != conditionParams) {
            queryConditionParams = new ArrayList<>(conditionParams.size());
            for (ConditionParam<QueryField> conditionParam : conditionParams) {
                QueryConditionParam queryParam = new QueryConditionParam(conditionParam.getQueryField().getFiledName(), conditionParam.getValue(), conditionParam.getOperation(), conditionParam.getConnector());
                queryConditionParams.add(queryParam);
            }
        }
        List<OrderParam<?>> orderParams = query.getOrderParams();
        List<QueryOrderParam> queryOrderParams = null;
        if (null != orderParams) {
            queryOrderParams = new ArrayList<>(orderParams.size());
            for (OrderParam<?> orderParam : orderParams) {
                QueryOrderParam queryOrderParam = new QueryOrderParam(orderParam.getOrderField().getFiledName(), orderParam.getOrderType());
                queryOrderParams.add(queryOrderParam);
            }
        }
        return query(paged, queryConditionParams, queryOrderParams, entityClass);
    }

    /**
     * 查询，只返回列表，不返回分页信息
     * @param query
     * @param <Q>
     * @return
     */
    public <Q extends BaseQuery> List<T> queryAll(Q query) {
        PagedResult<T> result = query(query);
        if (result != null && result.getResults() != null && result.getSize() > 0) {
            return result.getResults();
        }
        return null;
    }

    protected void notNull(Object obj, String message) {
        Assert.notNull(obj, message);
    }

    protected void isTrue(boolean flag, String message) {
        Assert.isTrue(flag, message);
    }

    protected void specialVal(String value,String message){
        Assert.isTrue(ValidateUtil.specialVal(value),message);
    }

}
