package com.xx.medicalimg.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;
import com.xx.medicalimg.MApplication;
import com.xx.medicalimg.R;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.bean.User;
import com.xx.medicalimg.constant.ConstantKeyInAskJson;
import com.xx.medicalimg.constant.ConstantKeyInJson;
import com.xx.medicalimg.constant.Urls;
import com.xx.medicalimg.manager.UserManager;
import com.xx.medicalimg.net.HttpAsyncTask;
import com.xx.medicalimg.net.HttpResponseParam;
import com.xx.medicalimg.util.LogUtils;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 * LoaderCallbacks<Cursor>,
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

	private AutoCompleteTextView mEmailView;
	private EditText mPasswordView;
//	private View mLoginFormView;
	private LoginTask loginTask;
	private RegisterTask registerTask;

	private UserManager userManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		userManager = MApplication.getUserManager();
		mEmailView = findViewById(R.id.email);
		mPasswordView = findViewById(R.id.password);

		Button mEmailSignInButton = (Button) findViewById(R.id.b_login);
		mEmailSignInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});
		Button registerBtn = (Button) findViewById(R.id.b_register);
		registerBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptRegister();
			}
		});
//		mLoginFormView = findViewById(R.id.login_form);
	}

	/**
	 * 登陆
	 */
	private void attemptLogin() {
		boolean isCancle = checkInput();
		if(!isCancle) {
			loginTask = new LoginTask(mEmailView.getText().toString(),mPasswordView.getText().toString(),this);
			loginTask.execute();
		}
	}

	/**
	 * 注册
	 */
	private void attemptRegister() {
		boolean isCancle = checkInput();
		if(!isCancle) {
			registerTask = new RegisterTask(mEmailView.getText().toString(),mPasswordView.getText().toString(),this);
			registerTask.execute();
		}
	}

	/**
	 * 检查输入
	 * @return true 输入正确
	 */
	private boolean checkInput(){
		mEmailView.setError(null);
		mPasswordView.setError(null);
		String phone  = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}
		if (TextUtils.isEmpty(phone)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!isPhoneValid(phone)) {
			mEmailView.setError(getString(R.string.error_invalid_phone));
			focusView = mEmailView;
			cancel = true;
		}
		if (cancel) {
			// 有错误，不能登陆
			focusView.requestFocus();
		}
		return cancel;
	}
	/**
	 * 验证手机号
	 * @param phone 手机号
	 * @return 手机号格式正确与否
	 */
	private boolean isPhoneValid(String phone) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");
		m = p.matcher(phone);
		b = m.matches();
		return b;
	}

	/**
	 * 验证密码
	 * @param password 密码
	 * @return 密码格式正确与否
	 */
	private boolean isPasswordValid(String password) {
		return password.length() > 4;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.b_login:
				attemptLogin();
				break;
			default:break;
		}
	}


	/**
	 * 异步登陆请求
	 */
	static class LoginTask extends HttpAsyncTask<LoginActivity>{
		public LoginTask(String phone,String password,LoginActivity context){
			super(context, Urls.LOGIN, null);
			JSONObject test;
			test = new JSONObject();
			test.put(ConstantKeyInAskJson.LOGIN_PHONE,phone);
			test.put(ConstantKeyInAskJson.LOGIN_PASSWORD,password);
			test.put(ConstantKeyInAskJson.LOIN_REQUEST_TYPE,0);
			setRequestBody(test.toJSONString());
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			toast(getWeakTarget(),"登陆失败");
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {
			toast(getWeakTarget(),"登陆成功");
			JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
			int type = jsonObject.getInteger(ConstantKeyInJson.LOGIN_RESPONSE_USER_TYPE);
			if(type == 0){
				User user = new User();
				user.transJsonToJavaBean(jsonObject.getJSONObject(ConstantKeyInJson.LOGIN_RESPONSE_USER));
				getWeakTarget().userManager.setUser(user);
			}else{
				Employee user = new Employee();
				user.transJsonToJavaBean(jsonObject.getJSONObject(ConstantKeyInJson.LOGIN_RESPONSE_USER));
				getWeakTarget().userManager.setUser(user);
			}
			getWeakTarget().finish();
		}

		@Override
		protected void onPreLoad() {

		}

	}

	/**
	 * 异步注册请求
	 */
	static class RegisterTask extends HttpAsyncTask<LoginActivity>{
		 RegisterTask(String phone,String password,LoginActivity context){
			super(context, Urls.LOGIN, null);
			JSONObject test;
			test = new JSONObject();
			test.put(ConstantKeyInAskJson.LOGIN_PHONE,phone);
			test.put(ConstantKeyInAskJson.LOGIN_PASSWORD,password);
			test.put(ConstantKeyInAskJson.LOIN_REQUEST_TYPE,1);
			setRequestBody(test.toJSONString());
		}

		@Override
		protected void onFail(HttpResponseParam httpResponseParam) {
			toast(getWeakTarget(),"注册失败");
		}

		@Override
		protected void onDataReceive(HttpResponseParam httpResponseParam) {
			JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
			User user = new User();
			user.transJsonToJavaBean(jsonObject.getJSONObject(ConstantKeyInJson.LOGIN_RESPONSE_USER));
			getWeakTarget().userManager.setUser(user);
			getWeakTarget().finish();
		}

		@Override
		protected void onPreLoad() {

		}

	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	static void toast(Context context,String str){
		Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
	}

	@Override
	/**
	 * 当前页面结束时，保存本地信息
	 */
	protected void onDestroy() {
		super.onDestroy();
		userManager.onDepose();
	}

	@Override
	/**
	 * 防止用户杀进程时未同步本地信息，页面不可见时就进行保存
	 */
	protected void onStop() {
		super.onStop();
		userManager.onDepose();
	}

}

