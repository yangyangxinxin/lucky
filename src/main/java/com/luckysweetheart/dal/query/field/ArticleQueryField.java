package com.luckysweetheart.dal.query.field;

/**
 * Created by yangxin on 2017/6/9.
 */
public enum ArticleQueryField implements QueryField {
    ARTICLE_ID("articleId"),
    TITLE("title"),
    USER_ID("ownerUserId"),
    CREATE_TIME("createTime"),
    DELETE_STATUS("deleteStatus"),
    VIEW_COUNT("viewCount"),
    LIKE_COUNT("likeCount"),
    COMMENT_COUNT("commentsCount");

    ArticleQueryField(String fieldName) {
        this.fieldName = fieldName;
    }

    private String fieldName;

    @Override
    public String getFiledName() {
        return this.fieldName;
    }

    @Override
    public void setFiledName(String fieldName) {
        this.fieldName = fieldName;
    }
}
