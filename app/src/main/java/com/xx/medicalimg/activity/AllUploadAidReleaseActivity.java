package com.xx.medicalimg.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.MApplication;
import com.xx.medicalimg.R;
import com.xx.medicalimg.abs.view.AbstractEndlessRecyclerOnScrollListener;
import com.xx.medicalimg.adapter.AidReleaseAdapter;
import com.xx.medicalimg.adapter.UploadAidReleaseAdapter;
import com.xx.medicalimg.bean.AidCase;
import com.xx.medicalimg.bean.AidRelease;
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
 * 查看已经处理的病单
 */
public class AllUploadAidReleaseActivity extends AppCompatActivity {
	public final static String TAG = "AllUploadAidReleaseActivity";
	private View view;
	private RecyclerView recyclerView;
	private AbstractEndlessRecyclerOnScrollListener onScrollListener;
	private List<AidRelease> aidReleasesList;
	private UploadAidReleaseAdapter aidReleaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_aid_case);
		aidReleasesList = new ArrayList<>();
		aidReleaseAdapter = new UploadAidReleaseAdapter(aidReleasesList);
		new AidReleaseTask(aidReleaseAdapter,aidReleasesList,0,5,"1").execute();
		onScrollListener = new AbstractEndlessRecyclerOnScrollListener(this) {
			@Override
			public void onLoadMore() {
				if(aidReleaseAdapter.getFootLoadState() != AidReleaseAdapter.LOADING_END){
					new AidReleaseTask(aidReleaseAdapter,aidReleasesList,aidReleasesList.size(),aidReleasesList.size()+5,"1").execute();
					Toast.makeText(AllUploadAidReleaseActivity.this, ""+aidReleasesList.size(), Toast.LENGTH_SHORT).show();
				}
			}
		};
		recyclerView = findViewById(R.id.rv_main);
		recyclerView.setAdapter(aidReleaseAdapter);
		recyclerView.addOnScrollListener(onScrollListener);
		aidReleaseAdapter.setOnItemClickListener((v, current) -> {
			AidRelease aidRelease = aidReleasesList.get(current);
			AidCase aidCase = new AidCase();
			aidCase.setAidRecord(aidRelease.getAidRecord());
			aidCase.setAidRelease(aidRelease);
			aidCase.setGreateTime(aidRelease.getGreateTime());
			aidCase.setModifiedTime(aidRelease.getModifiedTime());
			aidCase.setAidReleaseID(aidRelease.getId());
			aidCase.setAidRecordID(aidRelease.getAidRecord().getId());
			UploadAidReleaseActivity.actionStart(AllUploadAidReleaseActivity.this,aidCase);
		});
	}

	/**
	 * 异步网络请求获取AidRelease类
	 */
	static class AidReleaseTask extends HttpAsyncTask<UploadAidReleaseAdapter> {
		private List<AidRelease> listAidRelease;

		//初始化view并在获取网络返回值成功时加载对应view
		public AidReleaseTask(UploadAidReleaseAdapter context, List<AidRelease> listAidRelease, int start, int end, String employeeId) {
			this(context, Urls.ASK_AID_RELEASE, null);
			this.listAidRelease = listAidRelease;
			JSONObject test;
			test = new JSONObject();
			test.put(ConstantKeyInAskJson.AID_RELEASE_HANDLE_EMPLOYEE_ID, MApplication.getUserManager().getUser().getId());
			test.put(ConstantKeyInAskJson.AID_RELEASE_START, start);
			test.put(ConstantKeyInAskJson.AID_RELEASE_END, end);
			test.put(ConstantKeyInAskJson.AID_RELEASE_REQUEST_PERSON_TYPE, 1);//上传者
			test.put(ConstantKeyInAskJson.AID_RELEASE_TYPE, 2);//所有病单
			setRequestBody(test.toJSONString());
		}

		AidReleaseTask(UploadAidReleaseAdapter context, String url, String requestBody) {
			super(context, url, requestBody);
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			LogUtils.e(TAG, "onFail" + httpResponseParam.getStatusCode() + httpResponseParam.getResponseString());
			if (httpResponseParam.getStatusCode() == HttpResponseCode.NULL_RESULT) {
				setLoadStatus(AidReleaseAdapter.LOADING_END);
			} else {
				setLoadStatus(AidReleaseAdapter.LOADING_ERROR);
			}
			LogUtils.e(TAG, "错误代码：" + httpResponseParam.getStatusCode());
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {
			JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
			JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeyInJson.NET_AID_RELEASE_ARRAY);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject1 = jsonArray.getJSONObject(i);
				AidRelease aidRelease = new AidRelease();
				aidRelease.transJsonToJavaBean(jsonObject1);
				listAidRelease.add(aidRelease);
			}
			if (getWeakTarget() != null) {
				getWeakTarget().notifyDataSetChanged();
			}
			setLoadStatus(AidReleaseAdapter.LOADING_COMPLETE);
		}

		@Override
		protected void onPreLoad() {
			setLoadStatus(AidReleaseAdapter.LOADING);
		}

		private void setLoadStatus(int status) {
			if (getWeakTarget() != null) {
				getWeakTarget().setFootLoadState(status);
			}
		}
	}
}
