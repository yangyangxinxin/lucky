package com.luckysweetheart.web.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yangxin on 2017/5/31.
 */
public class UploadUtils {

    private static Logger logger = LoggerFactory.getLogger(UploadUtils.class);

    private static String extension;

    @Value("${extension.pic}")
    public static void setExtension(String extension) {
        UploadUtils.extension = extension;
    }

    public static boolean isPic(String extension) {
        if (StringUtils.isBlank(extension)) {
            return false;
        }
        try {
            List<String> list = Arrays.asList(UploadUtils.extension.split(","));
            if (list.contains(extension.toUpperCase())) {
                return true;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }

}
