package com.luckysweetheart.dal.query.order;



import com.luckysweetheart.dal.query.field.QueryField;

import java.io.Serializable;

/**
 * @author wlinguo
 * @version 1.0
 */
public class OrderParam<T extends QueryField> implements Serializable {

    public static final String ORDER_TYPE_ASC = "asc";
    public static final String ORDER_TYPE_DESC = "desc";
    public static final String ORDER_GROUP_BY = "GROUP BY";

    /**
     * 排序字段
     */
    private T orderField;

    /**
     * 排序顺序
     */
    private String orderType;

    public OrderParam(T orderField, String orderType) {
        this.orderField = orderField;
        this.orderType = orderType;
    }

    public T getOrderField() {
        return orderField;
    }

    public void setOrderField(T orderField) {
        this.orderField = orderField;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
