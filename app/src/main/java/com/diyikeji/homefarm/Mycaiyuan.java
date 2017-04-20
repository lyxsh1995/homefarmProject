package com.diyikeji.homefarm;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;

import bean.Caiyuanjson;
import bean.MD5;
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

public class Mycaiyuan extends Fragment
{
    View view;
    public Mycaiyuan mycaiyuanthis;
    public TextView mingcheng,jianjie,zhixingzhuangtai,shouhuoshijian;
    public LinearLayout shanchulayout,daorulayout;

    PagerAdapter f;
    public Button quedingdaoru, shanchufangan;

    Message msg = new Message();
    public Response response;
    public OkHttpClient mOkHttpClient = new OkHttpClient();
    private Request request;
    private RequestBody requestBody;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                //点击确定按钮后回调
                case  0:
                    if (msg.obj.equals("ok"))
                    {
                        Toast.makeText(getContext(),"方案导入成功",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(),"方案导入失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if (msg.obj.equals("ok"))
                    {
                        Toast.makeText(getContext(),"方案删除成功",Toast.LENGTH_SHORT).show();
                        shanchulayout.setVisibility(View.GONE);
                        daorulayout.setVisibility(View.VISIBLE);
                        Caiyuan.caiyuanthis.caiyuanlist.clear();
                        zhixingzhuangtai.setText("方案删除成功!");
                    }
                    else
                    {
                        Toast.makeText(getContext(),"方案删除失败",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mycaiyuanthis = this;

        if (view == null)
        {
            view = inflater.inflate(com.diyikeji.homefarm.R.layout.mycaiyuan, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
        {
            parent.removeView(view);
        }

        shanchulayout = (LinearLayout) view.findViewById(com.diyikeji.homefarm.R.id.shanchulayout);
        daorulayout = (LinearLayout) view.findViewById(com.diyikeji.homefarm.R.id.daorualayout);

        zhixingzhuangtai = (TextView) view.findViewById(com.diyikeji.homefarm.R.id.zhixingzhuangtai);
        mingcheng = (TextView) view.findViewById(com.diyikeji.homefarm.R.id.mingcheng);
        shouhuoshijian = (TextView) view.findViewById(com.diyikeji.homefarm.R.id.shouhuoshijian);
        jianjie = (TextView) view.findViewById(com.diyikeji.homefarm.R.id.jianjie);

        //第0层方案导入
        new Thread()
        {
            @Override
            public void run()
            {
                if (Caiyuan.caiyuanthis.pagerposition == 0 && Caiyuan.caiyuanthis.f == null)
                {
                    try
                    {
                        String cengshu = "";
                        while (Caiyuan.caiyuanthis.caiyuanlist == null)
                        {
                            Thread.sleep(100);
                        }
                        for (int i = 0;i<Caiyuan.caiyuanthis.caiyuanlist.size();i++)
                        {
                            cengshu = Caiyuan.caiyuanthis.caiyuanlist.get(i).getCengshu();
                            if (cengshu.equals(Caiyuan.caiyuanthis.pagerposition+""))
                            {
                                f = Caiyuan.caiyuanthis.pager.getAdapter();
                                Mycaiyuan mycaiyuan = (Mycaiyuan) f.instantiateItem(Caiyuan.caiyuanthis.pager, 0);
                                Caiyuanjson info = Caiyuan.caiyuanthis.caiyuanlist.get(i);

                                mycaiyuan.mycaiyuanthis.zhixingzhuangtai.setText("正在执行");
                                mycaiyuan.mycaiyuanthis.mingcheng.setText(info.getPingzhong());
                                mycaiyuan.mycaiyuanthis.shouhuoshijian.setText(info.getJieshu());
                                mycaiyuan.mycaiyuanthis.jianjie.setText(info.getJianjie());

                                daorulayout.setVisibility(View.GONE);
                                shanchulayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    catch (Exception e)
                    {
                        Log.e("我的菜园已导入方案", 0 + "层没有已导入方案");
                    }
                }
            }
        }.start();


        Button daoru = (Button) view.findViewById(com.diyikeji.homefarm.R.id.daoru);
        daoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),Fangan.class);
                startActivity(intent);
            }
        });

        quedingdaoru = (Button) view.findViewById(com.diyikeji.homefarm.R.id.quedingdaoru);
        quedingdaoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //没选择方案会直接报错
                try
                {
                requestBody = new FormBody.Builder()
                        .add("fangfa", "fangan")
                        .add("EQID", MainActivity.mainActivitythis.EQID)
                        .add("EQIDMD5", MD5.jiami( MainActivity.mainActivitythis.EQID))
                        .add("fanganid", Fangan.fanganthis.fanganid)
                        .add("cengshu",Caiyuan.caiyuanthis.pagerposition + "")
                        .build();
                }
                catch (Exception e)
                {
                    Toast.makeText(getContext(),"请先选择方案",Toast.LENGTH_SHORT).show();
                    return;
                }
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
                                msg = Message.obtain();
                                msg.obj = resstr;
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
            }
        });

        shanchufangan = (Button) view.findViewById(com.diyikeji.homefarm.R.id.shanchufangan);
        shanchufangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                requestBody = new FormBody.Builder()
                        .add("fangfa", "shanchufangan")
                        .add("EQID", MainActivity.mainActivitythis.EQID)
                        .add("EQIDMD5", MD5.jiami( MainActivity.mainActivitythis.EQID))
                        .add("cengshu",Caiyuan.caiyuanthis.pagerposition + "")
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
                                msg = Message.obtain();
                                msg.obj = resstr;
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return view;
    }
}
