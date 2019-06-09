package com.xx.medicalimg.abs.other;

import com.alibaba.fastjson.JSONObject;
/**
 * 实体类转化成JSONObject接口
 */
public interface TransJavaBeanToJsonAble {
	/**
	 * 将实体类转换成jsonObject,用于网络传输
	 * @return fastJson的JSONObject
	 */
	 JSONObject toJsonObject();
	/**
	 * 将实体类转换成jsonObject,不包含父类（便于重载，多继承时调用父类toJsonObject，便于获取父类元素，避免重复获取）
	 * @return fastJson的JSONObject
	 */
	 JSONObject toJsonObjectWithoutSuperClass();
	/**
	 * 将实体类转换成jsonObject, 不包含没有初始化的参数,减少于网络传输消耗
	 * @return fastJson的JSONObject
	 */
	 JSONObject toJsonObjectExceptNull();
	/**
	 * 将实体类转换成jsonObject, 不包含没有初始化的参数，不包含父类，减少于网络传输消耗
	 * （便于重载，多继承时调用父类toJsonObject，便于获取父类元素，避免重复获取）
	 * @return fastJson的JSONObject
	 */
	 JSONObject toJsonObjectExceptNullWithoutSuperClass();
}
