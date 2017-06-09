package com.luckysweetheart.dal.query.param;

/**
 * Created by wlinguo on 2016/11/16.
 */
public class QueryConditionParam {

    /**
     * 操作符
     */
    public static final String OPERATION_EQ = "=";
    public static final String OPERATION_NE = "<>";
    public static final String OPERATION_GT = ">";
    public static final String OPERATION_GE = ">=";
    public static final String OPERATION_LT = "<";
    public static final String OPERATION_LE = "<=";
    public static final String OPERATION_ISNULL = "IS NULL";
    public static final String OPERATION_ISNOTNULL = "IS NOT NULL";
    public static final String OPERATION_LIKE = "LIKE";
    public static final String OPERATION_NOTLIKE = "NOT LIKE";
    public static final String OPERATION_IN = "IN";

    /**
     * 连接符
     */
    public static final String CONNECTOR_AND = "AND";
    public static final String CONNECTOR_OR = "OR";

    /**
     * 字段名
     */
    private String queryField;

    /**
     * 值
     */
    private Object value;

    /**
     * 操作符
     */
    private String operation;

    /**
     * 连接符
     */
    private String connector;

    public QueryConditionParam(String queryField, Object value, String operation, String connector) {
        this.queryField = queryField;
        this.value = value;
        this.operation = operation;
        this.connector = connector;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getQueryField() {
        return queryField;
    }

    public void setQueryField(String queryField) {
        this.queryField = queryField;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
