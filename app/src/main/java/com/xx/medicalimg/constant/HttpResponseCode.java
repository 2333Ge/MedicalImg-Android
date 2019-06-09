package com.xx.medicalimg.constant;
/**
 * 网络请求状态码
 */
public class HttpResponseCode {
	public final static int FAIL = 0;
	public final static int SUCCESS = 1;
	public final static int NULL_RESPONSE_BODY = 2;
	//建立连接后服务器返回的状态状态码
	public final static int ERROR = 3;
	public final static int NETWORK_EXCEPTION = 4;//针对客户端方
	public final static int UNKNOW = 5;
	public final static int REQUEST_EXCEPTION = 6;
	public final static int SERVER_EXCEPTION = 7;
	public final static int NULL_RESULT = 8;
	public final static int ERROR_PASSWORD = 9;
	public final static int ERROR_DATABASE = 10;

}
