package com.xx.medicalimg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xx.medicalimg.activity.AidTreatActivity;
import com.xx.medicalimg.activity.AllUploadAidReleaseActivity;
import com.xx.medicalimg.activity.HandledAidCaseActivity;
import com.xx.medicalimg.activity.LoginActivity;
import com.xx.medicalimg.activity.UserSettingActivity;
import com.xx.medicalimg.bean.AidCase;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.bean.User;
import com.xx.medicalimg.fragment.AidCaseFragment;
import com.xx.medicalimg.fragment.ClassficationFragment;
import com.xx.medicalimg.fragment.NotificationFragment;
import com.xx.medicalimg.manager.UserManager;
import com.xx.medicalimg.mview.MViewPagerIndicator;
import com.xx.medicalimg.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 主活动，启动应用时调用
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    //private View headerLayout;
    private ImageView iv_header_drawer;
    private TextView tv_name,tv_email;
    private MViewPagerIndicator indicator;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private FragmentPagerAdapter pagerAdapter;
    private UserManager userManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userManager = MApplication.getUserManager();
        initView();
    }
    private void initView(){

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        iv_header_drawer = headerLayout.findViewById(R.id.iv_header_drawer);
        tv_name = headerLayout.findViewById(R.id.tv_name);
        tv_email = headerLayout.findViewById(R.id.tv_email);

        iv_header_drawer.setOnClickListener(this);
        tv_email.setOnClickListener(this);
        tv_name.setOnClickListener(this);
        setUserInfoView();
        initViewPager();
    }

    private void setUserInfoView() {
        if(userManager.isLogin()){
            String type = userManager.getUserType();
            User user;
            if(UserManager.USER_TYPE_EMPLOYEE.equals(type)){
                Employee employee = (Employee)userManager.getUser();
                user = employee.getBasicInfo();
            }else {
                user = (User)userManager.getUser();
            }
            tv_email.setText(user.getEmail() == null?"邮箱为空":user.getEmail());
            tv_name.setText(user.getNickname() == null?"昵称为空":user.getName());
        }else{
            tv_email.setText("邮箱为空");
            tv_name.setText("请先登录");
        }
    }

    private void initViewPager(){
        viewPager = findViewById(R.id.mainViewPager);
        indicator = findViewById(R.id.navigation);
        fragmentList.add(AidCaseFragment.newInstance());
        fragmentList.add(ClassficationFragment.newInstance());
        fragmentList.add(NotificationFragment.newInstance());
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList.get(i);
            }
            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
        viewPager.setAdapter(pagerAdapter);
        try {
            indicator.addViewPager(viewPager,fragmentList.size());
        } catch (Exception e) {
            LogUtils.e("设置viewPager指示器",e.getLocalizedMessage());
        }

    }

    @Override
    public void onBackPressed() {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧边栏菜单
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if ( !userManager.isLogin()){
            toast("请先登陆");
            return true;
        }
        if (id == R.id.nav_camera) {

        } else if (id == R.id.nav_gallery) {
            if (UserManager.USER_TYPE_EMPLOYEE.equals(userManager.getUserType()))
                startActivity(HandledAidCaseActivity.class);
            else{
                toast("非工作人员无法查看，请先完善信息");
            }
        } else if (id == R.id.nav_slideshow) {
            if (UserManager.USER_TYPE_EMPLOYEE.equals(userManager.getUserType()))
                startActivity(AllUploadAidReleaseActivity.class);
            else{
                toast("非工作人员无法查看，请先完善信息");
            }
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_header_drawer://已登陆则用户资料页面，未登陆则登陆
                if (MApplication.getUserManager().isLogin()){
                    UserSettingActivity.actionStart(this);
                }else{
                    Intent intent = new Intent(this,LoginActivity.class);
                    startActivityForResult(intent,1);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                setUserInfoView();
                break;
        }
    }

    private void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class c){
    	Intent i = new Intent(this,c);
    	startActivity(i);
	}
}
