package com.diyikeji.homefarm;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bean.DEjson;
import bean.MD5;
import bean.Sqlite;
import bean.Tongzhi;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/7.
 */

public class HoutaiService extends Service
{
    static public HoutaiService HoutaiServicethis;
    private List<DEjson> delist;
    Gson gson = new Gson();

    Response response;
    OkHttpClient mOkHttpClient = new OkHttpClient();
    Request request;
    RequestBody requestBody;

    String EQID;
    String url = "http://iotserver.csnc.cc/app/";

    Sqlite sqlite;
    SQLiteDatabase db;
    Cursor cursor;

    TimerTask task;
    Timer timer;

    @Override
    public void onCreate()
    {
        super.onCreate();
        HoutaiServicethis = this;
        sqlite = new Sqlite(getApplication(), "homefarm", null, 1);
        db = sqlite.getWritableDatabase();

        //读取EQID
        String sqls = "select * from xinxi where _id = 1";
        cursor = db.rawQuery(sqls, null);
        if (cursor.getCount() == 1)
        {
            cursor.move(1);
            EQID = cursor.getString(1);
        }

        task = new TimerTask()
        {
            @Override
            public void run()
            {
                getshebei();
            }
        };
        timer = new Timer(true);
        timer.schedule(task, 0, 3 * 1000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void getshebei()
    {
        Cursor cursor = db.rawQuery("select TIME from xinxi", null);
        cursor.move(1);
        long date = cursor.getLong(0);
        Log.e("时间", String.valueOf(date));
        String sqlstr = "SELECT * FROM dataexchangeshebei where EQID = '" + EQID + "' and exchTime>" + date +" limit 100";
        requestBody = new FormBody.Builder()
                .add("fangfa", "chaxun")
                .add("EQID", EQID)
                .add("EQIDMD5", MD5.jiami(EQID))
                .add("sqlstr", sqlstr)
                .build();
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

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
                        Log.i("getshebei", resstr);
                        delist = new ArrayList<DEjson>();
                        Type type = new TypeToken<List<DEjson>>() {}.getType();
                        delist = gson.fromJson(resstr, type);
                        //更新时间戳
                        db.execSQL("update xinxi set TIME = " + delist.get(delist.size() - 1).getExchTime() + " where _id = 1");
                        for (DEjson a : delist)
                        {
                            try
                            {
                                db.execSQL(a.getSQLString());
                            }catch (Exception e)
                            {}
                        }
                    }
                }
                catch (com.google.gson.JsonSyntaxException e)
                {
//                    e.printStackTrace();
                }
            }
        });

        try
        {
            String sqls = "select case when (Fnote='ts<17') then '土壤湿度小于17%，请及时补水！' else '您的植物状态不好!' end,Ftime from pushmsg where Ftype='2043'";
            cursor = db.rawQuery(sqls, null);
            int i = 0;
            while (cursor.moveToNext())
            {
                i++;
                Tongzhi tongzhi = new Tongzhi(getApplicationContext(), i++, cursor.getString(0), cursor.getString(1));
                db.execSQL("DELETE FROM pushmsg where Ftime = '"+cursor.getString(1)+"'");
            }
        }catch (Exception e){}
    }
}
