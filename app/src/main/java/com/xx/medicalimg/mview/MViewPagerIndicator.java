package com.xx.medicalimg.mview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义View,重写自BottomNavigationView，ViewPager列表指示器，图标对应当前Item
 */
public class MViewPagerIndicator extends BottomNavigationView
        implements ViewPager.OnPageChangeListener,BottomNavigationView.OnNavigationItemSelectedListener{
    private ViewPager viewPager;
    private List<Integer> idList = new ArrayList<>();
    private Scroller scroller;
    public MViewPagerIndicator(Context context) {
        super(context);
    }

    public MViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 添加ViewPager到指示器
     * @param viewPager 对应ViewPager
     * @param count 当前ViewPager页面数量
     * @throws Exception
     */
    public void addViewPager(ViewPager viewPager, int count) throws Exception {
        this.viewPager = viewPager;
        if (count != getMenu().size()){//getChildCount并不是获取fragment数量和指示器图标数量
            throw new Exception("viewPager child count " +viewPager.getChildCount()+ "!= indicator child count"+getChildCount());
        }
        for(int i = 0; i<count; i++){
            int id = getMenu().getItem(i).getItemId();
            idList.add(id);
        }
        scroller = new Scroller(getContext());
        viewPager.addOnPageChangeListener(this);
        setOnNavigationItemSelectedListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    /**
     * 接口方法，ViewPager某个页面选中时调用，切换图标显示
     */
    public void onPageSelected(int i) {
        setSelectedItemId(idList.get(i));
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    @Override
    /**
     * 当底部图标被点击时调用，切换ViewPager当前Fragment
     */
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(idList.contains(menuItem.getItemId())){
            int position = idList.indexOf(menuItem.getItemId());
            viewPager.setCurrentItem(position,true);//滑动
            return true;//处理事件则要消费事件
        }
        return false;
    }
}
