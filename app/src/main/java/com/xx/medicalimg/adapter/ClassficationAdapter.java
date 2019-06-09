package com.xx.medicalimg.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xx.medicalimg.R;
import com.xx.medicalimg.constant.DepartmentAndRoom;

import java.util.Map;

/**
 * 中间fragment，分类选项适配器
 */
public abstract class ClassficationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private Map<Integer,String> map;//存储类型map,用map解决乱序问题

	public ClassficationAdapter(){
		map = DepartmentAndRoom.departmentMap;
	}
	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new ClassficationAdapter.ViewHolder(
				LayoutInflater.from(viewGroup.getContext()).inflate(
						R.layout.item_classfication,viewGroup,false));
	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
		ViewHolder holder = (ViewHolder)viewHolder;
		holder.tv.setText(map.get(i));
		holder.cv.setOnClickListener(new OnItemClickListener(i));

	}

	@Override
	public int getItemCount() {
		return map == null ? 0 : map.size();
	}
	static class ViewHolder extends RecyclerView.ViewHolder{
		CardView cv;
		TextView tv;
		ViewHolder(@NonNull View itemView) {
			super(itemView);
			cv = itemView.findViewById(R.id.cv_partent);
			tv = itemView.findViewById(R.id.tv_content);
		}
	}
	public abstract void onItemClick(View v,int i);

	class OnItemClickListener implements  View.OnClickListener{
		private int currtent;
		public OnItemClickListener(int i){
			currtent = i;
		}
		@Override
		public void onClick(View v) {
			onItemClick(v,currtent);
		}
	}
}
