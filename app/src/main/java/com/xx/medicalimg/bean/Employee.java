package com.xx.medicalimg.bean;

import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.constant.ConstantKeyInJson;

import java.io.Serializable;
/**
 *  医务人员实体类
 *
 */
public class Employee extends BasicInfo implements Serializable {
	
	private String employeeId;
	private String department;
	private String room;
	private String hospitaId;
	private User basicInfo;

	@Override
	public void transJsonToJavaBean(JSONObject json) {
		if(json == null) return;
		JSONObject basicJson = json.getJSONObject(ConstantKeyInJson.BEAN_BASIC_INFO);
		super.transJsonToJavaBean(basicJson);
		this.employeeId = json.getString(ConstantKeyInJson.EMPLOYEE_INFO_EMPLOYEE_ID);
		this.department = json.getString(ConstantKeyInJson.EMPLOYEE_INFO_DEPARTMENT);
		this.room = json.getString(ConstantKeyInJson.EMPLOYEE_INFO_ROOM);
		this.hospitaId = json.getString(ConstantKeyInJson.EMPLOYEE_INFO_HOSPITAL_ID);
		if(basicInfo == null) {
			User basic = new User();
			this.basicInfo = basic;
		}
		this.basicInfo.transJsonToJavaBean(json.getJSONObject(ConstantKeyInJson.EMPLOYEE_INFO_BASIC_INFO));
	}
	@Override
	public JSONObject toJsonObjectExceptNull() {
		JSONObject jsonObject = toJsonObjectExceptNullWithoutSuperClass();
		jsonObject.put(ConstantKeyInJson.BEAN_BASIC_INFO,super.toJsonObjectExceptNull());
		return jsonObject;
	}

	@Override
	public JSONObject toJsonObjectExceptNullWithoutSuperClass() {
		JSONObject jsonObject = new JSONObject();
		if(employeeId != null) jsonObject.put(ConstantKeyInJson.EMPLOYEE_INFO_EMPLOYEE_ID, employeeId);
		if(department != null) jsonObject.put(ConstantKeyInJson.EMPLOYEE_INFO_DEPARTMENT, department);
		if(room != null) jsonObject.put(ConstantKeyInJson.EMPLOYEE_INFO_ROOM, room);
		if(hospitaId != null) jsonObject.put(ConstantKeyInJson.EMPLOYEE_INFO_HOSPITAL_ID, hospitaId);
		if(basicInfo != null) jsonObject.put(ConstantKeyInJson.EMPLOYEE_INFO_BASIC_INFO, basicInfo.toJsonObjectExceptNull());
		return jsonObject;
	}
	public User getBasicInfo() {
		return basicInfo;
	}
	public void setBasicInfo(User basicInfo) {
		this.basicInfo = basicInfo;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getHospitaId() {
		return hospitaId;
	}
	public void setHospitaId(String hospitaId) {
		this.hospitaId = hospitaId;
	}

}
