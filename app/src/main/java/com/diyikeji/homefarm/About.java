package com.diyikeji.homefarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/17.
 */

public class About extends Activity
{
    static public About aboutthis;
    private Message msg;
    private AlertDialog.Builder builder;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    DialogInterface.OnClickListener dialogOnclicListener = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case Dialog.BUTTON_POSITIVE:
                                    Toast.makeText(getApplicationContext(), "开始下载", Toast.LENGTH_SHORT).show();
                                    Login.loginthis.about = true;
                                    Login.loginthis.xizai();
                                    break;
                                case Dialog.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    //dialog参数设置
                    builder = new AlertDialog.Builder(aboutthis);
                    builder.setTitle("提示"); //设置标题
                    builder.setMessage("APP有新的版本是否下载?"); //设置内容
//                    builder.setIcon(R.drawable.logo_144);//设置图标，图片id即可
                    builder.setPositiveButton("确认", dialogOnclicListener);
                    builder.setNegativeButton("取消", dialogOnclicListener);
                    //如果自动登录到MainActivity就会报错,所以拦截
                    try
                    {
                        builder.create().show();
                    }
                    catch (Exception e) {}
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "连接服务器失败,请检查网络", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        aboutthis = this;
        super.onCreate(savedInstanceState);
        setContentView(com.diyikeji.homefarm.R.layout.guanyu);

        ImageButton gengxing = (ImageButton) findViewById(com.diyikeji.homefarm.R.id.gengxing);

        TextView text1 = (TextView)findViewById(com.diyikeji.homefarm.R.id.text1);
        final TextView text2 = (TextView)findViewById(com.diyikeji.homefarm.R.id.text2);
        TextView text3 = (TextView)findViewById(com.diyikeji.homefarm.R.id.text3);
        final TextView text4 = (TextView)findViewById(com.diyikeji.homefarm.R.id.text4);
        TextView text5 = (TextView)findViewById(com.diyikeji.homefarm.R.id.text5);
        final TextView text6 = (TextView)findViewById(com.diyikeji.homefarm.R.id.text6);


        gengxing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                OkHttpClient mOkHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(MainActivity.mainActivitythis.url + "banben.txt").build();

                mOkHttpClient.newCall(request).enqueue(new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {
                        Log.d("h_bl", "检查更新失败");
                        msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {
                        String resstr = response.body().string();
                        Log.i("jieshou", resstr);
                        if (Integer.parseInt(resstr) > Login.loginthis.banben)
                        {
                            msg = Message.obtain();
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }
                    }
                });
            }
        });

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (text2.getVisibility() == View.GONE)
                {
                    text2.setVisibility(View.VISIBLE);
                }else
                {
                    text2.setVisibility(View.GONE);
                }
            }
        });
        text2.setMovementMethod(ScrollingMovementMethod.getInstance());

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (text4.getVisibility() == View.GONE)
                {
                    text4.setVisibility(View.VISIBLE);
                }else
                {
                    text4.setVisibility(View.GONE);
                }
            }
        });
        text4.setMovementMethod(ScrollingMovementMethod.getInstance());

        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (text6.getVisibility() == View.GONE)
                {
                    text6.setVisibility(View.VISIBLE);
                }else
                {
                    text6.setVisibility(View.GONE);
                }
            }
        });
        text6.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
