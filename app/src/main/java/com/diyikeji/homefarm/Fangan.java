package com.diyikeji.homefarm;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.Fajson;
import bean.MD5;
import bean.Myadapter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/5.
 */

public class Fangan extends Activity
{
    public static Fangan fanganthis;

    private OkHttpClient mOkHttpClient;
    Request request;
    Gson gson = new Gson();
    Message msg = new Message();
    List<Fajson> rs;
    private RequestBody requestBody;

    private ListView fangan_list;
    private Myadapter adapter;

    //第几行
    int positions = 0;
    public String fanganid;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    adapter = new Myadapter(rs, getApplicationContext());
                    fangan_list.setAdapter(adapter);
                    fanganid = rs.get(0).ID;
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(com.diyikeji.homefarm.R.layout.fangan);
        fanganthis = this;

        fangan_list = (ListView) findViewById(com.diyikeji.homefarm.R.id.fangan_list);
        fangan_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                positions = position;
                fanganid = rs.get(position).ID;
                adapter.select(position);
            }
        });

        //确定按钮按下后
        Button daoru = (Button) findViewById(com.diyikeji.homefarm.R.id.daoru_button);
        daoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                PagerAdapter f =  Caiyuan.caiyuanthis.pager.getAdapter();
                Mycaiyuan mycaiyuan = (Mycaiyuan) f.instantiateItem(Caiyuan.caiyuanthis.pager,Caiyuan.caiyuanthis.pagerposition);
                mycaiyuan.mingcheng.setText(rs.get(positions).pingzhong);
                mycaiyuan.jianjie.setText(rs.get(positions).jianjie);
                mycaiyuan.shouhuoshijian.setText((rs.get(positions).zhouqi));
                mycaiyuan.quedingdaoru.setVisibility(View.VISIBLE);
                finish();
            }
        });

        mOkHttpClient = new OkHttpClient();
        String sqlstr = "SELECT * FROM fangan";
        requestBody = new FormBody.Builder()
                .add("fangfa","chaxun")
                .add("EQID",MainActivity.mainActivitythis.EQID)
                .add("EQIDMD5", MD5.jiami(MainActivity.mainActivitythis.EQID))
                .add("sqlstr",sqlstr)
                .build();
        request = new Request.Builder()
                .url(Login.loginthis.url)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                Log.e("jieshou", "testHttpPost ... onFailure() e=" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try
                {
                    if (response.isSuccessful())
                    {
                        String resstr = response.body().string();
                        Log.i("jieshou",resstr);
                        rs = new ArrayList<Fajson>();
                        java.lang.reflect.Type type = new TypeToken<List<Fajson>>() {}.getType();
                        rs = gson.fromJson(resstr, type);
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
    }
}
