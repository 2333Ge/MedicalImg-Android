package com.xx.medicalimg.constant;
/**
 * json串中的key,和数据库属性名类似
 *
 */
public class ConstantKeyInJson {

	//实体类名
	public static final String BEAN_BASIC_INFO = "basic";
	public final static String BEAN_AID_CASE = "aid_case";
	public final static String BEAN_AID_RECORD = "aid_record";
	public final static String BEAN_AID_RELEASE = "aid_release";
	public final static String BEAN_AID_TREAT = "aid_treat";
	public final static String BEAN_EMPLOYEE_INFO = "employee_info";
	public final static String BEAN_USER_INFO = "user_info";

	//公有属性
	public final static String ID = "id";
	public final static String GMT_CREATE = "gmt_create";
	public final static String GMT_MODIFIED = "gmt_modified";

	//各表私有属性
	//普通用户信息
	public final static String USER_INFO_NAME = "name";
	public final static String USER_INFO_NICKNAME = "nickname";
	public final static String USER_INFO_HEAD_IMG = "head_img";
	public final static String USER_INFO_PHONE = "phone";
	public final static String USER_INFO_EMAIL = "email";
	public final static String USER_INFO_PASSWORD = "password";
	public final static String USER_INFO_SEX = "sex";//0男1女
	public final static String USER_INFO_BIRTHDAY = "birthday";
	//医务人员信息
	public final static String EMPLOYEE_INFO_EMPLOYEE_ID = "employee_id";//工号
	public final static String EMPLOYEE_INFO_BASIC_INFO = "employee_user_info";
	public final static String EMPLOYEE_INFO_DEPARTMENT = "department";// 科
	public final static String EMPLOYEE_INFO_ROOM = "room";//室
	public final static String EMPLOYEE_INFO_HOSPITAL_ID = "hospital_id";

	// 病例
	public final static String AID_CASE_AID_RECORD_ID = "aid_record_id";
	public final static String AID_CASE_AID_RELEASE_ID = "aid_release_id";
	public final static String AID_CASE_AID_TREAT_ID = "aid_treat_id";
	public final static String AID_CASE_AID_RECORD = "aid_case_aid_record";
	public final static String AID_CASE_AID_RELEASE = "aid_case_aid_release";
	public final static String AID_CASE_AID_TREAT = "aid_case_aid_treat";
	//病单（医院）
	public final static String AID_RECORD_SEX = "sex";
	public final static String AID_RECORD_AGE = "age";
	public final static String AID_RECORD_IMG_LIST = "img_list";
	public final static String AID_RECORD_CHIEF_COMPLAINT = "chief_complaint";//主诉
	public final static String AID_RECORD_CLINICAL_MANIFESTATION = "clinical_manifestation";//临床表现
	public final static String AID_RECORD_IMAGEING_FEATURE = "imageing_feature";//影像表现
	//病单（发布的，给指定医生）
	public final static String AID_RELEASE_HANDLE_EMPLOYEE_ID = "handle_employee_id";
	public final static String AID_RELEASE_STATE = "state";
	public final static String AID_RELEASE_AID_RECORD_ID = "aid_record_id";
	public final static String AID_RELEASE_UPLOAD_EMPLOYEE_ID = "upload_employee_id";
	public final static String AID_RELEASE_AID_RECORD = "aid_record";
	//病单处理
	public final static String AID_TREAT_AID_RELEASE_ID = "aid_release_id";
	public final static String AID_TREAT_CLINICAL_DIAGNOSIS = "clinical_diagnosis";//临床诊断
	public final static String AID_TREAT_ANALYSIS = "analysis";//本例分析
	public final static String AID_TREAT_AID_SUMMARY = "aid_summary";//病例小结
	public final static String AID_TREAT_HANDLER_ID = "handler_id";//处理人id
	public final static String AID_TREAT_REVIEWER_ID = "reviewer_id";//复审人id

	//////以上对应数据库部分
	public final static String RESPONSE_CODE = "resopnseCode";

	//以下为非数据库部分关键字
	public final static String NET_AID_RELEASE_ARRAY = "aid_releases";
	public final static String NET_USER_INFO = "user_info";
	public final static String NET_NICKNAME = "nickName";
	public final static String NET_AID_CASE_ARRAY = "aid_cases";


	public final static String NET_AID_TREAT = "aidTreat";


	public static final String LOGIN_RESPONSE_USER_TYPE = "userType";//0普通，1医务人员
	public static final String LOGIN_RESPONSE_USER = "user";

	public static final String USER_SETTING_RESPONSE_USER = "user";
	//创建user改employee时返回全部信息

}
