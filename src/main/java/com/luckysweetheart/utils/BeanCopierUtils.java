package com.luckysweetheart.utils;

import net.sf.cglib.beans.BeanCopier;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wlinguo on 14-3-31.
 */
public class BeanCopierUtils {
	// 由于创建BeanCopier对性能消耗，将其放入内存中方便调用
	private static Map<String, BeanCopier> beanCopierMap = new HashMap<String, BeanCopier>();

	public static void copy(Object source, Object target) {
		String beanKey = generateKey(source.getClass(), target.getClass());
		BeanCopier copier = null;
		if (!beanCopierMap.containsKey(beanKey)) {
			copier = BeanCopier.create(source.getClass(), target.getClass(), false);
			beanCopierMap.put(beanKey, copier);
		} else {
			copier = beanCopierMap.get(beanKey);
		}
		copier.copy(source, target, null);
	}

	private static String generateKey(Class<?> class1, Class<?> class2) {
		return class1.toString() + " to " + class2.toString();
	}
}
