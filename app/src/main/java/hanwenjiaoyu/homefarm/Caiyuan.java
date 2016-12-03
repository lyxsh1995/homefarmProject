package hanwenjiaoyu.homefarm;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import bean.myPagerAdapter;

/**
 * Created by Administrator on 2016/12/3.
 */

//V4包需要FragmentActivity
public class Caiyuan extends FragmentActivity
{

    private ViewPager pager;
    private PagerTabStrip tabStrip;
    java.util.List<Fragment> mFragments = new ArrayList<Fragment>();
    List<String> titleContainer = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caiyuan);

        pager = (ViewPager) findViewById(R.id.viewpager1);
        tabStrip = (PagerTabStrip) findViewById(R.id.tabstrip1);

        addviewpager();
    }

    private void addviewpager()
    {

        //取消tab下面的长横线
        tabStrip.setDrawFullUnderline(false);
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(255255255);
        tabStrip.setTextSpacing(200);

        mFragments.add(new Mycaiyuan());
        mFragments.add(new Mycaiyuan());
        mFragments.add(new Mycaiyuan());
        mFragments.add(new Mycaiyuan());
        mFragments.add(new Mycaiyuan());
        titleContainer.add("种植一层");
        titleContainer.add("种植二层");
        titleContainer.add("种植三层");
        titleContainer.add("豆芽机");
        titleContainer.add("蘑菇箱");

        pager.setAdapter(new myPagerAdapter(getSupportFragmentManager(), mFragments, titleContainer));
    }
}