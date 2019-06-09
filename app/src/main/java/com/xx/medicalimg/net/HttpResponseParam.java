package com.xx.medicalimg.net;


//HTTP应答返回参数

import com.xx.medicalimg.constant.HttpResponseCode;

/**
 * 网络请求操作完成后传递的实体，包括状态码和返回内容
 */
public class HttpResponseParam {
	private int statusCode = HttpResponseCode.FAIL;	//状态码
	private String responseString ;		//响应的实体JSON字符串

	public HttpResponseParam(int statusCode, String responseString) {
		this.statusCode = statusCode;
		this.responseString = responseString;
	}
	public HttpResponseParam(){}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResponseString() {
		return responseString;
	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	@Override
	public String toString() {
		return "HttpResponseParam{" +
				"statusCode=" + statusCode +
				", responseString='" + responseString + '\'' +
				'}';
	}
}
