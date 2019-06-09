package com.xx.medicalimg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xx.medicalimg.MApplication;
import com.xx.medicalimg.R;
import com.xx.medicalimg.adapter.AidTreatReplayAdapter;
import com.xx.medicalimg.adapter.ImgDisplayAdapter;
import com.xx.medicalimg.bean.AidRelease;
import com.xx.medicalimg.bean.AidTreat;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.constant.Urls;
import com.xx.medicalimg.net.HttpAsyncTask;
import com.xx.medicalimg.net.HttpResponseParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 查看病单详情，并进行回复
 * .<br/>
 */
public class AidTreatActivity extends AppCompatActivity implements View.OnClickListener{
	private AidRelease aidRelease;
	private Employee employee;
	private AidTreat aidTreat;
	private Employee user;
	private CircleImageView civ_head;
	private TextView tv_nickName,tv_time,tv_state,tv_department,tv_content;
	private RecyclerView rv_display,rv_replay;
	private Button b_replay;
	private AidTreatReplayAdapter aIdTreatReplayAdapter;
	private final static String key = "aidRelease";
	private final List<String> aidReleaseState = Arrays.asList("不急","较急","急","很急","非常急");
	public static void actionStart(Context c, AidRelease aidRelease){
		Intent intent = new Intent(c,AidTreatActivity.class);
		intent.putExtra(key,aidRelease);
		c.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aid_treat);
		initID();
		initData();
		initView();
	}

	private void initData() {
		aidRelease = (AidRelease) getIntent().getSerializableExtra(key);
		aidTreat = new AidTreat();
		aidTreat.setAidReleaseId(aidRelease.getId());
		aidTreat.setHandlerId(MApplication.getUserManager().getUser().getId());
	}

	private void initView() {
		hideActionBar();
		aIdTreatReplayAdapter = new AidTreatReplayAdapter();
		aIdTreatReplayAdapter.setOnTitleLongClickListener((v, i) -> Toast.makeText(AidTreatActivity.this,
				"long",Toast.LENGTH_SHORT).show());

		tv_time.setText(aidRelease.getGreateTime());
		ImgDisplayAdapter imgDisplayAdapter = new ImgDisplayAdapter(aidRelease.getAidRecord().getId(),aidRelease.getAidRecord().getImgList());
		rv_display.setAdapter(imgDisplayAdapter);
		rv_replay.setAdapter(aIdTreatReplayAdapter);
		tv_state.setText(aidReleaseState.get(aidRelease.getState()));
		tv_content.setText(getContentText(aidRelease));
		employee = aidRelease.getUpLoadEmployee();
		if(employee != null){
			tv_nickName.setText(employee.getBasicInfo().getNickname());
			tv_department.setText(employee.getDepartment()+" "+employee.getRoom());
			RequestOptions options = new RequestOptions()
					.error(R.drawable.load_fail64)
					.fitCenter()
					.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图像
			Glide.with(this)
					.applyDefaultRequestOptions(options)
					.load(Urls.getHeadImgUrl(employee.getId(),
							employee.getBasicInfo().getHeadImg()))
					.into(civ_head);
		}
		b_replay.setOnClickListener(this);
	}

	/**
	 * 隐藏状态栏
	 */
	private void hideActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
	}

	private void initID() {
		civ_head = findViewById(R.id.civ_headImg);
		tv_content =  findViewById(R.id.tv_content);
		tv_nickName =  findViewById(R.id.tv_nickname);
		tv_time =  findViewById(R.id.tv_time);
		tv_state =  findViewById(R.id.tv_state);
		tv_department =  findViewById(R.id.tv_department);
		rv_display = findViewById(R.id.rv_imgs);
		rv_replay = findViewById(R.id.rv_reply);
		b_replay = findViewById(R.id.b_replay);
	}

	private String getContentText(AidRelease aidRelease){
		StringBuilder sb = new StringBuilder();
		sb.append("性别：")
				.append(aidRelease.getAidRecord().getSex() == 0 ? "男":"女")
				.append("  年龄：")
				.append(aidRelease.getAidRecord().getAge())
				.append("\n【主诉】")
				.append(aidRelease.getAidRecord().getChiefComplaint())
				.append("\n【临床诊断】")
				.append(aidRelease.getAidRecord().getClinicalManifestation())
				.append("\n【影像表现】")
				.append(aidRelease.getAidRecord().getImagingFeatures());
		return sb.toString();

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id){
			case R.id.b_replay:
				attemptToReplay();
				break;

			default:break;
		}
	}

	private void attemptToReplay() {
		boolean isInsertValid = false;
		Map<String,String> map = aIdTreatReplayAdapter.getMap();
		for(String key:aIdTreatReplayAdapter.getKeyList()) {
			if(map.containsKey(key) && map.get(key)!= null && map.get(key).length() != 0) {
				//先判断输入是否合法
				if (key.equals(AidTreatReplayAdapter.AID_TREAT_AID_SUMMARY))
					aidTreat.setAidSummary(map.get(key));
				if (key.equals(AidTreatReplayAdapter.AID_TREAT_ANALYSIS))
					aidTreat.setAnalysis(map.get(key));
				if (key.equals(AidTreatReplayAdapter.AID_TREAT_CLINICAL_DIAGNOSIS))
					aidTreat.setClinicalDiagnosis(map.get(key));
				isInsertValid = true;
		}
		}
		if (isInsertValid){
			new UploadAidTreatTask(this,aidTreat.toJsonObjectExceptNull().toJSONString()).execute();
		}else{
			toast("请输入有效回复");
		}
	}

	private void toast(String str){
		Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
	}

	/**
	 * 异步请求，上传病单处理
	 */
	static class UploadAidTreatTask extends HttpAsyncTask<AidTreatActivity>{

		public UploadAidTreatTask(AidTreatActivity context, String requestBody) {
			super(context,Urls.UPLOAD_AID_TREAT, requestBody);
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			Toast.makeText(getWeakTarget(),httpResponseParam.toString(),Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {
			Toast.makeText(getWeakTarget(),"回复成功",Toast.LENGTH_SHORT).show();
		}

		@Override
		protected void onPreLoad() {

		}

	}

}
