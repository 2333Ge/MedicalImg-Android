package com.xx.medicalimg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xx.medicalimg.R;
import com.xx.medicalimg.adapter.ImgDisplayAdapter;
import com.xx.medicalimg.bean.AidCase;
import com.xx.medicalimg.bean.AidRelease;
import com.xx.medicalimg.bean.AidTreat;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.constant.ConstantKeyInAskJson;
import com.xx.medicalimg.constant.ConstantKeyInJson;
import com.xx.medicalimg.constant.Urls;
import com.xx.medicalimg.net.HttpAsyncTask;
import com.xx.medicalimg.net.HttpResponseParam;
import com.xx.medicalimg.util.LogUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 查看发布的病单详细情况,AidRelease由上一个活动获取，其他由上一个activity获取
 */
public class UploadAidReleaseActivity extends AppCompatActivity {
	private AidCase aidCase;
	private static final String TAG = "UploadAidReleaseActivity";
	private CircleImageView civ_head;
	private TextView tv_nickName, tv_time, tv_state, tv_department, tv_content;
	private RecyclerView rv_display;

	private final static String key = "aidCase";

	/**
	 * 启动新的当前Activity
	 * @param c 上下文，
	 * @param aidCase 查看的病例
	 */
	public static void actionStart(Context c, AidCase aidCase){
		Intent intent = new Intent(c,UploadAidReleaseActivity.class);
		intent.putExtra(key,aidCase);
		c.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aid_release_item);
		initID();
		initData();
		initView();
	}

	private void initView() {
		notifyContent();
		tv_content.setMaxLines(300);
		AidRelease aidRelease = aidCase.getAidRelease();
		String greateTime = "发布时间: " + aidCase.getGreateTime().split(" ")[0];
		tv_time.setText(greateTime);
		if(aidCase.getAidTreat() != null){
			String modifiedTime = "处理时间: " + aidCase.getModifiedTime().split(" ")[0];
			tv_state.setText(modifiedTime);
		}else{
			tv_state.setText("未处理");
		}
		ImgDisplayAdapter imgDisplayAdapter = new ImgDisplayAdapter(aidCase.getAidRecord().getId(), aidCase.getAidRecord().getImgList());
		rv_display.setAdapter(imgDisplayAdapter);
		notifyUserInfo();
	}

	/**
	 *  更新病例显示，获取一些上个活动未传入的信息
	 */
	public void notifyContent() {
		if (aidCase.getAidTreat() == null){
			new AidTreatTask(this,aidCase).execute();
		}
		tv_content.setText(getContentText(aidCase));
	}

	/**
	 * 更新侧边栏用户信息显示
	 */
	public void notifyUserInfo() {
		Employee employee = aidCase.getAidRelease().getHandleEmployee();
		if (employee != null) {
			tv_department.setText(employee.getDepartment() + " " + employee.getRoom());
			if (employee.getBasicInfo() != null) {
				tv_nickName.setText(employee.getBasicInfo().getNickname());
				RequestOptions options = new RequestOptions()
						.error(R.drawable.load_fail64)
						.fitCenter()
						.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图像
				Glide.with(this)
						.applyDefaultRequestOptions(options)
						.load(Urls.getHeadImgUrl(employee.getEmployeeId(), employee.getBasicInfo().getHeadImg()))
						.into(civ_head);
			}
		}else{
			new HandledEmployeeCommonInfoTask(this,aidCase.getAidRelease()).execute();
		}
	}

	private void initData() {
		aidCase = (AidCase) getIntent().getSerializableExtra(key);
	}

	private void initID() {
		civ_head = findViewById(R.id.civ_headImg);
		tv_content = findViewById(R.id.tv_content);
		tv_nickName = findViewById(R.id.tv_nickname);
		tv_time = findViewById(R.id.tv_time);
		tv_state = findViewById(R.id.tv_state);
		tv_department = findViewById(R.id.tv_department);
		rv_display = findViewById(R.id.rv_imgs);
	}

	private String getContentText(AidCase aidCase) {
		StringBuilder sb = new StringBuilder();
		sb.append("性别：")
				.append(aidCase.getAidRecord().getSex() == 0 ? "男" : "女")
				.append("  年龄：")
				.append(aidCase.getAidRecord().getAge())
				.append("\n【主诉】")
				.append(aidCase.getAidRecord().getChiefComplaint())
				.append("\n【临床表现】")
				.append(aidCase.getAidRecord().getClinicalManifestation())
				.append("\n【影像表现】")
				.append(aidCase.getAidRecord().getImagingFeatures());
		if(aidCase.getAidTreat() != null){
			sb.append("\n【临床诊断】")
					.append(aidCase.getAidTreat().getClinicalDiagnosis())
					.append("\n【本例分析】")
					.append(aidCase.getAidTreat().getAnalysis())
					.append("\n【病例小结】")
					.append(aidCase.getAidTreat().getAidSummary());
		}
		return sb.toString();
	}

	/**
	 * 异步请求获取医务人员可见信息类
	 */
	static class HandledEmployeeCommonInfoTask extends HttpAsyncTask<UploadAidReleaseActivity> {
		AidRelease aidRelease;
		public HandledEmployeeCommonInfoTask(UploadAidReleaseActivity context,AidRelease aidRelease) {
			this(context, Urls.ASK_USER_INFO, null);
			this.aidRelease = aidRelease;
			JSONObject test;
			test = new JSONObject();
			test.put(ConstantKeyInAskJson.EMPLOYEE_OR_USER,0);
			test.put(ConstantKeyInAskJson.ID,aidRelease.getHandleEmployeeId());
			test.put(ConstantKeyInAskJson.REQUEST_TYPE,1);
			setRequestBody(test.toJSONString());
		}

		public HandledEmployeeCommonInfoTask(UploadAidReleaseActivity context, String url, String requestBody) {
			super(context, url, requestBody);
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			LogUtils.e(TAG,"onFail" + httpResponseParam.getStatusCode() + httpResponseParam.getResponseString());
			LogUtils.e(TAG,"错误代码：" + httpResponseParam.getStatusCode());
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {
			JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
			Employee employee = new Employee();
			aidRelease.setHandleEmployee(employee);
			employee.transJsonToJavaBean(jsonObject.getJSONObject(ConstantKeyInJson.NET_USER_INFO));
			getWeakTarget().notifyUserInfo();
		}
		@Override
		protected void onPreLoad() {
		}
	}

	/**
	 *异步请求任务，获取对应病单处理情况
	 */
	static class AidTreatTask extends HttpAsyncTask<UploadAidReleaseActivity> {
		AidCase aidCase;
		public AidTreatTask(UploadAidReleaseActivity context,AidCase aidCase) {
			this(context, Urls.ASK_AID_TREAT, null);
			this.aidCase = aidCase;
			JSONObject test;
			test = new JSONObject();
			test.put(ConstantKeyInAskJson.AID_TREAT_HANDLER_ID,aidCase.getAidRelease().getHandleEmployeeId());
			test.put(ConstantKeyInAskJson.AID_TREAT_AID_RELEASE_ID,aidCase.getAidRelease().getId());
			test.put(ConstantKeyInAskJson.AID_TREAT_TYPE,0);
			setRequestBody(test.toJSONString());
		}

		public AidTreatTask(UploadAidReleaseActivity context, String url, String requestBody) {
			super(context, url, requestBody);
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			LogUtils.e(TAG,"onFail" + httpResponseParam.getStatusCode() + httpResponseParam.getResponseString());
			LogUtils.e(TAG,"错误代码：" + httpResponseParam.getStatusCode());
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {
			LogUtils.e("",httpResponseParam.getResponseString());
			JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
			AidTreat aidTreat = new AidTreat();
			aidTreat.transJsonToJavaBean(jsonObject.getJSONObject(ConstantKeyInJson.NET_AID_TREAT));
			aidCase.setAidTreat(aidTreat);
			getWeakTarget().notifyContent();
		}
		@Override
		protected void onPreLoad() {
		}
	}
}
