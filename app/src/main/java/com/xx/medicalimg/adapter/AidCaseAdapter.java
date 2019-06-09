package com.xx.medicalimg.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xx.medicalimg.R;
import com.xx.medicalimg.bean.AidCase;
import com.xx.medicalimg.bean.AidRelease;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.constant.ConstantKeyInAskJson;
import com.xx.medicalimg.constant.ConstantKeyInJson;
import com.xx.medicalimg.constant.Urls;
import com.xx.medicalimg.net.HttpAsyncTask;
import com.xx.medicalimg.net.HttpResponseParam;
import com.xx.medicalimg.util.LogUtils;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 病例显示适配器
 */
public class AidCaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private static final String TAG = "AidCaseAdapter";
	private List<AidCase> aidCasesList;
	private Context context;
	private int footLoadState;
	// 当前布局标志位
	private final int TYPE_ITEM = 1;    // 普通布局
	private final int TYPE_FOOT = 2;    // 脚布局
	private boolean isMine = false;
	//加载状态,4底部栏专属
	public static final int LOADING = 1;// 正在加载
	public static final int LOADING_COMPLETE = 2;// 加载完成
	public static final int LOADING_ERROR = 3;// 加载失败
	public static final int LOADING_END = 4;// 加载到底
	//底部显示内容，和上面标志位一一对应
	private final List<String> listState = Arrays.asList("正在加载", "加载完成", "加载失败", "加载到底");
	//private final List<String> aidReleaseState = Arrays.asList("不急", "较急", "急", "很急", "非常急");

	private OnItemClickListener onItemClickListener;

	public AidCaseAdapter(List<AidCase> aidCasesList) {
		this.aidCasesList = aidCasesList;

	}
	public AidCaseAdapter(List<AidCase> aidCasesList,boolean isMine){
		this.aidCasesList = aidCasesList;
		this.isMine = isMine;
	}

	@Override
	/**
	 * 区分item是否是最后一个
	 */
	public int getItemViewType(int position) {
		// 最后一个item设置为FootView
		if (position + 1 == getItemCount()) {
			return TYPE_FOOT;
		} else {
			return TYPE_ITEM;
		}
	}

	@NonNull
	@Override
	/**
	 * 根据viewType返回不同视图
	 */
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		context = viewGroup.getContext();
		switch (viewType) {
			case TYPE_ITEM:
				return new ItemViewHolder(
						LayoutInflater.from(viewGroup.getContext()).inflate(
								R.layout.aid_release_item, viewGroup, false));
			case TYPE_FOOT:
				return new FootViewHolder(
						LayoutInflater.from(viewGroup.getContext()).inflate(
								R.layout.foot_view, viewGroup, false));
		}
		return null;
	}

	@Override
	/**
	 * 根据viewType加载不同视图
	 */
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
		if (viewHolder instanceof ItemViewHolder) {//设置普通条目
			AidCase aidCase = aidCasesList.get(i);
			ItemViewHolder holder = (ItemViewHolder) viewHolder;
			holder.tv_content.setText(getContentText(aidCase));
			//设置监听器
			if (onItemClickListener != null) {
				holder.tv_content.setOnClickListener(v -> onItemClickListener.onClick(v, i));
			}
			AidRelease aidRelease = aidCase.getAidRelease();
			String greateTime = "发布时间: " + aidCase.getGreateTime().split(" ")[0];
			holder.tv_time.setText(greateTime);
			String modifiedTime = "处理时间: " + aidRelease.getModifiedTime().split(" ")[0];
			holder.tv_state.setText(modifiedTime);
			if( !isMine )
				new NicknameTask(holder.tv_state,aidRelease).execute();//此处应该存储数据不然每次都要加载网络
			ImgDisplayAdapter imgDisplayAdapter = new ImgDisplayAdapter(aidCase.getAidRecord().getId(), aidCase.getAidRecord().getImgList(),i+"/"+aidCasesList.size());
			holder.rv_display.setAdapter(imgDisplayAdapter);
			//holder.tv_state.setText(aidReleaseState.get(aidRelease.getState()));
			Employee employee = aidRelease.getUpLoadEmployee();
			if (employee != null) {
				//LogUtils.e("######","employee!= null");
				holder.tv_department.setText(employee.getDepartment() + " " + employee.getRoom());
				if (employee.getBasicInfo() != null) {
					holder.tv_nickName.setText(employee.getBasicInfo().getNickname());
					RequestOptions options = new RequestOptions()
							.error(R.drawable.load_fail64)
							.fitCenter()
							.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图像
					Glide.with(context)
							.applyDefaultRequestOptions(options)
							.load(Urls.getHeadImgUrl(employee.getEmployeeId(), employee.getBasicInfo().getHeadImg()))
							.into(holder.civ_head);
				}


			}
		} else if (viewHolder instanceof FootViewHolder) {
			FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
			switch (footLoadState) {
				case LOADING: // 正在加载
					footViewHolder.pbLoading.setVisibility(View.VISIBLE);
					footViewHolder.tvLoading.setVisibility(View.VISIBLE);
					//footViewHolder.llEnd.setVisibility(View.GONE);
					break;

				case LOADING_COMPLETE: // 加载完成
					footViewHolder.pbLoading.setVisibility(View.INVISIBLE);
					footViewHolder.tvLoading.setVisibility(View.INVISIBLE);
					//footViewHolder.llEnd.setVisibility(View.GONE);
					break;
				case LOADING_ERROR: // 加载失败
					footViewHolder.pbLoading.setVisibility(View.VISIBLE);
					footViewHolder.tvLoading.setVisibility(View.VISIBLE);
					//footViewHolder.llEnd.setVisibility(View.GONE);
					break;
				case LOADING_END: // 加载到底
					footViewHolder.pbLoading.setVisibility(View.GONE);
					footViewHolder.tvLoading.setVisibility(View.GONE);
					//footViewHolder.llEnd.setVisibility(View.VISIBLE);
					break;

				default:
					break;
			}
		}

	}

	@Override
	public int getItemCount() {
		return aidCasesList.size() + 1;//aidReleaseList.size();
	}

	static class ItemViewHolder extends RecyclerView.ViewHolder {
		CircleImageView civ_head;
		TextView tv_nickName, tv_time, tv_state, tv_department, tv_content;
		RecyclerView rv_display;

		ItemViewHolder(@NonNull View itemView) {
			super(itemView);
			civ_head = itemView.findViewById(R.id.civ_headImg);
			tv_content = itemView.findViewById(R.id.tv_content);
			tv_nickName = itemView.findViewById(R.id.tv_nickname);
			tv_time = itemView.findViewById(R.id.tv_time);
			tv_state = itemView.findViewById(R.id.tv_state);
			tv_department = itemView.findViewById(R.id.tv_department);
			rv_display = itemView.findViewById(R.id.rv_imgs);
		}
	}

	private static class FootViewHolder extends RecyclerView.ViewHolder {

		ProgressBar pbLoading;
		TextView tvLoading;

		//LinearLayout llEnd;
		FootViewHolder(View itemView) {
			super(itemView);
			pbLoading = itemView.findViewById(R.id.pb_loading);
			tvLoading = itemView.findViewById(R.id.tv_loadState);
			//llEnd = itemView.findViewById(R.id.ll_footView);
		}
	}

	/**
	 * 设置上拉加载状态
	 *
	 * @param loadState 1.正在加载 2.加载完成 3.加载失败 4.加载到底
	 */
	public void setFootLoadState(int loadState) {
		this.footLoadState = loadState;
		notifyDataSetChanged();
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
				.append(aidCase.getAidRecord().getImagingFeatures())
				.append("\n【临床诊断】")
				.append(aidCase.getAidTreat().getClinicalDiagnosis())
				.append("\n【本例分析】")
				.append(aidCase.getAidTreat().getAnalysis())
				.append("\n【病例小结】")
				.append(aidCase.getAidTreat().getAidSummary());
		return sb.toString();

	}

	public int getFootLoadState() {
		return footLoadState;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public interface OnItemClickListener {
		void onClick(View v, int current);
	}

	static class NicknameTask extends HttpAsyncTask<TextView> {
		AidRelease aidRelease;
		public NicknameTask(TextView context,AidRelease aidRelease) {
			this(context, Urls.ASK_USER_INFO, null);
			this.aidRelease = aidRelease;
			JSONObject test;
			test = new JSONObject();
			test.put(ConstantKeyInAskJson.EMPLOYEE_OR_USER,0);
			test.put(ConstantKeyInAskJson.ID,aidRelease.getHandleEmployeeId());
			test.put(ConstantKeyInAskJson.REQUEST_TYPE,3);
			setRequestBody(test.toJSONString());
		}

		public NicknameTask(TextView context, String url, String requestBody) {
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
			if(getWeakTarget() != null){
				String modifiedTime = "处理时间: " + aidRelease.getModifiedTime().split(" ")[0];
				String handleInfo = modifiedTime+"\n处理人："+jsonObject.getString(ConstantKeyInJson.NET_NICKNAME);
				getWeakTarget().setText(handleInfo);
			}

		}

		@Override
		protected void onPreLoad() {

		}

	}
}
