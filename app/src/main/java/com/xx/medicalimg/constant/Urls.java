package com.xx.medicalimg.constant;

import com.xx.medicalimg.util.TextUtils;

import java.io.File;

/**
 * 网络请求路径
 */
public class Urls {
	/**
	 * 请求病单
	 */
	public static final String DOWNLOAD_ROOT_URL = "http://10.108.217.211:8080/";
	public static final String SERVICE_URL = "http://10.108.217.211:8080/dcmweb1/";
	public static final String ASK_AID_RELEASE = SERVICE_URL + "askAidReleases";
	public static final String ASK_USER_INFO = SERVICE_URL + "askUserInfo";
	public static final String UPLOAD_AID_TREAT = SERVICE_URL + "uploadAidTreat";
	public static final String ASK_AID_CASE = SERVICE_URL + "askAidCase";
	public static final String ASK_AID_TREAT = SERVICE_URL + "askAidTreat";
	public static final String LOGIN = SERVICE_URL + "login";
	public static final String USER_SETTING = SERVICE_URL + "userSetting";
	public static final String DOWNLOAD_URL = DOWNLOAD_ROOT_URL + "upload/";

	/**
	 * 文件存储路径规范
	 * rootUploadPath/文件类型（如：jpg/dcm） /表名(如：AidRecord)/id/文件名(包括拓展名)
	 * 存在dcm图像，默认已解析，对应jpg路径下存在解析后的jpg图像
	 * @param id
	 * @param fileName
	 * @return
	 */
	public static String getFileUrl(String id,String table,String fileName){
		String fileExtName = TextUtils.getFileExtName(fileName);
		return DOWNLOAD_URL +
				fileExtName +
				"/" +
				table +
				"/" +
				id +
				"/"+
				fileName;
	}

	/**
	 * 同上，得到同名不同类型的文件
	 * @param id
	 * @param table
	 * @param exitName
	 * @param fileName
	 * @return
	 */
	public static String getFileUrl(String id,String table,String exitName,String fileName){
		return DOWNLOAD_URL +
				exitName +
				"/" +
				table +
				"/" +
				id +
				"/"+
				TextUtils.getFileNameWithoutDot(fileName)+
				"."+
				exitName;
	}
	public static String getAidRecordJpgUrl(String id,String fileName){
		return getFileUrl(id,"aid_record","jpg",fileName);
	}
	public static String getHeadImgUrl(String id,String fileName){
		return getFileUrl(id,"user_info","jpg",fileName);
	}
}
