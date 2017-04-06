package hanwenjiaoyu.homefarm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import bean.Caiyuanjson;
import bean.ColorAnimationView;
import bean.MD5;
import bean.myPagerAdapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/3.
 */

//V4包需要FragmentActivity
public class Caiyuan extends FragmentActivity
{
    public static Caiyuan caiyuanthis;

    public Response response;
    public OkHttpClient mOkHttpClient = new OkHttpClient();
    private Request request;
    private RequestBody requestBody;

    public List<Caiyuanjson> caiyuanlist = new ArrayList<Caiyuanjson>();
    Gson gson = new Gson();
    Message msg = new Message();

    PagerAdapter f;

    public ViewPager pager;
    private PagerTabStrip tabStrip;
    //第几页
    public int pagerposition=0;
//    //初始化第几页
//    public int pagercreate = 0;

    java.util.List<Fragment> mFragments = new ArrayList<Fragment>();
    List<String> titleContainer = new ArrayList<String>();
    private ColorAnimationView colorAnimationView;


    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    for (Caiyuanjson cy : caiyuanlist)
                    {
                        switch (cy.getCengshu().toString())
                        {
                            case "0":
                                Toast.makeText(getApplicationContext(),"这是第一层",Toast.LENGTH_SHORT).show();
                                break;
                            case "1":
                                Toast.makeText(getApplicationContext(),"这是第二层",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caiyuan);
        caiyuanthis = this;

        pager = (ViewPager) findViewById(R.id.viewpager1);
        tabStrip = (PagerTabStrip) findViewById(R.id.tabstrip1);
        colorAnimationView = (ColorAnimationView) findViewById(R.id.ColorAnimationView);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                pagerposition = position;
                //已选择方案显示
                    try
                    {
                        String cengshu = "";
                        for (int i = 0;i<Caiyuan.caiyuanthis.caiyuanlist.size();i++)
                        {
                            cengshu = Caiyuan.caiyuanthis.caiyuanlist.get(i).getCengshu();
                            if (cengshu.equals(position+""))
                            {
                                f = Caiyuan.caiyuanthis.pager.getAdapter();
                                Mycaiyuan mycaiyuan = (Mycaiyuan) f.instantiateItem(Caiyuan.caiyuanthis.pager, position);
                                Caiyuanjson info = Caiyuan.caiyuanthis.caiyuanlist.get(i);
                                mycaiyuan.mycaiyuanthis.zhixingzhuangtai.setText("正在执行");
                                mycaiyuan.mycaiyuanthis.mingcheng.setText(info.getPingzhong());
                                mycaiyuan.mycaiyuanthis.shouhuoshijian.setText(info.getJieshu());
                                mycaiyuan.mycaiyuanthis.jianjie.setText(info.getJianjie());
                                mycaiyuan.mycaiyuanthis.daorulayout.setVisibility(View.GONE);
                                mycaiyuan.mycaiyuanthis.shanchulayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("我的菜园已导入方案", position + "层没有已导入方案");
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        //去读已导入的方案
        String sqlstr = "SELECT * FROM fanganbiao where EQID = '" + MainActivity.mainActivitythis.EQID + "' and zhuangtai = 1";
        requestBody = new FormBody.Builder()
                .add("fangfa", "chaxun")
                .add("EQID", MainActivity.mainActivitythis.EQID)
                .add("EQIDMD5", MD5.jiami(MainActivity.mainActivitythis.EQID))
                .add("sqlstr", sqlstr)
                .build();

        request = new Request.Builder()
                .url(Login.loginthis.url)
                .post(requestBody)
                .build();

        response = null;

        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                Log.e("jieshou1", "testHttpPost ... onFailure() e=" + e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try
                {
                    if (response.isSuccessful())
                    {
                        String resstr = response.body().string();
                        Log.i("jieshou", resstr);
                        Type type = new TypeToken<List<Caiyuanjson>>() {}.getType();
                        caiyuanlist = gson.fromJson(resstr, type);
                        msg = Message.obtain();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        addviewpager();
    }

    private void addviewpager()
    {
        //取消tab下面的长横线
        tabStrip.setDrawFullUnderline(false);
        //设置当前tab页签的下划线颜色
        tabStrip.setTabIndicatorColor(0xccffffff);
        //文字颜色
        tabStrip.setTextColor(0xddffffff);
        tabStrip.setTextSpacing(200);
        //背景色
        tabStrip.setBackgroundColor(0x00000000);

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

        colorAnimationView.setmViewPager(pager,5);
    }
}
