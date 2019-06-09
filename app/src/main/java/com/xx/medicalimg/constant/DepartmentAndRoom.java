package com.xx.medicalimg.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 科室列表
 */
public class DepartmentAndRoom {

	public static final int DEPARTMENT_INTERNAL = 0;//内
	public static final int DEPARTMENT_SURGERY = 1;//外
	public static final int DEPARTMENT_GYNAECOLOGY = 2;//妇产科
	public static final int DEPARTMENT_PEDIATRICS = 3;//儿科
	public static final int DEPARTMENT_OTHER = 4;

	public static final String STRING_INTERNAL = "内科";
	public static final String STRING_SURGERY = "外科";
	public static final String STRING_GYNAECOLOGY = "妇产科";
	public static final String STRING_PEDIATRICS = "儿科";
	public static final String STRING_OTHER = "其他";

	public static final HashMap<Integer,String> departmentMap;
	static {
		departmentMap = new HashMap();
		departmentMap.put(0,STRING_INTERNAL);
		departmentMap.put(1,STRING_SURGERY);
		departmentMap.put(2,STRING_GYNAECOLOGY);
		departmentMap.put(3,STRING_PEDIATRICS);
		departmentMap.put(4,STRING_OTHER);
	}
}
