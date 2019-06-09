package com.xx.medicalimg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.xx.medicalimg.R;
import com.xx.medicalimg.abs.view.ILoadVisibleItemsImage;
import com.xx.medicalimg.activity.ImgDisplayActivity;
import com.xx.medicalimg.constant.Urls;
import com.xx.medicalimg.util.LogUtils;

import java.util.List;

/**
 * 病单图片显示适配器
 */
public class ImgDisplayAdapter extends RecyclerView.Adapter<ImgDisplayAdapter.ViewHolder>  {
	private List<String> imgList;
	private String aidRecordID;
	private Context context;
	private String order;
	public ImgDisplayAdapter(String aidRecordID,List<String> imgList,String order){
		this.aidRecordID = aidRecordID;
		this.imgList = imgList;
		this.order = order;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		context = viewGroup.getContext();
		return new ImgDisplayAdapter.ViewHolder(
				LayoutInflater.from(viewGroup.getContext()).inflate(
						R.layout.img_display,viewGroup,false));

	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
		RequestOptions options = new RequestOptions()
				.error(R.drawable.load_fail64)
				.fitCenter()
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE);//只缓存最终的图像
		Glide.with(context)
				.applyDefaultRequestOptions(options)
				.load(Urls.getAidRecordJpgUrl(aidRecordID,imgList.get(i)))
				.into(viewHolder.iv_display);
				if(imgList!= null) viewHolder.tv_order.setText(i+1 + "/" +imgList.size());
				viewHolder.iv_display.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						ImgDisplayActivity.actionStart(context,imgList.get(i),aidRecordID,order);
					}
				});
	}

	@Override
	public int getItemCount() {
		return imgList == null ? 0:imgList.size();//aidReleaseList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		ImageView iv_display;
		TextView tv_order;
		ViewHolder(@NonNull View itemView) {
			super(itemView);
			iv_display = itemView.findViewById(R.id.iv_display);
			tv_order  = itemView.findViewById(R.id.tv_order);
		}
	}
}
