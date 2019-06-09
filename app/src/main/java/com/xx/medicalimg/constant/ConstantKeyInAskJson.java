package com.xx.medicalimg.constant;
/**
 * 在获取请求中的json的key
 * @author 2413324637
 *
 */
public class ConstantKeyInAskJson {
	public static final String AID_RELEASE_REQUEST_PERSON_TYPE = "requestPersonType";//0 id为上传者id，1 id为发布者id
	public static final String AID_RELEASE_TYPE = "type";//0为未处理病单，1为已处理病单
	public static final String AID_RELEASE_HANDLE_EMPLOYEE_ID = "employeeID";
	public static final String AID_RELEASE_START = "start";
	public static final String AID_RELEASE_END = "end";
	//获取用户信息
	public static final String EMPLOYEE_OR_USER = "employeeOrUser";//0employee信息，1user信息
	public static final String ID = "id";
	public static final String PHONE = "phone";
	public static final String PASSWORD = "password";
	public static final String REQUEST_TYPE = "requestType";//全部信息0,常用可见信息1,2请求登陆

	//病例
	public static final String AID_CASE_REQUEST_PERSON_ID = "requestPersonId";
	public static final String AID_CASE_REQUEST_TYPE = "requestType";//0随机获取
	public static final String AID_CASE_START = "start";
	public static final String AID_CASE_END = "end";

	//病单处理
	public static final String AID_TREAT_TYPE = "requestType";//
	public static final String AID_TREAT_AID_RELEASE_ID = "aidReleaseID";
	public static final String AID_TREAT_HANDLER_ID = "handlerID";

	//
	public static final String LOIN_REQUEST_TYPE = "requestType";//0登陆，1注册
	public static final String LOGIN_PHONE = "phone";
	public static final String LOGIN_PASSWORD = "password";

	//用户信息
	/**
	 * 0 user,1employee
	 */
	public static final String USER_SETTING_PERSON_TYPE = "personType";//0 user,1employee
	public static final String USER_SETTING_TYPE = "type";//指定序号
	public static final String USER_SETTING_VALUE = "value";
	public static final String USER_SETTING_ID = "id";
	public static final String USER_SETTING_INFO_TYPE = "infoType";

}
