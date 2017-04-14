package bean;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import diyikeji.homefarm.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Administrator on 2017/4/8.
 */

public class Tongzhi
{
    public Tongzhi(Context context,int notifyid,String text,String time)
    {
        Intent creatIntent = new Intent(context.getPackageManager().getLaunchIntentForPackage("diyikeji.homefarm"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, creatIntent, 0);

        java.util.Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            date = sdf.parse(time); //转换为util.date
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle("植物状态不好!")//设置通知栏标题
                .setContentText(text) //设置通知栏显示内容
                .setContentIntent(pendingIntent) //设置通知栏点击意图
//                .setNumber(1) //设置通知集合的数量
                .setTicker("城市农场") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(date.getTime())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.mipmap.shishui)
        ;//设置通知小ICON

        mNotificationManager.notify(notifyid, mBuilder.build());
    }
}
