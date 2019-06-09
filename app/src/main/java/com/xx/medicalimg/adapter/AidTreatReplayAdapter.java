package com.xx.medicalimg.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.xx.medicalimg.R;
import com.xx.medicalimg.util.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 病单处理反馈选项适配器
 */
public class AidTreatReplayAdapter extends RecyclerView.Adapter<AidTreatReplayAdapter.ViewHolder> {
	private List<String> keyList = Arrays.asList(AID_TREAT_CLINICAL_DIAGNOSIS,AID_TREAT_ANALYSIS,AID_TREAT_AID_SUMMARY);
	private Map<String,String> map = new HashMap<>();
	private OnTitleLongClickListener onTitleLongClickListener;
	private OnEditTextChangedListener onEditTextChangedListener;

	public final static String AID_TREAT_CLINICAL_DIAGNOSIS = "【临床诊断】：";
	public final static String AID_TREAT_ANALYSIS = "【本例分析】：";
	public final static String AID_TREAT_AID_SUMMARY = "【病例小结】：";

	public List<String> getKeyList() {
		return keyList;
	}
	public Map<String, String> getMap() {
		return map;
	}


	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		return new AidTreatReplayAdapter.ViewHolder(
				LayoutInflater.from(viewGroup.getContext()).inflate(
						R.layout.item_aid_treat_replay,viewGroup,false));
	}
	@Override
	public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
		//LogUtils.e(" " + i + " onBind","before");
		viewHolder.textView.setText(keyList.get(i));
		if(onTitleLongClickListener != null){
			viewHolder.textView.setOnClickListener(v -> onTitleLongClickListener.onTitleLongClick(v,i));
		}
		if( !keyList.contains((String) viewHolder.editText.getTag())){
//			LogUtils.e(" " + i + " tag","###");
			TextWatcher textWatcher = new MTextWatcher(keyList.get(i));
			viewHolder.editText.addTextChangedListener(textWatcher);
			viewHolder.editText.setTag(keyList.get(i));
		}
		LogUtils.e(" " + i + " onBind","before");
	}

	@Override
	public int getItemCount() {
		return keyList == null ? 0:keyList.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder{
		TextView textView;
		EditText editText;
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.tv_tile);
			editText = itemView.findViewById(R.id.et_content);
		}
	}

	public void setOnTitleLongClickListener(OnTitleLongClickListener onTitleLongClickListener) {
		this.onTitleLongClickListener = onTitleLongClickListener;
	}

	public void setOnEditTextChangedListener(OnEditTextChangedListener onEditTextChangedListener) {
		this.onEditTextChangedListener = onEditTextChangedListener;
	}

	public interface OnTitleLongClickListener{
		void onTitleLongClick(View v,int i);
	}
	public  interface OnEditTextChangedListener{
		void onTextChanged(int i);
	}
	class MTextWatcher implements TextWatcher{
		String key;
		MTextWatcher(String key){
			this.key = key;
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			map.put(key,s.toString());
		}
	}
}
