package hanwenjiaoyu.homefarm;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

/**
 * Created by lyxsh on 2016/11/18.
 */
public class FloatWindowService extends Service
{
    public static FloatWindowService floatWindowServicethis;
    private String EQID,EQIDMD5;

    View view;
    public WindowManager windowManager;
    public WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    FloatView floatView;

    @Override
    public void onCreate()
    {
        super.onCreate();
        view = LayoutInflater.from(this).inflate(R.layout.window, null);

        floatWindowServicethis = this;

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

        floatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ActivityManager activityManager = (ActivityManager) getApplication().getSystemService(getApplication().ACTIVITY_SERVICE);
                List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
                if (tasksInfo.size() > 0) {
                    // 应用程序位于堆栈的顶层
                    if ("hanwenjiaoyu.homefarm".equals(tasksInfo.get(0).topActivity.getPackageName()))
                    {
                    }else
                    {
                        Intent mainIntent = getPackageManager().getLaunchIntentForPackage("hanwenjiaoyu.homefarm");
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
        if (intent.getStringExtra("EQID")== null)
        {
            return null;
        }
        return null;
    }
}