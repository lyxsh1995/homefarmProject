package hanwenjiaoyu.homefarm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import bean.Sqlite;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/22.
 */

public class Login extends Activity
{
    //版本号
    int banben = 1;

    public static Login loginthis;

    public Sqlite sqlite;
    public SQLiteDatabase db;
    private Cursor cursor;

    public String EQID,EQIDMD5;
    private LinearLayout login_layout;

    private Message msg;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //提示更新
                case 0:
                    DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch(which){
                                case Dialog.BUTTON_POSITIVE:
                                    Toast.makeText(getApplicationContext(), "开始下载", Toast.LENGTH_SHORT).show();
                                    xizai();
                                    break;
                                case Dialog.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    //dialog参数设置
                    AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);  //先得到构造器
                    builder.setTitle("提示"); //设置标题
                    builder.setMessage("有新的版本是否下载?"); //设置内容
//                    builder.setIcon(R.drawable.logo_144);//设置图标，图片id即可
                    builder.setPositiveButton("确认",dialogOnclicListener);
                    builder.setNegativeButton("取消", dialogOnclicListener);
                    builder.create().show();
                    break;
                //下载进度更新
                case 1:
                    int progress = msg.arg1;
//                    mProgressBar.setProgress(progress);
                    break;
                default:
                    break;
                case 2:
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/homefarm/", "homefarm.apk")),
                                          "application/vnd.android.package-archive");
                    startActivity(intent);
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(),"更新文件下载失败",Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private String sdPath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginthis = this;

        //创建数据库
        sqlite = new Sqlite(getApplication(), "homefarm", null, 1);
        db = sqlite.getWritableDatabase();
        //读取EQID
        String sqlstr = "select * from xinxi where _id = 1";
        cursor = db.rawQuery(sqlstr,null);
        if(cursor.getCount() == 1)
        {
            cursor.move(1);
            EQID = cursor.getString(1);
            EQIDMD5 = cursor.getString(2);
        }
        if (EQID != null)
        {
            Intent intent = new Intent(Login.this,MainActivity.class);
            intent.putExtra("EQID", EQID);
            intent.putExtra("EQIDMD5", EQIDMD5);
            startActivity(intent);
            finish();
        }

        //做动画的layout容器
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        //临时登录按钮,以后删除
        Button denglu = (Button) findViewById(R.id.denglu);
        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this,MainActivity.class);
                intent.putExtra("EQID", "1111111111");
                intent.putExtra("EQIDMD5", "737207bfff986b451956db85a7c8d380");
                Login.loginthis.db.execSQL("update xinxi set EQID = '1111111111', EQIDMD5 = '737207bfff986b451956db85a7c8d380' where _id =1");
                startActivity(intent);
                finish();
            }
        });

        Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "扫描二维码", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 23) {
                    ActivityCompat.requestPermissions(Login.this,new String[]{Manifest.permission.CAMERA}, 4);
                }else
                {
                    Intent intent = new Intent(Login.this,com.dtr.zxing.activity.CaptureActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button wifi_button = (Button) findViewById(R.id.wifi_button);
        wifi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this,com.rtk.simpleconfig_wizard.MainActivity.class);
                startActivity(intent);
            }
        });
        gengxing();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        donghua();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 4) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Login.this,com.dtr.zxing.activity.CaptureActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"未成功获取权限,请手动打开",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void donghua()
    {
//        AlphaAnimation myAnimation_Alpha;
//        myAnimation_Alpha=new AlphaAnimation(0.1f, 1.0f);
//        myAnimation_Alpha.setDuration(5000);
        Animation myAnimation= AnimationUtils.loadAnimation(this, R.anim.alpha);
        login_layout.setAnimation(myAnimation);
    }

    public void gengxing()
    {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.1.100:8011/banben.txt").build();

        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                Log.d("h_bl", "检查更新失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String resstr = response.body().string();
                Log.i("jieshou", resstr);
                if (Integer.parseInt(resstr)>banben)
                {
                    msg = Message.obtain();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void xizai()
    {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.1.100:8011/apk.apk").build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e)
            {
                Log.d("h_bl", "onFailure");
                msg = Message.obtain();
                msg.what = 3;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                sdPath = Environment.getExternalStorageDirectory() + "/homefarm/";
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(sdPath, "homefarm.apk");
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("h_bl", "progress=" + progress);
                        msg = Message.obtain();
                        msg.what = 1;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                    fos.flush();
                    Log.d("h_bl", "文件下载成功");
                    msg = Message.obtain();
                    msg.what = 2;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Log.d("h_bl", "文件下载失败");
                    msg = Message.obtain();
                    msg.what = 3;
                    handler.sendMessage(msg);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }
}
