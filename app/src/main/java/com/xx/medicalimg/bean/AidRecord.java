package com.xx.medicalimg.bean;

import android.util.Log;

import java.io.Serializable;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.constant.ConstantKeyInJson;
import com.xx.medicalimg.util.LogUtils;

/**
 * 上传的病单实体类，存放解析后的信息
 *
 */
public class AidRecord extends BasicInfo implements Serializable {
	private int sex;
	private int age;
	private String 	chiefComplaint;//主诉
	private String clinicalManifestation;//临床表现
	private String 	imagingFeatures;//影像表现
	private List<String> imgList;
	
	
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getChiefComplaint() {
		return chiefComplaint;
	}
	public void setChiefComplaint(String chiefComplaint) {
		this.chiefComplaint = chiefComplaint;
	}
	public String getClinicalManifestation() {
		return clinicalManifestation;
	}
	public void setClinicalManifestation(String clinicalManifestation) {
		this.clinicalManifestation = clinicalManifestation;
	}
	public String getImagingFeatures() {
		return imagingFeatures;
	}
	public void setImagingFeatures(String imagingFeatures) {
		this.imagingFeatures = imagingFeatures;
	}
	public List<String> getImgList() {
		return imgList;
	}
	public void setImgList(List<String> imgList) {
		this.imgList = imgList;
	}
	

	@Override
	public JSONObject toJsonObject() {
		JSONObject jsonObject = toJsonObjectWithoutSuperClass();
		jsonObject.put(ConstantKeyInJson.BEAN_BASIC_INFO,super.toJsonObject());
		return jsonObject;
	}
	@Override
	public JSONObject toJsonObjectWithoutSuperClass() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ConstantKeyInJson.AID_RECORD_AGE,age);
		jsonObject.put(ConstantKeyInJson.AID_RECORD_SEX,sex);
		jsonObject.put(ConstantKeyInJson.AID_RECORD_CHIEF_COMPLAINT,chiefComplaint);
		jsonObject.put(ConstantKeyInJson.AID_RECORD_CLINICAL_MANIFESTATION,clinicalManifestation);
		jsonObject.put(ConstantKeyInJson.AID_RECORD_IMAGEING_FEATURE,imagingFeatures);
		jsonObject.put(ConstantKeyInJson.AID_RECORD_IMG_LIST,imgList);
		return jsonObject;
	}
	@Override
	public void transJsonToJavaBean(JSONObject json) {
		if(json == null) return;
		JSONObject basicJson = json.getJSONObject(ConstantKeyInJson.BEAN_BASIC_INFO);
		super.transJsonToJavaBean(basicJson);
		this.age = json.getInteger(ConstantKeyInJson.AID_RECORD_AGE);
		this.sex = json.getInteger(ConstantKeyInJson.AID_RECORD_SEX);
		this.chiefComplaint = json.getString(ConstantKeyInJson.AID_RECORD_CHIEF_COMPLAINT);
		this.clinicalManifestation = json.getString(ConstantKeyInJson.AID_RECORD_CLINICAL_MANIFESTATION);
		this.imagingFeatures = json.getString(ConstantKeyInJson.AID_RECORD_IMAGEING_FEATURE);
		transJsonToImageList(json.getString(ConstantKeyInJson.AID_RECORD_IMG_LIST));
	}

	/**
	 * 这部分和服务器端有出入,服务器端是jsonObject,此处是jsonArray
	 * @param json
	 */
	private void transJsonToImageList(String json) {
		JSONArray imgJsonArray = JSONArray.parseArray(json);
		if(imgList == null) imgList = new ArrayList<>();
		if(imgJsonArray == null) return;
		for(int i = 0; i < imgJsonArray.size(); i++) {
			this.imgList.add(imgJsonArray.get(i).toString());
		}
	}

}
