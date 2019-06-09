package com.xx.medicalimg.net;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.constant.ConstantKeyInJson;
import com.xx.medicalimg.constant.HttpResponseCode;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *  网络请求操作抽象类
 * @param <WeakTarget> 需要更新的视图的弱应用
 */
public abstract class HttpAsyncTask<WeakTarget> extends
		AsyncTask<Void, Integer, HttpResponseParam> {
	private static final String TAG = "HttpAsyncTask";
	private String url;
	private String requestBody;
	private MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
	private WeakReference<WeakTarget> weakTarget;

	/**
	 *
	 * @param context 上下文
	 * @param url 地址
	 * @param requestBody 请求体
	 */
	public HttpAsyncTask(WeakTarget context, String url, String requestBody){
		this.url = url;
		this.requestBody = requestBody;
		this.weakTarget = new WeakReference<>(context);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		onPreLoad();
	}

	@Override
	/**
	 *  新线程，后台发送网络请求
	 */
	protected HttpResponseParam doInBackground(Void... voids) {
		HttpResponseParam httpResponseParam = new HttpResponseParam();
		OkHttpClient okHttpClient = new OkHttpClient();
		final Request request = new Request.Builder()
				.url(url)
				.post(RequestBody.create(mediaType, requestBody))
				.build();
		final Call call = okHttpClient.newCall(request);
		Response response = null;
		String responseString;
		try{
			response = call.execute();
			ResponseBody body = response.body();
			responseString = body == null ? null:body.string();
			httpResponseParam.setResponseString(responseString);
			if (body == null){
				httpResponseParam.setStatusCode(HttpResponseCode.NULL_RESPONSE_BODY);
			}else{
				if (JSONObject.isValidObject(responseString)){
					JSONObject jsonObject = JSONObject.parseObject(responseString);
					int status = jsonObject.getInteger(ConstantKeyInJson.RESPONSE_CODE);
					//服务器返回数据成功，解析json串中状态码
					if ( status == HttpResponseCode.SUCCESS){
						httpResponseParam.setStatusCode(HttpResponseCode.SUCCESS);
					}else{
						httpResponseParam.setStatusCode(status);
					}
				}else{
					httpResponseParam.setStatusCode(HttpResponseCode.ERROR);
				}

			}
			return httpResponseParam;
		}catch (IOException e){
			e.printStackTrace();
			Log.e(TAG,e.toString());
			httpResponseParam.setStatusCode(HttpResponseCode.FAIL);
			return httpResponseParam;
		}
	}

	@Override
	/**
	 * 主线程，请求完成时调用
	 */
	protected void onPostExecute(HttpResponseParam httpResponseParam) {
		super.onPostExecute(httpResponseParam);
		switch (httpResponseParam.getStatusCode()){
			case HttpResponseCode.SUCCESS:
				onDataReceive(httpResponseParam);
				break;
			default:
				onFail(httpResponseParam);
				break;
		}
	}

	/**
	 * 获取传递的弱引用
	 * @return 对应视图
	 */
	public WeakTarget getWeakTarget(){
		return weakTarget.get();
	}

	/**
	 * 设置请求体
	 * @param requestBody 通常时JSON串
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	/**
	 * 主线程回调，请求成功时调用，返回网络请求的返回状态和内容
	 * @param httpResponseParam 包含状态码和返回内容
	 */
	protected abstract void onDataReceive(HttpResponseParam httpResponseParam);
	/**
	 * 主线程回调，请求失败时调用，返回网络请求的返回状态和内容
	 * @param httpResponseParam 包含状态码和返回内容
	 */
	protected abstract void onFail(HttpResponseParam httpResponseParam);
	/**
	 * 主线程回调，发送网络请求前调用
	 */
	protected  abstract  void onPreLoad();
}
