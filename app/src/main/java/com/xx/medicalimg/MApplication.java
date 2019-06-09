package com.xx.medicalimg;

import android.app.Application;
import android.content.Context;

import com.xx.medicalimg.manager.UserManager;

/**
 * 应用实体，启动应用时调用
 */
public class MApplication extends Application {
	private static UserManager userManager;
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}

	/**
	 * 懒汉单例模式获取本地用户信息管理者UserManger
	 * @return 本地用户信息管理者
	 */
	public static UserManager getUserManager(){
		if (userManager == null){
			userManager = new UserManager(context);
		}
		return userManager;
	}



}
