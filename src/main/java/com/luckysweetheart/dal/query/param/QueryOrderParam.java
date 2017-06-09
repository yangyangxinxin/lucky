package com.luckysweetheart.dal.query.param;

/**
 * @author wlinguo
 * @version 1.0
 */
public class QueryOrderParam {
    public static final String ORDER_TYPE_ASC = "asc";
    public static final String ORDER_TYPE_DESC = "desc";

    /**
     * 排序字段
     */
    private String orderField;

    /**
     * 排序顺序
     */
    private String orderType;

    public QueryOrderParam(String orderField, String orderType) {
        this.orderField = orderField;
        this.orderType = orderType;
    }

    public String getOrderField() {
        return orderField;
    }

    public void setOrderField(String orderField) {
        this.orderField = orderField;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
