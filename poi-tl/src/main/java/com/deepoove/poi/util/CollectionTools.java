package com.deepoove.poi.util;


import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

/**
 * @description: 集合操作工具类
 * @author: Weijie.Wu
 * @create: 2021-09-30 08:52
 */
public class CollectionTools {

	public static <T> Collection<T> reverseCollection(Class<T> clazz, Collection<T> collection) {
		T[] array = (T[]) Array.newInstance(clazz, collection.size());
		collection.toArray(array);
		CollectionUtils.reverseArray(array);
		collection.clear();
		collection.addAll(Arrays.asList(array));
		return collection;
	}

}
