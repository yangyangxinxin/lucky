package com.luckysweetheart.dal.query;


import com.luckysweetheart.common.Paged;
import com.luckysweetheart.dal.query.condition.ConditionParam;
import com.luckysweetheart.dal.query.field.QueryField;
import com.luckysweetheart.dal.query.order.OrderParam;

import java.io.Serializable;
import java.util.List;

/**
 * @author wlinguo
 * @version 1.0
 */
public class BaseQuery<T extends QueryField> implements Serializable {

    /**
     * 分页条件
     */
    private Paged paged;

    /**
     * 参数条件
     */
    private List<ConditionParam<T>> conditionParams;

    /**
     * 排序条件
     */
    private List<OrderParam<T>> orderParams;

    public BaseQuery() {
    }

    public BaseQuery(Paged paged, List<ConditionParam<T>> conditionParams, List<OrderParam<T>> orderParams) {
        this.paged = paged;
        this.conditionParams = conditionParams;
        this.orderParams = orderParams;
    }

    public Paged getPaged() {
        return paged;
    }

    public void setPaged(Paged paged) {
        this.paged = paged;
    }

    public List<ConditionParam<T>> getConditionParams() {
        return conditionParams;
    }

    public void setConditionParams(List<ConditionParam<T>> conditionParams) {
        this.conditionParams = conditionParams;
    }

    public List<OrderParam<T>> getOrderParams() {
        return orderParams;
    }

    public void setOrderParams(List<OrderParam<T>> orderParams) {
        this.orderParams = orderParams;
    }
}
