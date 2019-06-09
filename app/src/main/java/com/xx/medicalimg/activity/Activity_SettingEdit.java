package com.xx.medicalimg.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.xx.medicalimg.MApplication;
import com.xx.medicalimg.R;
import com.xx.medicalimg.mview.Topbar;

/**
 * 文本类个人信息修改活动
 * .<br/> 获取intent中的3个数据，编辑，返回结果
 */
public class Activity_SettingEdit extends AppCompatActivity {

    public static final String KEY_TITLE = "title";
    public static final String KEY_VALUE_LAST = "valueLast";
    public static final String KEY_POSITION = "position";

    public static void actionStart(Context context,String title, String valueLast,int position){
        Intent intent = new Intent(context,Activity_SettingEdit.class);
        intent.putExtra(KEY_TITLE,title);
        intent.putExtra(KEY_VALUE_LAST,valueLast);
        intent.putExtra(KEY_POSITION,position);
        context.startActivity(intent);
    }



    private Topbar topbar;
    private EditText et_input;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_edit);
        initView();
    }

    private void initView() {
        topbar = findViewById(R.id.topBar_settingEdit);
        et_input = findViewById(R.id.et_settingInput);
        Intent intent = getIntent();
        String title = "修改",value = "";
        title = intent.getStringExtra(KEY_TITLE);
        value = intent.getStringExtra(KEY_VALUE_LAST);
        position = intent.getIntExtra(KEY_POSITION,0);
        topbar.setTitleText(title);
        et_input.setText(value);
        et_input.setSelection(value == null ? 0:value.length());
        topbar.setOnTopbarCilckListener(topbarClickListener);
    }
    Topbar.TopbarCilckListener topbarClickListener = new Topbar.TopbarCilckListener(){

        @Override
        public void leftClick(View v) {
            Activity_SettingEdit.this.finish();
        }

        @Override
        public void rightClick(View v) {
            //向服务器发送更改信息+更改本地信息+回传给上一个界面
            Intent intent = new Intent();
            intent.putExtra(KEY_POSITION,position);
            intent.putExtra(KEY_VALUE_LAST,et_input.getText().toString());
            setResult(UserSettingActivity.REQUEST_USER_SETTING,intent);
            Activity_SettingEdit.this.finish();
            //Toast.makeText(Activity_SettingEdit.this,"right",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
