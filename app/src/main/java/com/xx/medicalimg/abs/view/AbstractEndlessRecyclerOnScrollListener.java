package com.xx.medicalimg.abs.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

/**
 * 上拉加载动作监听器,滑动结束时加载图像
 */
public abstract class AbstractEndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener{
	//用来标记是否正在向上滑动
	private boolean isSlidingUpward = false;
	private Context context;
	public AbstractEndlessRecyclerOnScrollListener(Context context){
		this.context = context;
	}
	@Override
	/**
	 * 滑动到末端调用onLoadMore
	 * 滑动时设置不加载图像
	 */
	public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
		super.onScrollStateChanged(recyclerView, newState);
		LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
		// 当不滑动时
		if (newState == RecyclerView.SCROLL_STATE_IDLE) {
			//获取最后一个完全显示的itemPosition
			int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
			int itemCount = manager.getItemCount();

			// 判断是否滑动到了最后一个item，并且是向上滑动
			if (lastItemPosition == (itemCount - 1) && isSlidingUpward) {
				//加载更多
				onLoadMore();
			}

			if(context != null) Glide.with(context).resumeRequests();
			}else {
				if(context != null)  Glide.with(context).pauseRequests();
			}
	}

	@Override
	/**
	 * 判断滑动方向
	 */
	public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
		super.onScrolled(recyclerView, dx, dy);
		// 大于0表示正在向上滑动，小于等于0表示停止或向下滑动
		isSlidingUpward = dy > 0;
	}

	/**
	 * 加载更多回调函数
	 */
	public abstract void onLoadMore();
}
