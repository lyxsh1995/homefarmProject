package com.diyikeji.homefarm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import bean.Cljson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lyxsh on 2016/11/18.
 */
public class FloatView extends LinearLayout
{
    Context context;

    private float mTouchX;
    private float mTouchY;
    private float x;
    private float y;
    private float mStartX;
    private float mStartY;
    private OnClickListener mClickListener;
    private OnLongClickListener mLongClickListener;
    WindowManager windowManager;
    WindowManager.LayoutParams windowManagerParams;
    long touchTime = 0;

    public static Timer timer;
    Message msg = new Message();

    public String url = "http://iotserver.csnc.cc/app/";
    public Response response;
    public OkHttpClient mOkHttpClient = new OkHttpClient();
    private Request request;
    private RequestBody requestBody;
    Gson gson = new Gson();

    private List<Cljson> rs;
    private TextView wendu_shuju;
    private TextView shidu_shuju;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                //定时更新悬浮窗数据
                case 0:
                    //计算平均值
                    java.text.DecimalFormat df  =new java.text.DecimalFormat("#.00");
                    float a = 0;
                    float b = 0;
                    int c = 0;
                    int d = 0;
                    for (int i = 0; i < rs.size(); i++)
                    {
                        if (rs.get(i).d_lastvalue != 0)
                        {
                            switch (rs.get(i).d_name.substring(6, 7))
                            {
                                //温度
                                case "w":
                                    a += rs.get(i).d_lastvalue;
                                    c++;
                                    break;
                                //湿度
                                case "s":
                                    b += rs.get(i).d_lastvalue;
                                    d++;
                                    break;
                            }
                        }
                    }
                    a /= c;
                    b /= d;

                    //更新UI
                    wendu_shuju.setText(df.format(a));
                    shidu_shuju.setText(df.format(b));

                    if (20 <= a && a <= 40)
                    {
//                        wendu_shuju.setText(wendu_shuju.getText() + " 良好");
                    } else
                    {
//                        wendu_shuju.setText(wendu_shuju.getText() + " 恶劣");
                    }
                    if (20 <= b && b <= 40)
                    {
//                        shidu_shuju.setText(shidu_shuju.getText() + " 良好");
                    } else
                    {
//                        shidu_shuju.setText(shidu_shuju.getText() + " 恶劣");
                    }
                    break;
                case 7:
                    //更新UI
                    Cursor cursor = (Cursor)msg.obj;
                    df = new java.text.DecimalFormat("#.00");
                    a = 0;
                    b = 0;
                    c = 0;
                    d = 0;
                    while (cursor.moveToNext())
                    {
                        if (cursor.getInt(cursor.getColumnIndex("d_lastvalue")) != 0)
                        {
                            switch (cursor.getString(cursor.getColumnIndex("d_name")).substring(6, 7))
                            {
                                //温度
                                case "w":
                                    a += cursor.getInt(cursor.getColumnIndex("d_lastvalue"));
                                    c++;
                                    break;
                                //湿度
                                case "s":
                                    b += cursor.getInt(cursor.getColumnIndex("d_lastvalue"));
                                    d++;
                                    break;
                            }
                        }
                    }
                    a /= c;
                    b /= d;


                    wendu_shuju.setText(df.format(a));
                    shidu_shuju.setText(df.format(b));

//                        if (20 <= a && a <= 40)
//                        {
//                            wendu_shuju.setText(wendu_shuju.getText() + "   良好");
//                        } else
//                        {
//                            wendu_shuju.setText(wendu_shuju.getText() + "   恶劣");
//                        }
//                        if (20 <= b && b <= 40)
//                        {
//                            shidu_shuju.setText(shidu_shuju.getText() + "   良好");
//                        } else
//                        {
//                            shidu_shuju.setText(shidu_shuju.getText() + "   恶劣");
//                        }
                    break;
            }
        }
    };

    public FloatView(Context context)
    {
        super(context);
        try
        {
            this.context = context;
            final boolean tongxingmod = MainActivity.mainActivitythis.udp.tongxingmod;
            //获取浮动窗口视图所在布局
            View view = LayoutInflater.from(context).inflate(
                    com.diyikeji.homefarm.R.layout.xuanfuchuang, null);
            windowManager = FloatWindowService.floatWindowServicethis.windowManager;
            // 此windowManagerParams变量为获取的全局变量，用以保存悬浮窗口的属性
            windowManagerParams = FloatWindowService.floatWindowServicethis.params;
            this.addView(view);

            wendu_shuju = (TextView) view.findViewById(com.diyikeji.homefarm.R.id.wendu_shuju);
            shidu_shuju = (TextView) view.findViewById(com.diyikeji.homefarm.R.id.shidu_shuju);

//            String sqlstr = "SELECT d_name,d_lastvalue FROM device where d_type = 'cl'  and d_status = 1 and EQID = '" + FloatWindowService.floatWindowServicethis.EQID + "' and d_name like 'turangshidu%' or d_name like 'turangwendu%'";
//            requestBody = new FormBody.Builder()
//                    .add("fangfa", "chaxun")
//                    .add("EQID", FloatWindowService.floatWindowServicethis.EQID)
//                    .add("EQIDMD5", MD5.jiami(FloatWindowService.floatWindowServicethis.EQID))
//                    .add("sqlstr", sqlstr)
//                    .build();
//            request = new Request.Builder()
//                    .url(url)
//                    .post(requestBody)
//                    .build();

//            response = null;

            TimerTask task = new TimerTask()
            {
                public void run()
                {
                    if(!tongxingmod)
                    {
//                        mOkHttpClient.newCall(request).enqueue(new Callback()
//                        {
//                            @Override
//                            public void onFailure(Call call, IOException e)
//                            {
//                                Log.e("jieshou1", "testHttpPost ... onFailure() e=" + e);
//                            }
//
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException
//                            {
//                                try
//                                {
//                                    if (response.isSuccessful())
//                                    {
//                                        String resstr = response.body().string();
//                                        Log.i("jieshou", resstr);
//                                        rs = new ArrayList<Cljson>();
//                                        java.lang.reflect.Type type = new TypeToken<List<Cljson>>() {}.getType();
//                                        rs = gson.fromJson(resstr, type);
//                                        msg = Message.obtain();
//                                        msg.what = 0;
//                                        handler.sendMessage(msg);
//                                    }
//                                }
//                                catch (Exception e)
//                                {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                        String sqlstr = "SELECT d_name,d_lastvalue FROM device where d_type = 'cl' and (d_name like 'turangshidu%' or d_name like 'turangwendu%')";
                        Login.loginthis.cursor = Login.loginthis.db.rawQuery(sqlstr, null);
                        Login.loginthis.cursor.move(1);

                        msg = Message.obtain();
                        msg.what = 7;
                        msg.obj = Login.loginthis.cursor;
                        handler.sendMessage(msg);
                    }
                }
            };
            timer = new Timer(true);
            timer.schedule(task, 0, 10*1000);
        }catch (Exception e)
        {
            Intent intent = new Intent(context, FloatWindowService.class);
            context.stopService(intent);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {

        //获取到状态栏的高度
        Rect frame = new Rect();
        getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
// 获取相对屏幕的坐标，即以屏幕左上角为原点
        x = event.getRawX();
        y = event.getRawY() - statusBarHeight; // statusBarHeight是系统状态栏的高度
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN: // 捕获手指触摸按下动作
// 获取相对View的坐标，即以此View左上角为原点
                mTouchX = event.getX();
                mTouchY = event.getY();
                mStartX = x;
                mStartY = y;
                break;
            case MotionEvent.ACTION_MOVE: // 捕获手指触摸移动动作
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
                updateViewPosition();
                mTouchX = mTouchY = 0;
                if ((x - mStartX) < 5 && (y - mStartY) < 5)
                {
//                    //双击
//                    if ((System.currentTimeMillis() - touchTime) < 1000)
//                    {
//                        //销毁定时器
//                        FloatView.timer.cancel();
//                        //停止服务
//                        FloatWindowService.floatWindowServicethis.stopSelf();
//                        try
//                        {
//                            if (MainActivity.mainActivitythis.kaiguan != null)
//                            {
//                                MainActivity.mainActivitythis.kaiguan.setChecked(false);
//                            }
//                        }catch (Exception e)
//                        {}
//                    }
                    if (mClickListener != null)
                    {
                        mClickListener.onClick(this);
                        //获取触摸时间
                        touchTime = System.currentTimeMillis();
                    }
                }
                break;
        }
        return true;
    }
    @Override
    public void setOnClickListener(OnClickListener l)
    {
        this.mClickListener = l;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l)
    {
        this.mLongClickListener = l;
    }

    private void updateViewPosition()
    {
// 更新浮动窗口位置参数
        windowManagerParams.x = (int) (x - mTouchX);
        windowManagerParams.y = (int) (y - mTouchY);
        windowManager.updateViewLayout(this, windowManagerParams); // 刷新显示
    }
}