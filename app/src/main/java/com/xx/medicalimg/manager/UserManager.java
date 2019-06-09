package com.xx.medicalimg.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.MApplication;
import com.xx.medicalimg.abs.other.AbstractUserManager;
import com.xx.medicalimg.bean.BasicInfo;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.bean.User;
import com.xx.medicalimg.util.LogUtils;

/**
 * 本地用户信息管理者
 */
public class UserManager extends AbstractUserManager {
	private final static String TAG = "UserManager";

	private Context context;
	private boolean isLogin = false;//优先初始化
	private String userType;
	private BasicInfo user;
	private SharedPreferences spStatus,spUserInfo;

	public static final String USER_TYPE_NORMAL = "normal";
	public static final String USER_TYPE_EMPLOYEE = "employee";

	private static final String SP_STATUS = "statusSp";
	private static final String SP_USER_INFO = "userInfo";
	private static final String KEY_IS_LOGIN = "isLogin";
	private static final String KEY_USER_TYPE = "userType";
	private static final String KEY_JSON_USER_INFO = "jsonUserInfo";//转换成json存储

	public UserManager(Context context){
		this.context = context;
		initStatus();
		initUserInfo();
	}
	public boolean isLogin() {
		return isLogin;
	}
	public void setLogin(boolean isLogin){
		this.isLogin = isLogin;
	}

	@Override
	public void setUser(BasicInfo user) {
		setLogin(true);
		this.user = user;
		if (user instanceof User){
			userType = USER_TYPE_NORMAL;
		}else if(user instanceof Employee){
			userType = USER_TYPE_EMPLOYEE;
		}
	}

	public BasicInfo getUser (){
		if(isLogin) return user;
		else{
			LogUtils.e(TAG,"尚未登陆");
			return null;
		}
	}

	@Override
	public String getUserType() {
		return userType;
	}

	@Override
	public void onDepose() {
		saveStatus();
		if(isLogin){
			saveUserInfo();
		}
	}
	@Override
	protected void restoreUserInfo() {
		String jsonUserInfo = spUserInfo.getString(KEY_JSON_USER_INFO,null);
		if(isLogin && jsonUserInfo != null){
			switch (userType){
				case USER_TYPE_NORMAL:
					User user = new User();
					user.transJsonToJavaBean(JSONObject.parseObject(jsonUserInfo));
					this.user = user;
					break;
				case USER_TYPE_EMPLOYEE:
					Employee employee = new Employee();
					employee.transJsonToJavaBean(JSONObject.parseObject(jsonUserInfo));
					this.user = employee;
					break;
				default:
					break;
			}
		}
	}

	@Override
	protected void saveUserInfo() {
		if (isLogin && user != null){
			SharedPreferences.Editor editor  = spUserInfo.edit();
			editor.putString(KEY_JSON_USER_INFO,user.toJsonObjectExceptNull().toJSONString());
			editor.apply();
		}
	}
	/**
	 * 初始化状态信息
	 */
	private void initStatus() {
		spStatus = context.getSharedPreferences(SP_STATUS,Context.MODE_PRIVATE);
		spUserInfo = context.getSharedPreferences(SP_USER_INFO,Context.MODE_PRIVATE);
		isLogin = spStatus.getBoolean(KEY_IS_LOGIN,false);
		userType = spStatus.getString(KEY_USER_TYPE,USER_TYPE_NORMAL);
	}

	/**
	 * 初始化用户信息
	 */
	private void initUserInfo() {
		restoreUserInfo();
	}

	/**
	 * 保存状态信息
	 */
	private void saveStatus() {
		SharedPreferences.Editor editor  = spStatus.edit();
		editor.putBoolean(KEY_IS_LOGIN,isLogin);
		editor.putString(KEY_USER_TYPE,userType);
		editor.apply();
	}

	/**
	 * 根据参数函数索引设置对应字段值
	 * @param isNormal 基本信息与否
	 * @param index 序号
	 * @param value 值
	 */
	public void setInfoByIndex(boolean isNormal,int index, String value){
		User user = null;
		Employee employee = null;
		if (isNormal){//只修改基本信息
			if (UserManager.USER_TYPE_NORMAL.equals(getUserType())){
				user = (User)getUser();
			}else{
				Employee e = (Employee) getUser();
				user = e.getBasicInfo();
			}
		}else{//只修改其他信息
			if (UserManager.USER_TYPE_EMPLOYEE.equals(getUserType())){
				employee = (Employee) getUser();
			}
		}
		if (isNormal){//姓名 昵称 电话 邮箱 性别 生日
			if(user == null) {
				LogUtils.e(TAG,"设置失败 user == null");
				return;
			}
			switch (index){
				case 0:
					 user.setName(value);
					 break;
				case 1:
					user.setNickname(value);
					break;
				case 2:
					user.setPhone(value);
					break;
				case 3:
					user.setEmail(value);
					break;
				case 4:
					user.setSex("男".equals(value) ? 0:1);
					break;
				case 5:
					user.setBirthday(value);
					break;
			}
		}else{ //工号 医院编号 科 室
			if (employee == null){
				LogUtils.e(TAG,"设置失败 employee == null");
				return;
			}
			switch (index){
				case 0:
					 employee.setEmployeeId(value);
					break;
				case 1:
					employee.setHospitaId(value);
					break;
				case 2:
					employee.setDepartment(value);
					break;
				case 3:
					employee.setRoom(value);
					break;
			}
		}
	}

	public User getBasicUser(){
		if(USER_TYPE_EMPLOYEE.equals(userType)){
			return ((Employee)user).getBasicInfo();
		}else{
			return (User)user;
		}
	}
	public Employee getEmployee(){
		if(USER_TYPE_EMPLOYEE.equals(userType)){
			return (Employee) user;
		}else{
			return null;
		}
	}
}
