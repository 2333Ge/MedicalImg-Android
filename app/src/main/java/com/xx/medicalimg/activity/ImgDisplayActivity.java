package com.xx.medicalimg.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xx.medicalimg.R;
import com.xx.medicalimg.constant.Urls;

import uk.co.senab.photoview.PhotoView;

/**
 * 图片查看活动
 * .<br/>显示图像、序号
 */
public class ImgDisplayActivity extends AppCompatActivity {
	 private String imgName;
	 private String id;
	 private String order;
	 private PhotoView pv;
	 private TextView tv;

	/**
	 *
	 * @param context 上下文
	 * @param imgName 图片名
	 * @param id 对应aidRecordId
	 * @param order 序号
	 */
	 public static void actionStart(Context context,String imgName, String id,String order){
		 Intent intent = new Intent(context, ImgDisplayActivity.class);
		 intent.putExtra("imgName",imgName);
		 intent.putExtra("id",id);
		 intent.putExtra("order",order);
		 context.startActivity(intent);
	 }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_img_display);
		initData();
		initView();
	}

	private void initData() {
		imgName = getIntent().getStringExtra("imgName");
		id = getIntent().getStringExtra("id");
		order = getIntent().getStringExtra("order");
	}

	private void initView() {
		pv = findViewById(R.id.pv_display);
		tv = findViewById(R.id.tv_order);

		hideActionBar();

		RequestOptions options = new RequestOptions()
				.error(R.drawable.load_fail64)
				.fitCenter()
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图像
		Glide.with(this)
				.applyDefaultRequestOptions(options)
				.load(Urls.getAidRecordJpgUrl(id,imgName))
				.into(pv);
		tv.setText(order);
	}

	/**
	 * 隐藏状态栏
	 */
	private void hideActionBar() {
		//得到当前界面的装饰视图
		if(Build.VERSION.SDK_INT >= 21) {
			View decorView = getWindow().getDecorView();
			//让应用主题内容占用系统状态栏的空间,注意:下面两个参数必须一起使用 stable 牢固的
			int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			decorView.setSystemUiVisibility(option);
			//设置状态栏颜色为透明
			getWindow().setStatusBarColor(Color.TRANSPARENT);
		}
		View decorView = getWindow().getDecorView();
//        SYSTEM_UI_FLAG_FULLSCREEN表示全屏的意思，也就是会将状态栏隐藏
		//设置系统UI元素的可见性
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		//隐藏标题栏
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
	}

}
