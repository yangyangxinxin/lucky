package com.luckysweetheart.dal.query.field;

import java.io.Serializable;

/**
 * Created by wlinguo on 2016/11/16.
 */
public interface QueryField extends Serializable {

    String getFiledName();

    void setFiledName(String fieldName);

}
