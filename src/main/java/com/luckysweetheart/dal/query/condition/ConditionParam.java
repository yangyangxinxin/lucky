package com.luckysweetheart.dal.query.condition;



import com.luckysweetheart.dal.query.field.QueryField;

import java.io.Serializable;

/**
 * @author wlinguo
 * @version 1.0
 */
public class ConditionParam<T extends QueryField> implements Serializable {

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
    public static final String OPERATION_NOT_EQ = "<>";//不等于
    
    /**
     * 连接符
     */
    public static final String CONNECTOR_AND = "AND";
    public static final String CONNECTOR_OR = "OR";

    /**
     * 字段名
     */
    private T queryField;

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

    public ConditionParam(T queryField, Object value, String operation) {
        this.queryField = queryField;
        this.value = value;
        this.operation = operation;
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

    public T getQueryField() {
        return queryField;
    }

    public void setQueryField(T queryField) {
        this.queryField = queryField;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}