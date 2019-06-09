package com.xx.medicalimg.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * 主界面第一个Fragment,显示病例
 */
public class AidCaseFragment extends Fragment {
	public final static String TAG = "NotificationFragment";
	private View view;
	private RecyclerView recyclerView;
	private SwipeRefreshLayout srl_main;
	private AbstractEndlessRecyclerOnScrollListener onScrollListener;
	private List<AidCase> aidCaseList;
	private AidCaseAdapter aidCaseAdapter;
	public static AidCaseFragment newInstance(){
		AidCaseFragment f = new AidCaseFragment();
		return f;

	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main,container,false);
		recyclerView = view.findViewById(R.id.rv_main);
		recyclerView.setAdapter(aidCaseAdapter);
		recyclerView.addOnScrollListener(onScrollListener);
		srl_main = view.findViewById(R.id.srl_main);
		srl_main.setOnRefreshListener(() -> resetAdapter());
		return view;
	}


	static class AidCaseTask extends HttpAsyncTask<AidCaseFragment> {
		private List<AidCase> listAidCase;
		//初始化view并在获取网络返回值成功时加载对应view
		public AidCaseTask(AidCaseFragment context, List<AidCase> listAidCase, int start,int end) {
			super(context, Urls.ASK_AID_CASE, null);
			this.listAidCase = listAidCase;
			JSONObject test;
			test = new JSONObject();
			if(MApplication.getUserManager().isLogin()){
				test.put(ConstantKeyInAskJson.AID_CASE_REQUEST_PERSON_ID, MApplication.getUserManager().getUser().getId());
			}else{
				test.put(ConstantKeyInAskJson.AID_CASE_REQUEST_PERSON_ID, "-1");
			}
			test.put(ConstantKeyInAskJson.AID_CASE_START,start);
			test.put(ConstantKeyInAskJson.AID_CASE_END,end);
			test.put(ConstantKeyInAskJson.AID_CASE_REQUEST_TYPE,0);//0,随机
			setRequestBody(test.toJSONString());
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
				new EmployeeCommonInfoTask(getWeakTarget(),aidCase.getAidRelease()).execute();
			}
			if(getWeakTarget() != null){
				getWeakTarget().aidCaseAdapter.notifyDataSetChanged();
			}
			setLoadStatus(AidCaseAdapter.LOADING_COMPLETE);
		}

		@Override
		protected void onPreLoad() {
			setLoadStatus(AidReleaseAdapter.LOADING);
		}
		private void setLoadStatus(int status){
			if(getWeakTarget() != null){
				getWeakTarget().aidCaseAdapter.setFootLoadState(status);
			}
		}
	}
	static class EmployeeCommonInfoTask extends HttpAsyncTask<AidCaseFragment> {
		AidRelease aidRelease;
		public EmployeeCommonInfoTask(AidCaseFragment context,AidRelease aidRelease) {
			super(context, Urls.ASK_USER_INFO, null);
			this.aidRelease = aidRelease;
			JSONObject test;
			test = new JSONObject();
			test.put(ConstantKeyInAskJson.EMPLOYEE_OR_USER,0);
			test.put(ConstantKeyInAskJson.ID,aidRelease.getUploadEmployeeId());
			test.put(ConstantKeyInAskJson.REQUEST_TYPE,1);
			setRequestBody(test.toJSONString());
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			getWeakTarget().srl_main.setRefreshing(false);
			LogUtils.e(TAG,"onFail" + httpResponseParam.getStatusCode() + httpResponseParam.getResponseString());
			LogUtils.e(TAG,"错误代码：" + httpResponseParam.getStatusCode());
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {

			JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
			Employee employee = new Employee();
			aidRelease.setUpLoadEmployee(employee);
			employee.transJsonToJavaBean(jsonObject.getJSONObject(ConstantKeyInJson.NET_USER_INFO));
			if(getWeakTarget() != null){
				getWeakTarget().aidCaseAdapter.notifyDataSetChanged();
				getWeakTarget().srl_main.setRefreshing(false);
			}

		}

		@Override
		protected void onPreLoad() {

		}

	}
	private void init() {
		onScrollListener = new AbstractEndlessRecyclerOnScrollListener(getActivity()) {
			@Override
			public void onLoadMore() {
				if(aidCaseAdapter.getFootLoadState() != AidReleaseAdapter.LOADING_END){
					new AidCaseTask(AidCaseFragment.this,aidCaseList,aidCaseList.size(),aidCaseList.size()+5).execute();
					}
			}
		};
		resetAdapter();
	}

	private void resetAdapter() {
		aidCaseList = new ArrayList<>();
		aidCaseAdapter = new AidCaseAdapter(aidCaseList);
		new AidCaseTask(AidCaseFragment.this,aidCaseList,0,5).execute();
	}

}
