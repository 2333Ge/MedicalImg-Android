package com.xx.medicalimg.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.MApplication;
import com.xx.medicalimg.R;
import com.xx.medicalimg.adapter.UserSettingAdapter;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.constant.ConstantKeyInAskJson;
import com.xx.medicalimg.constant.ConstantKeyInJson;
import com.xx.medicalimg.constant.Urls;
import com.xx.medicalimg.manager.UserManager;
import com.xx.medicalimg.net.HttpAsyncTask;
import com.xx.medicalimg.net.HttpResponseParam;
import com.xx.medicalimg.util.LogUtils;

import java.util.Calendar;

/**
 * 显示所有用户信息设置Activity
 */
public class UserSettingActivity extends AppCompatActivity {

	public static final int REQUEST_USER_SETTING = 1;

	private UserSettingAdapter normalAdapter,employeeAdapter;
	private RecyclerView rv_normal,rv_employee;
	private LinearLayout ll_employee;
	private Button b_quit;
	private boolean isNormal = true;

	public static void actionStart(Context context){

		Intent intent = new Intent(context,UserSettingActivity.class);
		context.startActivity(intent);

	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);
		initID();
		initView();
	}



	private void initView() {
		employeeAdapter = new UserSettingAdapter(false);
		normalAdapter = new UserSettingAdapter(true);
		rv_normal.setAdapter(normalAdapter);
		rv_employee.setAdapter(employeeAdapter);

		normalAdapter.setOnRightIconClickListener((v, i) -> {
			//姓名 昵称 电话 邮箱 性别 生日
			if(i == 5){
				displayDateDialog();
			}else if(i == 4){
				showSexChooseDialog();
			}else{
				startEdit(normalAdapter.getList().get(i),normalAdapter.getContent(i),i);
			}
			isNormal = true;
		});

		employeeAdapter.setOnRightIconClickListener((v, i) -> {
			//工号 医院编号 科 室
			startEdit(employeeAdapter.getList().get(i),employeeAdapter.getContent(i),i);
			isNormal = false;
		});
		b_quit.setOnClickListener(v -> {
			finish();
			MApplication.getUserManager().setLogin(false);
		});

	}

	private void initID() {
		rv_employee = findViewById(R.id.rv_employee);
		rv_normal = findViewById(R.id.rv_normal);
		ll_employee = findViewById(R.id.ll_employee);
		b_quit = findViewById(R.id.b_out);
	}

	/**
	 * 修异步请求改用户信息
	 */
	static class UserSettingTask extends HttpAsyncTask<UserSettingActivity>{
		int keyIndex;
		String value;
		boolean isNormal;
		public UserSettingTask(UserSettingActivity context,int keyIndex,String value,boolean isNormal){
			super(context, Urls.USER_SETTING, null);
			//settingType : 0 user,1 employee
			JSONObject requestJson = new JSONObject();
			requestJson.put(ConstantKeyInAskJson.USER_SETTING_PERSON_TYPE,
					UserManager.USER_TYPE_NORMAL.equals(MApplication.getUserManager().getUserType()));
			requestJson.put(ConstantKeyInAskJson.USER_SETTING_INFO_TYPE,
					isNormal);
			requestJson.put(ConstantKeyInAskJson.USER_SETTING_TYPE,keyIndex);
			requestJson.put(ConstantKeyInAskJson.USER_SETTING_ID,MApplication.getUserManager().getUser().getId());
			requestJson.put(ConstantKeyInAskJson.USER_SETTING_VALUE,value);
			setRequestBody(requestJson.toJSONString());
			this.isNormal = isNormal;
			this.keyIndex = keyIndex;
			this.value = value;
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {
			getWeakTarget().toast("修改信息成功");

			JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
			String responseUser = jsonObject.getString(ConstantKeyInJson.USER_SETTING_RESPONSE_USER);
			if (responseUser == null){
				MApplication.getUserManager().setInfoByIndex(isNormal,keyIndex,value);
			}else{//由user变为employee
				JSONObject jsonObject1 = JSONObject.parseObject(responseUser);
				Employee employee = new Employee();
				employee.transJsonToJavaBean(jsonObject1);
				MApplication.getUserManager().setUser(employee);
			}
			getWeakTarget().normalAdapter.notifyDataSetChanged();
			getWeakTarget().employeeAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			getWeakTarget().toast("修改信息失败");
		}

		@Override
		protected void onPreLoad() {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null){
			String value = data.getStringExtra(Activity_SettingEdit.KEY_VALUE_LAST);
			int index = data.getIntExtra(Activity_SettingEdit.KEY_POSITION,-1);
			new UserSettingTask(this,index,value,isNormal).execute();
		}

	}

	/**
	 * 新activity
	 * @param title
	 * @param valueLast
	 * @param position
	 */
	private void startEdit(String title,String valueLast,int position){
		Intent intent = new Intent(this,Activity_SettingEdit.class);
		intent.putExtra(Activity_SettingEdit.KEY_TITLE,title);
		intent.putExtra(Activity_SettingEdit.KEY_VALUE_LAST,valueLast);
		intent.putExtra(Activity_SettingEdit.KEY_POSITION,position);
		startActivityIfNeeded(intent,REQUEST_USER_SETTING);
	}

	/**
	 *性别选择
	 */
	private void showSexChooseDialog() {
		AlertDialog.Builder builder3 = new AlertDialog.Builder(this);// 自定义对话框
		builder3.setSingleChoiceItems(new String[]{"男","女"}, 0, new DialogInterface.OnClickListener() {// 2默认的选中

			@Override
			public void onClick(DialogInterface dialog, int which) {// which是被选中的位置
				String value = which +"";
				new UserSettingTask(UserSettingActivity.this,4,value,true).execute();
				dialog.dismiss();// 随便点击一个item消失对话框，不用点击确认取消
			}
		});
		builder3.show();// 让弹出框显示
	}
	/**
	 * 显示日期选择框
	 */
	private void displayDateDialog(){
		String birthday = MApplication.getUserManager().getBasicUser().getBirthday();
		int mYear,mMonth,mDay;
		if (birthday!= null){
			String[] t = birthday.split("-");
			mYear = Integer.parseInt(t[0]);
			mMonth = Integer.parseInt(t[1]);
			mDay = Integer.parseInt(t[2]);
		}else{
			Calendar ca = Calendar.getInstance();
			mYear = ca.get(Calendar.YEAR);
			mMonth = ca.get(Calendar.MONTH);
			mDay = ca.get(Calendar.DAY_OF_MONTH);
		}

		new DatePickerDialog(this,onDateSetListener, mYear, mMonth, mDay).show();
	}
	/**
	 * 日期选择器对话框监听
	 */
	private DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
		String birthday = year + "-" + monthOfYear + "-" + dayOfMonth;
		new UserSettingTask(UserSettingActivity.this,5,birthday,true).execute();
	};

	private void toast(String str){
		Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MApplication.getUserManager().onDepose();
	}
}
