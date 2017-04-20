package com.diyikeji.homefarm;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.List;

import bean.MD5;
import bean.Sqlite;

/**
 * Created by lyxsh on 2016/11/18.
 */
public class FloatWindowService extends Service
{
    public static FloatWindowService floatWindowServicethis;
    private Context context;
    public String EQID, EQIDMD5;

    public Sqlite sqlite;
    public SQLiteDatabase db;
    private Cursor cursor;

    View view;
    public WindowManager windowManager;
    public WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    FloatView floatView;

    @Override
    public void onCreate()
    {
        super.onCreate();
        view = LayoutInflater.from(this).inflate(com.diyikeji.homefarm.R.layout.window, null);

        floatWindowServicethis = this;

        sqlite = new Sqlite(getApplication(), "homefarm", null, 1);
        db = sqlite.getWritableDatabase();
        //读取EQID
        String sqls = "select * from xinxi where _id = 1";
        cursor = db.rawQuery(sqls, null);
        if (cursor.getCount() == 1)
        {
            cursor.move(1);
            EQID = cursor.getString(1);
            EQIDMD5 = MD5.jiami(EQID);
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatView = new FloatView(getApplicationContext());
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.format = PixelFormat.RGBA_8888;
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        params.x = 100;
        params.y = 100;
        windowManager.addView(floatView, params);

        floatView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(getApplication().ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
                if (tasksInfo.size() > 0)
                {
                    // 应用程序位于堆栈的顶层
                    if ("hanwenjiaoyu.homefarm".equals(tasksInfo.get(0).topActivity.getPackageName()))
                    {
                    } else
                    {
                        Intent mainIntent = getPackageManager().getLaunchIntentForPackage("diyikeji.homefarm");
                        startActivity(mainIntent);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy()
    {
        windowManager.removeView(floatView);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        if (intent.getStringExtra("EQID") == null)
        {
            return null;
        }
        return null;
    }
}