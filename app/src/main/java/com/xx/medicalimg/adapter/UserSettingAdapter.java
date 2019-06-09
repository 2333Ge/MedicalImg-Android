package com.xx.medicalimg.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xx.medicalimg.MApplication;
import com.xx.medicalimg.R;
import com.xx.medicalimg.bean.Employee;
import com.xx.medicalimg.bean.User;
import com.xx.medicalimg.constant.UserSettings;
import com.xx.medicalimg.manager.UserManager;

import java.util.List;

/**
 *  用户信息更改列表适配器
 */
public class UserSettingAdapter extends RecyclerView.Adapter<UserSettingAdapter.ViewHolder> {
	public List<String> list;
	private boolean isNormal;//决定是是用户信息还是医务人员信息

	private OnRightIconClickListener onRightIconClickListener;

	/**
	 * 参数决定显示内容是用户信息还是医务人员信息
	 * @param isNormal true 用户信息 false 医务人员信息
	 */
	public UserSettingAdapter(boolean isNormal){
		super();
		this.isNormal = isNormal;
		init();
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new ViewHolder(
				LayoutInflater.from(viewGroup.getContext()).inflate(
						R.layout.item_user_settings_normal,viewGroup,false));
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		viewHolder.tv_title.setText(list.get(i));
		viewHolder.tv_content.setText(getContent(i));
		if (onRightIconClickListener != null){
			viewHolder.right.setOnClickListener(v -> onRightIconClickListener.onRightIconClick(v,i));
		}
	}

	@Override
	public int getItemCount() {
		return list == null ? 0:list.size();
	}

	 static class ViewHolder extends RecyclerView.ViewHolder{
		ImageView right;
		TextView tv_title;
		TextView tv_content;
		 ViewHolder(@NonNull View itemView) {
			super(itemView);
			right = itemView.findViewById(R.id.iv_settingItem_iconNext);
			tv_title = itemView.findViewById(R.id.tv_settingItem_title);
			tv_content = itemView.findViewById(R.id.tv_settingItem_content);
		}
	}

	public void setOnRightIconClickListener(OnRightIconClickListener onRightIconClickListener) {
		this.onRightIconClickListener = onRightIconClickListener;
	}

	public interface OnRightIconClickListener {
		void onRightIconClick(View v,int i);
	}
	public List<String> getList() {
		return list;
	}
	/**
	 * 获得内容框内容，和list对应
	 * @param i 对应list序号
	 * @return 对应内容框内容
	 */
	public String getContent(int i){
		User user = MApplication.getUserManager().getBasicUser();//用于获取基础信息
		Employee employee = MApplication.getUserManager().getEmployee();//用于获取其他信息
		if (isNormal){//姓名 昵称 电话 邮箱 性别 生日
			switch (i){
				case 0:
					return user.getName();
				case 1:
					return user.getNickname();
				case 2:
					return user.getPhone();
				case 3:
					return user.getEmail();
				case 4:
					return user.getSex() == 0 ? "男":"女";
				case 5:
					return user.getBirthday();
			}
		}else{ //工号 医院编号 科 室
			if (employee == null){
				return null;
			}
			switch (i){
				case 0:
					return employee.getEmployeeId();
				case 1:
					return employee.getHospitaId();
				case 2:
					return employee.getDepartment();
				case 3:
					return employee.getRoom();
			}
		}
		return null;
	}

	private void init() {
		if (isNormal){
			this.list = UserSettings.normalList;
		}else{
			this.list = UserSettings.employeeList;
		}
	}
}
