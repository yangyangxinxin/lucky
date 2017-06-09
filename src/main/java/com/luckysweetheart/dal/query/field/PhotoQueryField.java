package com.luckysweetheart.dal.query.field;

/**
 * Created by yangxin on 2017/6/9.
 */
public enum PhotoQueryField implements QueryField {

    PHOTO_ID("photoId"),
    NAME("name"),
    USER_ID("userId"),
    CREATE_TIME("createTime"),
    DELETE_STATUS("deleteStatus"),
    RESOURCE_PATH("resourcePath"),
    PARENT_ID("parentId"),
    IS_DERECTORY("isDirectory")
    ;

    private String filedName;

    PhotoQueryField(String filedName) {
        this.filedName = filedName;
    }

    @Override
    public String getFiledName() {
        return null;
    }
}
