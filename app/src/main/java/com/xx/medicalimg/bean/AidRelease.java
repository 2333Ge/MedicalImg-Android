package com.xx.medicalimg.bean;

import java.io.Serializable;
import java.sql.ResultSet;

import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.constant.ConstantKeyInJson;


/**
 * 收到的病单消息实体类
 */
public class AidRelease extends BasicInfo implements Serializable{
	//对应数据库aid_release
	private int state;
	private String uploadEmployeeId;
	private String handleEmployeeId;
	private AidRecord aidRecord;
	//客户端特有参数
	private Employee upLoadEmployee;
	private Employee handleEmployee;

	public Employee getHandleEmployee() {
		return handleEmployee;
	}

	public void setHandleEmployee(Employee handleEmployee) {
		this.handleEmployee = handleEmployee;
	}

	public Employee getUpLoadEmployee() {
		return upLoadEmployee;
	}

	public void setUpLoadEmployee(Employee upLoadEmployee) {
		this.upLoadEmployee = upLoadEmployee;
	}



	public String getUploadEmployeeId() {
		return uploadEmployeeId;
	}

	public void setUploadEmployeeId(String uploadEmployeeId) {
		this.uploadEmployeeId = uploadEmployeeId;
	}
	public void setAidRecord(AidRecord aidRecord) {
		this.aidRecord = aidRecord;
	}
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	public String getHandleEmployeeId() {
		return handleEmployeeId;
	}

	public void setHandleEmployeeId(String handleEmployeeId) {
		this.handleEmployeeId = handleEmployeeId;
	}

	public AidRecord getAidRecord() {
		return aidRecord;
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
		jsonObject.put(ConstantKeyInJson.AID_RELEASE_AID_RECORD,aidRecord.toJsonObjectWithoutSuperClass());
		jsonObject.put(ConstantKeyInJson.AID_RELEASE_HANDLE_EMPLOYEE_ID,handleEmployeeId);
		jsonObject.put(ConstantKeyInJson.AID_RELEASE_UPLOAD_EMPLOYEE_ID,uploadEmployeeId);
		jsonObject.put(ConstantKeyInJson.AID_RELEASE_STATE,state);
		return jsonObject;
	}

	@Override
	public void transJsonToJavaBean(JSONObject json) {
		if(json == null) return;
		JSONObject basicJson = json.getJSONObject(ConstantKeyInJson.BEAN_BASIC_INFO);
		super.transJsonToJavaBean(basicJson);
		JSONObject aidRecordJson = json.getJSONObject(ConstantKeyInJson.AID_RELEASE_AID_RECORD);
		AidRecord aidRecord = new AidRecord();
		aidRecord.transJsonToJavaBean(aidRecordJson);
		this.aidRecord = aidRecord;
		this.handleEmployeeId = json.getString(ConstantKeyInJson.AID_RELEASE_HANDLE_EMPLOYEE_ID);
		this.uploadEmployeeId = json.getString(ConstantKeyInJson.AID_RELEASE_UPLOAD_EMPLOYEE_ID);
		this.state = json.getInteger(ConstantKeyInJson.AID_RELEASE_STATE);
	}
}
