package com.xx.medicalimg.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.xx.medicalimg.R;
import com.xx.medicalimg.adapter.ClassficationAdapter;

/**
 * 主界面第二个Fragment，显示分类，暂时没用到
 */
public class ClassficationFragment extends Fragment{
	public final static String TAG = "NotificationFragment";
	private RecyclerView recyclerView;
	private ClassficationAdapter adapter;
	public static ClassficationFragment newInstance(){
		ClassficationFragment f = new ClassficationFragment();
		return f;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new ClassficationAdapter() {
			@Override
			public void onItemClick(View v, int i) {
				toast("" + i);
			}
		};
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_classification,container,false);
		StaggeredGridLayoutManager layoutManager =
				new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
		recyclerView = view.findViewById(R.id.rv_classfication);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);
		return view;
	}
	private void toast(String str){
		Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
	}
}
