package com.xx.medicalimg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.MApplication;
import com.xx.medicalimg.R;
import com.xx.medicalimg.abs.view.AbstractEndlessRecyclerOnScrollListener;
import com.xx.medicalimg.adapter.AidCaseAdapter;
import com.xx.medicalimg.adapter.AidReleaseAdapter;
import com.xx.medicalimg.bean.AidCase;
import com.xx.medicalimg.bean.AidRelease;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.constant.ConstantKeyInAskJson;
import com.xx.medicalimg.constant.ConstantKeyInJson;
import com.xx.medicalimg.constant.HttpResponseCode;
import com.xx.medicalimg.constant.Urls;
import com.xx.medicalimg.net.HttpAsyncTask;
import com.xx.medicalimg.net.HttpResponseParam;
import com.xx.medicalimg.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看所有已经处理的病例
 */
public class HandledAidCaseActivity extends AppCompatActivity {
	public final static String TAG = "HandledAidCaseActivity";
	private View view;
	private RecyclerView recyclerView;
	private AbstractEndlessRecyclerOnScrollListener onScrollListener;
	private List<AidCase> aidCaseList;
	private AidCaseAdapter aidCaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_aid_case);
		aidCaseList = new ArrayList<>();
		aidCaseAdapter = new AidCaseAdapter(aidCaseList,true);
		new AidCaseTask(aidCaseAdapter,aidCaseList,0,5,"1").execute();
		onScrollListener = new AbstractEndlessRecyclerOnScrollListener(this) {
			@Override
			public void onLoadMore() {
				if(aidCaseAdapter.getFootLoadState() != AidReleaseAdapter.LOADING_END){
					new AidCaseTask(aidCaseAdapter,aidCaseList,aidCaseList.size(),aidCaseList.size()+5,"1").execute();
					Toast.makeText(HandledAidCaseActivity.this, ""+aidCaseList.size(), Toast.LENGTH_SHORT).show();
				}
			}
		};
		recyclerView = findViewById(R.id.rv_main);
		recyclerView.setAdapter(aidCaseAdapter);
		recyclerView.addOnScrollListener(onScrollListener);
	}
	/**
	 * 异步网络请求获取AidCase类
	 */
	static class AidCaseTask extends HttpAsyncTask<AidCaseAdapter> {
		private List<AidCase> listAidCase;
		//初始化view并在获取网络返回值成功时加载对应view
		public AidCaseTask(AidCaseAdapter context, List<AidCase> listAidCase, int start,int end,String employeeId) {
			this(context, Urls.ASK_AID_CASE, null);
			this.listAidCase = listAidCase;
			JSONObject test;
			test = new JSONObject();
			test.put(ConstantKeyInAskJson.AID_CASE_REQUEST_PERSON_ID, MApplication.getUserManager().getUser().getId());
			test.put(ConstantKeyInAskJson.AID_CASE_START,start);
			test.put(ConstantKeyInAskJson.AID_CASE_END,end);
			test.put(ConstantKeyInAskJson.AID_CASE_REQUEST_TYPE,1);//1，处理人员
			setRequestBody(test.toJSONString());
		}

		public AidCaseTask(AidCaseAdapter context, String url, String requestBody) {
			super(context, url, requestBody);
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			LogUtils.e(TAG,"onFail" + httpResponseParam.getStatusCode() + httpResponseParam.getResponseString());
			if(httpResponseParam.getStatusCode() == HttpResponseCode.NULL_RESULT){
				setLoadStatus(AidCaseAdapter.LOADING_END);
			}else{
				setLoadStatus(AidCaseAdapter.LOADING_ERROR);
			}
			LogUtils.e(TAG,"错误代码：" + httpResponseParam.getStatusCode());
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {
			//LogUtils.e(TAG,httpResponseParam.getResponseString());
			JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
			JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeyInJson.NET_AID_CASE_ARRAY);
			for(int i = 0; i<jsonArray.size(); i++){
				JSONObject jsonObject1 = jsonArray.getJSONObject(i);
				AidCase aidCase = new AidCase();
				aidCase.transJsonToJavaBean(jsonObject1);
				listAidCase.add(aidCase);
				Employee employee = new Employee();
				employee.setId(aidCase.getAidRelease().getUploadEmployeeId());
				new EmployeeCommonInfoTask(getWeakTarget(),aidCase.getAidRelease(),Urls.ASK_USER_INFO).execute();
			}
			if(getWeakTarget() != null){
				getWeakTarget().notifyDataSetChanged();
			}
			setLoadStatus(AidCaseAdapter.LOADING_COMPLETE);
		}

		@Override
		protected void onPreLoad() {
			setLoadStatus(AidReleaseAdapter.LOADING);
		}
		private void setLoadStatus(int status){
			if(getWeakTarget() != null){
				getWeakTarget().setFootLoadState(status);
			}
		}
	}

	/**
	 * 异步请求获取医务人员可见信息类
	 */
	static class EmployeeCommonInfoTask extends HttpAsyncTask<AidCaseAdapter> {
		AidRelease aidRelease;
		 EmployeeCommonInfoTask(AidCaseAdapter context,AidRelease aidRelease,String url) {
			this(context, url, null);
			this.aidRelease = aidRelease;
			JSONObject test;
			test = new JSONObject();
			test.put(ConstantKeyInAskJson.EMPLOYEE_OR_USER,0);
			test.put(ConstantKeyInAskJson.ID,aidRelease.getUploadEmployeeId());
			test.put(ConstantKeyInAskJson.REQUEST_TYPE,1);
			setRequestBody(test.toJSONString());
		}

		 EmployeeCommonInfoTask(AidCaseAdapter context, String url, String requestBody) {
			super(context, url, requestBody);
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			LogUtils.e(TAG,"onFail" + httpResponseParam.getStatusCode() + httpResponseParam.getResponseString());
			LogUtils.e(TAG,"错误代码：" + httpResponseParam.getStatusCode());
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {
			//LogUtils.e(TAG,httpResponseParam.getResponseString());
			JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
			Employee employee = new Employee();
			aidRelease.setUpLoadEmployee(employee);
			employee.transJsonToJavaBean(jsonObject.getJSONObject(ConstantKeyInJson.NET_USER_INFO));
			if(getWeakTarget() != null){
				getWeakTarget().notifyDataSetChanged();
			}

		}

		@Override
		protected void onPreLoad() {

		}

	}
}
