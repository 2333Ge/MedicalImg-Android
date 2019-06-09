package com.xx.medicalimg.constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户个人设置列表
 */
public class UserSettings {

	public static List<String> normalList;
	static{
		normalList = new ArrayList<>();
		normalList.add("姓名");
		normalList.add("昵称");
		normalList.add("电话");
		normalList.add("邮箱");
		normalList.add("性别");
		normalList.add("生日");
	}
	public static List<String> employeeList;
	static {
		employeeList = new ArrayList<>();
		employeeList.add("工号");
		employeeList.add("医院编号");
		employeeList.add("科");
		employeeList.add("室");
	}
}
