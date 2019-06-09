package com.xx.medicalimg.bean;

import java.io.Serializable;
import java.sql.ResultSet;

import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.constant.ConstantKeyInJson;
import com.xx.medicalimg.util.LogUtils;
/**
 * 病例实体类
 *
 */
public class AidCase extends BasicInfo implements Serializable {
	private String aidRecordID;
	private String aidReleaseID;
	private String aidTreatID;
	private AidRelease aidRelease;
	private AidRecord aidRecord;
	private AidTreat aidTreat;
	


	@Override
	public JSONObject toJsonObjectExceptNull() {
		JSONObject jsonObject = toJsonObjectExceptNullWithoutSuperClass();
		jsonObject.put(ConstantKeyInJson.BEAN_BASIC_INFO,super.toJsonObject());
		return jsonObject;
	}
	
	@Override
	public JSONObject toJsonObjectExceptNullWithoutSuperClass() {
		JSONObject jsonObject = new JSONObject();
    	if(aidReleaseID != null) jsonObject.put(ConstantKeyInJson.AID_CASE_AID_RELEASE_ID, aidReleaseID);
    	if(aidRecordID != null) jsonObject.put(ConstantKeyInJson.AID_CASE_AID_RECORD_ID, aidRecordID);
    	if(aidTreatID != null) jsonObject.put(ConstantKeyInJson.AID_CASE_AID_TREAT_ID, aidTreatID);
    	if(aidRecord != null) jsonObject.put(ConstantKeyInJson.AID_CASE_AID_RECORD, aidRecord.toJsonObjectExceptNull());
    	if(aidRelease != null) jsonObject.put(ConstantKeyInJson.AID_CASE_AID_RELEASE, aidRelease.toJsonObjectExceptNull());
    	if(aidTreat != null) jsonObject.put(ConstantKeyInJson.AID_CASE_AID_TREAT, aidTreat.toJsonObjectExceptNull());
    	return jsonObject;
	}
	
	@Override
	public void transJsonToJavaBean(JSONObject json) {
		if(json == null) return;
		JSONObject basicJson = json.getJSONObject(ConstantKeyInJson.BEAN_BASIC_INFO);
		super.transJsonToJavaBean(basicJson);
		this.setAidRecordID(json.getString(ConstantKeyInJson.AID_CASE_AID_RECORD_ID));
		this.setAidReleaseID(json.getString(ConstantKeyInJson.AID_CASE_AID_RECORD_ID));
		this.setAidTreatID(json.getString(ConstantKeyInJson.AID_CASE_AID_TREAT_ID));
		if(aidRecord == null) {
			aidRecord = new AidRecord();
		}
		if(aidRelease == null) {
			aidRelease = new AidRelease();
		}
		if(aidTreat == null) {
			aidTreat = new AidTreat();
		}
		this.aidRecord.transJsonToJavaBean(json.getJSONObject(ConstantKeyInJson.AID_CASE_AID_RECORD));
		this.aidRelease.transJsonToJavaBean(json.getJSONObject(ConstantKeyInJson.AID_CASE_AID_RELEASE));
		this.aidTreat.transJsonToJavaBean(json.getJSONObject(ConstantKeyInJson.AID_CASE_AID_TREAT));
	
	}
	public String getAidReleaseID() {
		return aidReleaseID;
	}
	public void setAidReleaseID(String aidReleaseID) {
		this.aidReleaseID = aidReleaseID;
	}
	public String getAidTreatID() {
		return aidTreatID;
	}
	public void setAidTreatID(String aidTreatID) {
		this.aidTreatID = aidTreatID;
	}
	public String getAidRecordID() {
		return aidRecordID;
	}
	public void setAidRecordID(String aidRecordID) {
		this.aidRecordID = aidRecordID;
	}
	public AidRelease getAidRelease() {
		return aidRelease;
	}
	public void setAidRelease(AidRelease aidRelease) {
		this.aidRelease = aidRelease;
	}
	public AidRecord getAidRecord() {
		return aidRecord;
	}
	public void setAidRecord(AidRecord aidRecord) {
		this.aidRecord = aidRecord;
	}
	public AidTreat getAidTreat() {
		return aidTreat;
	}
	public void setAidTreat(AidTreat aidTreat) {
		this.aidTreat = aidTreat;
	}
	
}
