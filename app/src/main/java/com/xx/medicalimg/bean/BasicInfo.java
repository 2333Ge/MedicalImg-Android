package com.xx.medicalimg.bean;

import java.io.Serializable;
import java.sql.ResultSet;

import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.abs.other.TransJavaBeanToJsonAble;
import com.xx.medicalimg.abs.other.TransJsonToJavaBeanAble;
import com.xx.medicalimg.constant.ConstantKeyInJson;
/**
 * 实体类，和数据库对应，这是所有实体类都有的字段，作为基类
 *
 */
public class BasicInfo implements TransJavaBeanToJsonAble, TransJsonToJavaBeanAble, Serializable {
	//为空的时候可以插入空,注意引用类型都要判断空情况
	private String id;
	private String greateTime;
	private String modifiedTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGreateTime() {
		return greateTime;
	}
	public void setGreateTime(String greateTime) {
		this.greateTime = greateTime;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	@Override
	public JSONObject toJsonObject() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ConstantKeyInJson.ID, id);
		jsonObject.put(ConstantKeyInJson.GMT_CREATE, greateTime);
		jsonObject.put(ConstantKeyInJson.GMT_MODIFIED,modifiedTime);
		return jsonObject;
	}

	@Override
	public JSONObject toJsonObjectWithoutSuperClass() {
		return toJsonObject();
	}
	@Override
	public JSONObject toJsonObjectExceptNull() {
		JSONObject jsonObject = new JSONObject();
		if(id != null) jsonObject.put(ConstantKeyInJson.ID, id);
		if(greateTime != null) jsonObject.put(ConstantKeyInJson.GMT_CREATE, greateTime);
		if(modifiedTime != null) jsonObject.put(ConstantKeyInJson.GMT_MODIFIED,modifiedTime);
		return jsonObject;
	}
	@Override
	public JSONObject toJsonObjectExceptNullWithoutSuperClass() {
		return toJsonObjectExceptNull();
	}
	@Override
	public void transJsonToJavaBean(JSONObject json) {
		if(json == null) return;
		this.id = json.getString(ConstantKeyInJson.ID);
		this.greateTime = json.getString(ConstantKeyInJson.GMT_CREATE);
		this.modifiedTime = json.getString(ConstantKeyInJson.GMT_MODIFIED);
	}
}
