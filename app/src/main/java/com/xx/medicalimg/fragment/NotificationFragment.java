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
import com.xx.medicalimg.activity.AidTreatActivity;
import com.xx.medicalimg.bean.AidRelease;
import com.xx.medicalimg.adapter.AidReleaseAdapter;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.bean.User;
import com.xx.medicalimg.constant.ConstantKeyInAskJson;
import com.xx.medicalimg.constant.ConstantKeyInJson;
import com.xx.medicalimg.constant.HttpResponseCode;
import com.xx.medicalimg.constant.Urls;
import com.xx.medicalimg.manager.UserManager;
import com.xx.medicalimg.net.HttpAsyncTask;
import com.xx.medicalimg.net.HttpResponseParam;
import com.xx.medicalimg.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面第三个Fragment,显示病单通知
 */
public class NotificationFragment extends Fragment {
    public final static String TAG = "NotificationFragment";
    public static final String BUNDLE_TITLE = "title";
    private View view;
    private RecyclerView recyclerView;
    private AidReleaseAdapter aidReleaseAdapter;
    private AbstractEndlessRecyclerOnScrollListener onScrollListener;
    private List<AidRelease> aidReleaseList;

    private Employee user;
    private UserManager userManager;
    private SwipeRefreshLayout srl_main;
    public static NotificationFragment newInstance(){
        NotificationFragment f = new NotificationFragment();
        return f;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUser();
        initData();
    }

    private void initUser() {
         userManager = MApplication.getUserManager();
        if ( !userManager.isLogin()){
            Toast.makeText(getActivity(),"请先登陆",Toast.LENGTH_SHORT).show();
            return;
        }
        if (UserManager.USER_TYPE_EMPLOYEE.equals(userManager.getUserType())){
            user = (Employee) userManager.getUser();
        }else{
            Toast.makeText(getActivity(),"非工作人员无法查看病单，请先完善信息",Toast.LENGTH_LONG).show();
        }
    }

    private void initData() {

        onScrollListener = new AbstractEndlessRecyclerOnScrollListener(getActivity()) {
            @Override
            public void onLoadMore() {
                if(aidReleaseAdapter.getFootLoadState() != AidReleaseAdapter.LOADING_END){
                    new AidReleaseTask(NotificationFragment.this,aidReleaseList,Urls.ASK_AID_RELEASE,aidReleaseList.size(),aidReleaseList.size()+5,user.getId()).execute();
                    }
            }
        };
        if( !UserManager.USER_TYPE_EMPLOYEE.equals(userManager.getUserType())){
            return;
        }
        resetAdapter();
    }
    private void resetAdapter(){
        aidReleaseList = new ArrayList<>();
        aidReleaseAdapter = new AidReleaseAdapter(aidReleaseList);
        aidReleaseAdapter.setOnItemClickListener((v, current) -> {
            AidTreatActivity.actionStart(getActivity(),aidReleaseList.get(current));
        });
        if(user != null){
			new AidReleaseTask(this,aidReleaseList,Urls.ASK_AID_RELEASE,0,5,user.getId()).execute();
		}


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main,container,false);
        recyclerView = view.findViewById(R.id.rv_main);
        recyclerView.setAdapter(aidReleaseAdapter);
        recyclerView.addOnScrollListener(onScrollListener);

        srl_main = view.findViewById(R.id.srl_main);
        srl_main.setOnRefreshListener(() -> resetAdapter());
        return view;
    }

    static class AidReleaseTask extends HttpAsyncTask<NotificationFragment>{
        private List<AidRelease> listAidRelease;
        //初始化view并在获取网络返回值成功时加载对应view
        public AidReleaseTask(NotificationFragment context, List<AidRelease> listAidRelease,String url, int start,int end,String employeeId) {
            this(context, url, null);
            this.listAidRelease = listAidRelease;
            JSONObject test;
            test = new JSONObject();
            test.put(ConstantKeyInAskJson.AID_RELEASE_HANDLE_EMPLOYEE_ID,employeeId);
            test.put(ConstantKeyInAskJson.AID_RELEASE_START,start);
            test.put(ConstantKeyInAskJson.AID_RELEASE_END,end);
            test.put(ConstantKeyInAskJson.AID_RELEASE_REQUEST_PERSON_TYPE,0);//处理者
            test.put(ConstantKeyInAskJson.AID_RELEASE_TYPE,0);//未处理病单
            setRequestBody(test.toJSONString());
        }

        public AidReleaseTask(NotificationFragment context, String url, String requestBody) {
            super(context, url, requestBody);
        }

        @Override
        protected void onFail(HttpResponseParam httpResponseParam) {
            getWeakTarget().srl_main.setRefreshing(false);
            LogUtils.e(TAG,"onFail" + httpResponseParam.getStatusCode() + httpResponseParam.getResponseString());
            if(httpResponseParam.getStatusCode() == HttpResponseCode.NULL_RESULT){
                setLoadStatus(AidReleaseAdapter.LOADING_END);
            }else{
                setLoadStatus(AidReleaseAdapter.LOADING_ERROR);
            }
            LogUtils.e(TAG,"错误代码：" + httpResponseParam.getStatusCode());
        }

        @Override
        protected void onDataReceive(HttpResponseParam httpResponseParam) {
//            LogUtils.e(TAG,httpResponseParam.getStatusCode() + "\n" + httpResponseParam.getResponseString());
            JSONObject jsonObject = JSONObject.parseObject(httpResponseParam.getResponseString());
            JSONArray jsonArray = jsonObject.getJSONArray(ConstantKeyInJson.NET_AID_RELEASE_ARRAY);
            for(int i = 0; i<jsonArray.size(); i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                AidRelease aidRelease = new AidRelease();
                aidRelease.transJsonToJavaBean(jsonObject1);
                listAidRelease.add(aidRelease);
                Employee employee = new Employee();
                employee.setId(aidRelease.getUploadEmployeeId());
                new EmployeeCommonInfoTask(getWeakTarget().aidReleaseAdapter,aidRelease).execute();
            }
            if(getWeakTarget() != null){
                getWeakTarget().aidReleaseAdapter.notifyDataSetChanged();
                getWeakTarget().srl_main.setRefreshing(false);
             }
            setLoadStatus(AidReleaseAdapter.LOADING_COMPLETE);
        }

        @Override
        protected void onPreLoad() {
            setLoadStatus(AidReleaseAdapter.LOADING);
        }
        private void setLoadStatus(int status){
            if(getWeakTarget() != null){
                getWeakTarget().aidReleaseAdapter.setFootLoadState(status);
            }
        }
    }
    static class EmployeeCommonInfoTask extends HttpAsyncTask<AidReleaseAdapter> {
        AidRelease aidRelease;
        public EmployeeCommonInfoTask(AidReleaseAdapter context,AidRelease aidRelease) {
            this(context, Urls.ASK_USER_INFO, null);
            this.aidRelease = aidRelease;
            JSONObject test;
            test = new JSONObject();
            test.put(ConstantKeyInAskJson.EMPLOYEE_OR_USER,0);
            test.put(ConstantKeyInAskJson.ID,aidRelease.getUploadEmployeeId());
            test.put(ConstantKeyInAskJson.REQUEST_TYPE,1);
            setRequestBody(test.toJSONString());
        }

        public EmployeeCommonInfoTask(AidReleaseAdapter context, String url, String requestBody) {
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
            aidRelease.setUpLoadEmployee(employee);
            employee.transJsonToJavaBean(jsonObject.getJSONObject(ConstantKeyInJson.NET_USER_INFO));
            if(getWeakTarget() != null){
                getWeakTarget().notifyDataSetChanged();
            }

        }

        @Override
        protected void onPreLoad() {

        }

    }

}
