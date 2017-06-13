package com.luckysweetheart.dal.query.field;

/**
 * Created by yangxin on 2017/6/13.
 */
public enum ArticleCommentsQueryField implements QueryField {

    COMMENTS_ID("commentsId"),
    ARTICLE_ID("articleId"),
    USER_ID("userId"),
    CONTENT("content"),
    CREATE_TIME("createTime"),
    DELETE_STATUS("deleteStatus");

    private String fieldName;

    ArticleCommentsQueryField(String fieldName) {
        setFiledName(fieldName);
    }


    @Override
    public String getFiledName() {
        return this.fieldName;
    }

    @Override
    public void setFiledName(String fieldName) {
        this.fieldName = fieldName;
    }
}
