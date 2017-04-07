package diyikeji.homefarm;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;

import bean.MD5;
import bean.Sqlite;
import bean.UDP;
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
    //APP版本号
    int banben = 1;
    //服务器地址
    public String url = "http://192.168.0.104:80/app/";

    //服务器设备版本
    int edatabanben = 0;
    int homefarmbanben = 0;
    private int shebeiedata;
    private int shebeihomefarm;

    public static Login loginthis;

    private long exitTime = 0;

    public Sqlite sqlite;
    public SQLiteDatabase db;
    public Cursor cursor;

    public String EQID, EQIDMD5;
    private LinearLayout login_layout;

    private IntentFilter filter;
    private NetworkConnectChangedReceiver guangbo;

    private Message msg;
    boolean showflag = true;//更新窗口是否显示,防止多次显示
    public UDP udp;

    private String sdPath;
    private AlertDialog.Builder builder;
    private ProgressBar progressBar;


    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                //提示更新
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
                                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                                    progressBar.setVisibility(View.VISIBLE);
                                    xizai();
                                    break;
                                case Dialog.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    //dialog参数设置

                    //判断ACTIVITY是否会跳转
                    if (EQID != null)
                    {//跳转到主界面
                        //先得到构造器
                        builder = new AlertDialog.Builder(MainActivity.mainActivitythis);
                    } else
                    {
                        builder = new AlertDialog.Builder(Login.this);
                    }
                    builder.setTitle("提示"); //设置标题
                    builder.setMessage("APP有新的版本是否下载?"); //设置内容
//                    builder.setIcon(R.drawable.logo_144);//设置图标，图片id即可
                    builder.setPositiveButton("确认", dialogOnclicListener);
                    builder.setNegativeButton("取消", dialogOnclicListener);
                    //如果自动登录到MainActivity就会报错,所以拦截
                    try
                    {
                        if (showflag)
                        {
                            builder.create().show();
                            showflag = false;
                            try
                            {
                                unregisterReceiver(guangbo);
                            }
                            catch (Exception e) {}
                        }
                    }
                    catch (Exception e) {}
                    break;
                //下载进度更新
                case 1:
                    int progress = msg.arg1;
                    progressBar.setProgress(progress);
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
                    Toast.makeText(getApplicationContext(), "更新文件下载失败", Toast.LENGTH_SHORT).show();
                    break;
                //连接不到服务器是时给提示
                case 4:
                    Toast.makeText(getApplicationContext(), "连接服务器失败,请检查网络", Toast.LENGTH_SHORT).show();
                    //注册广播
                    if (EQID == null) //如果EQID有值就不在Login界面
                    {
                        guangbo = new NetworkConnectChangedReceiver();
                        filter = new IntentFilter();
                        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
                        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
                        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                        registerReceiver(guangbo, filter);
                    }
                    break;
                case 5:
                    //检查设备更新
                    DialogInterface.OnClickListener dialogOnclicListener1 = new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which)
                            {
                                case Dialog.BUTTON_POSITIVE:
                                    Toast.makeText(getApplicationContext(), "开始下载设备更新文件", Toast.LENGTH_SHORT).show();
                                    progressBar = (ProgressBar) findViewById(R.id.progressBar);
                                    progressBar.setVisibility(View.VISIBLE);
                                    shebeixizai();
                                    break;
                                case Dialog.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    //dialog参数设置

                    //判断ACTIVITY是否会跳转
                    if (EQID != null)
                    {//跳转到主界面
                        //先得到构造器
                        builder = new AlertDialog.Builder(MainActivity.mainActivitythis);
                    } else
                    {
                        builder = new AlertDialog.Builder(Login.this);
                    }
                    builder.setTitle("提示"); //设置标题
                    builder.setMessage("设备有新的版本是否更新?"); //设置内容
//                    builder.setIcon(R.drawable.logo_144);//设置图标，图片id即可
                    builder.setPositiveButton("确认", dialogOnclicListener1);
                    builder.setNegativeButton("取消", dialogOnclicListener1);
                    //如果自动登录到MainActivity就会报错,所以拦截
                    try
                    {
                        if (showflag)
                        {
                            builder.create().show();
                            showflag = false;
                            try
                            {
                                unregisterReceiver(guangbo);
                            }
                            catch (Exception e) {}
                        }
                    }
                    catch (Exception e) {}
                    break;
                case 6:
                    String jieguo = "更新失败,请稍后重试";
                    //开始安装设备更新
//                    if (edatabanben > shebeiedata)
                    {
                        try
                        {
                            final File edata = new File(sdPath, "edata");

//                        开始进入离线模式
                            new AsyncTask<String, Void, Boolean>()
                            {
                                @Override
                                protected Boolean doInBackground(String... params)
                                {
                                    String receivedata = "";
                                    for (int i = 0; i < 10; i++)
                                    {
                                        try
                                        {
                                            udp.UDPsend(params[0], false);
                                            receivedata = udp.UDPreceive(false);
                                            udp.wifiad = receivedata.substring(0, receivedata.indexOf(","));
                                            break;
                                        }
                                        catch (Exception e) {}
                                    }
                                    try
                                    {
                                        udp.UDPsend(params[1], false);
                                        if (udp.UDPreceive(false).contains("+ok"))
                                        {
                                            udp.UDPsend(params[2], false);
                                            if (udp.UDPreceive(false).contains("+ok"))
                                            {
                                                udp.UDPsend(params[3], false);
                                                udp.UDPsend("UDP:Redata"+edatabanben+"#" + edata.length(), true);
                                                int ii = 0;
                                                while (!udp.UDPreceive(true).contains("ok"))
                                                {
                                                    udp.UDPsend("UDP:Redata"+edatabanben+"#" + edata.length(), true);
                                                    ii++;
                                                    if (ii >= 3)
                                                    {
                                                        return false;
                                                    }
                                                }
                                                return true;
                                            }
                                        }
                                    }
                                    catch (Exception e) {}
                                    return false;
                                }

                                @Override
                                protected void onPostExecute(Boolean b)
                                {
                                    if (b)
                                    {
                                        Toast.makeText(getApplicationContext(), "进入离线模式成功", Toast.LENGTH_SHORT).show();
                                        udp.tongxingmod = true;

                                        new Thread()
                                        {
                                            @Override
                                            public void run()
                                            {
                                                try
                                                {
                                                    int shibaicishu = 0;//更新文件接收失败次数
                                                    FileInputStream inputStream = new FileInputStream(edata);
                                                    byte[] bytes = new byte[1024];
                                                    while ((inputStream.read(bytes)) != -1)
                                                    {
                                                        DatagramPacket p = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(udp.wifiad), udp.port);
                                                        udp.datagramSocket.send(p);
                                                        Log.e("data数据",new String(bytes));
                                                        bytes = new byte[1024];
                                                        Thread.sleep(100);
                                                    }
                                                    if (udp.UDPreceive(true).contains("ok"))
                                                    {
                                                        Log.w("edata发送成功","");
//                                                        //文件发送成功
//                                                        Toast.makeText(getApplicationContext(), "设备正在更新", Toast.LENGTH_SHORT).show();
                                                        final File homefarm = new File(sdPath, "homefarm");
                                                        Thread.sleep(1000);
                                                        udp.UDPsend("UDP:Rhomefarm"+ homefarmbanben +"#" + homefarm.length(), true);
                                                        if (udp.UDPreceive(true).contains("ok"))
                                                        {
                                                            try
                                                            {
                                                                shibaicishu = 0;//更新文件接收失败次数
                                                                inputStream = new FileInputStream(homefarm);
                                                                bytes = new byte[1024];
                                                                while ((inputStream.read(bytes)) != -1)
                                                                {
                                                                    DatagramPacket p = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(udp.wifiad), udp.port);
                                                                    udp.datagramSocket.send(p);
                                                                    Log.e("homefarm数据",new String(bytes));
                                                                    bytes = new byte[1024];
                                                                    Thread.sleep(100);
                                                                }
                                                                if (udp.UDPreceive(true).contains("ok"))
                                                                {
                                                                    Log.w("homefarm发送成功","");
                                                                    //文件发送成功
//                                                                Toast.makeText(getApplicationContext(), "设备正在更新", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            catch (IOException e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
//                                                    else if (udp.UDPreceive(true).contains("no"))
//                                                    {
//                                                        if (shibaicishu > 3)
//                                                        {
//                                                            return;
//                                                        }
//                                                        shibaicishu++;
//                                                        while ((inputStream.read(bytes)) != -1)
//                                                        {
//                                                            final DatagramPacket p = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(udp.wifiad), udp.port);
//                                                            udp.datagramSocket.send(p);
//                                                        }
//                                                    }
                                                }
                                                catch (Exception e)
                                                {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                    } else
                                    {
                                        Toast.makeText(getApplicationContext(), "进入离线模式失败,请稍后再尝试更新", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }.execute("csncat@yzr", "AT+NETP=UDP,SERVER," + udp.port + "," + udp.myip + "\r", "AT+TMODE=throughput\r", "AT+Z\r");
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    //else
                    {

////                        开始进入离线模式
//                            new AsyncTask<String, Void, Boolean>()
//                            {
//                                @Override
//                                protected Boolean doInBackground(String... params)
//                                {
//                                    String receivedata = "";
//                                    for (int i = 0; i < 10; i++)
//                                    {
//                                        try
//                                        {
//                                            udp.UDPsend(params[0], false);
//                                            receivedata = udp.UDPreceive(false);
//                                            udp.wifiad = receivedata.substring(0, receivedata.indexOf(","));
//                                            break;
//                                        }
//                                        catch (Exception e) {}
//                                    }
//                                    try
//                                    {
//                                        udp.UDPsend(params[1], false);
//                                        if (udp.UDPreceive(false).contains("+ok"))
//                                        {
//                                            udp.UDPsend(params[2], false);
//                                            if (udp.UDPreceive(false).contains("+ok"))
//                                            {
//                                                udp.UDPsend(params[3], false);
//                                                udp.UDPsend("UDP:Rhomefarm"+homefarmbanben +"#"+ lzg.length(), true);
//                                                int ii = 0;
//                                                while (!udp.UDPreceive(true).contains("ok"))
//                                                {
//                                                    udp.UDPsend("UDP:Rhomefarm"+homefarmbanben + "#"+lzg.length(), true);
//                                                    ii++;
//                                                    if (ii >= 3)
//                                                    {
//                                                        return false;
//                                                    }
//                                                }
//                                                return true;
//                                            }
//                                        }
//                                    }
//                                    catch (Exception e) {}
//                                    return false;
//                                }
//
//                                @Override
//                                protected void onPostExecute(Boolean b)
//                                {
//                                    if (b)
//                                    {
//                                        Toast.makeText(getApplicationContext(), "进入离线模式成功", Toast.LENGTH_SHORT).show();
//                                        udp.tongxingmod = true;
//                                    } else
//                                    {
//                                        Toast.makeText(getApplicationContext(), "进入离线模式失败,请稍后再尝试更新", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            }.execute("csncat@yzr", "AT+NETP=UDP,SERVER," + udp.port + "," + udp.myip + "\r", "AT+TMODE=throughput\r", "AT+Z\r");
                    }
//                    if (edatabanben > shebeiedata && homefarmbanben > shebeihomefarm)
//                    {
//                        new Thread()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                try
//                                {
//                                    final File lzg = new File(sdPath, "homefarm");
//                                    udp.UDPsend("UDP:Rhomefarm" + homefarmbanben + "#" + lzg.length(), true);
//                                    if (udp.UDPreceive(true).contains("ok"))
//                                    {
//                                        int shibaicishu = 0;//更新文件接收失败次数
//                                        FileInputStream inputStream = new FileInputStream(lzg);
//                                        byte[] bytes = new byte[1024];
//                                        while ((inputStream.read(bytes)) != -1)
//                                        {
//                                            final DatagramPacket p = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(udp.wifiad), udp.port);
//                                            udp.datagramSocket.send(p);
//                                        }
//                                        if (udp.UDPreceive(true).contains("ok"))
//                                        {
//                                            //文件发送成功
//                                            Toast.makeText(getApplicationContext(), "设备正在更新", Toast.LENGTH_SHORT).show();
//                                        } else if (udp.UDPreceive(true).contains("no"))
//                                        {
//                                            if (shibaicishu > 3)
//                                            {
//                                                return;
//                                            }
//                                            shibaicishu++;
//                                            while ((inputStream.read(bytes)) != -1)
//                                            {
//                                                DatagramPacket p = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(udp.wifiad), udp.port);
//                                                udp.datagramSocket.send(p);
//                                            }
//                                        }
//                                    }
//                                }
//                                catch (IOException e)
//                                {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }.start();
//                    }
                    break;
                case 7:

                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginthis = this;

        udp = new UDP(getApplicationContext());

        //创建数据库
        sqlite = new Sqlite(getApplication(), "homefarm", null, 1);
        db = sqlite.getWritableDatabase();
        //读取EQID
        String sqlstr = "select * from xinxi where _id = 1";
        cursor = db.rawQuery(sqlstr, null);
        if (cursor.getCount() == 1)
        {
            cursor.move(1);
            EQID = cursor.getString(1);
            EQIDMD5 = MD5.jiami(EQID);
        }
        if (EQID != null)
        {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra("EQID", EQID);
            startActivity(intent);
            finish();
        }

        //做动画的layout容器
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        //临时登录按钮,以后删除!!!!!!!!!!!!!!!!!!!!
        Button denglu = (Button) findViewById(R.id.denglu);
        denglu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("EQID", "d8b04cb5c20e");
                intent.putExtra("EQIDMD5", MD5.jiami("d8b04cb5c20e"));
                db.execSQL("update xinxi set EQID = 'd8b04cb5c20e' where _id =1");
                startActivity(intent);
                finish();
            }
        });

        Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "扫描二维码", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 23)
                {
                    ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.CAMERA}, 4);
                } else
                {
                    Intent intent = new Intent(Login.this, com.dtr.zxing.activity.CaptureActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button wifi_button = (Button) findViewById(R.id.wifi_button);
        wifi_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this, com.wifi.connection.MainActivity.class);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 4)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Login.this, com.dtr.zxing.activity.CaptureActivity.class);
                startActivity(intent);
                finish();
            } else
            {
                Toast.makeText(getApplicationContext(), "未成功获取权限,请手动打开", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void donghua()
    {
//        AlphaAnimation myAnimation_Alpha;
//        myAnimation_Alpha=new AlphaAnimation(0.1f, 1.0f);
//        myAnimation_Alpha.setDuration(5000);
        Animation myAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        login_layout.setAnimation(myAnimation);
    }

    public void gengxing()
    {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url + "banben.txt").build();

        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                Log.d("h_bl", "检查更新失败");
                msg = Message.obtain();
                msg.what = 4;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String resstr = response.body().string();
                Log.i("jieshou", resstr);
                if (Integer.parseInt(resstr) > banben)
                {
                    msg = Message.obtain();
                    msg.what = 0;
                    handler.sendMessage(msg);
                } else
                {
                    //开始检查设备更新
                    shebeigengxin();
                }
            }
        });
    }

    public void shebeigengxin()
    {
        shebeiedata = 0;
        shebeihomefarm = 0;
//        shebeiedata = Login.loginthis.db.rawQuery("sqlstr")
//        shebeihomefarm = Login.loginthis.db.rawQuery("sqlstr")
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url + "shebeibanben.txt").build();

        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                Log.d("h_bl2", "检查更新失败");
                msg = Message.obtain();
                msg.what = 4;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                String resstr = response.body().string();
                Log.i("jieshou", resstr);
                edatabanben = Integer.parseInt(resstr.substring(0, resstr.indexOf("|")));
                homefarmbanben = Integer.parseInt(resstr.substring(resstr.indexOf("|") + 1));
                if (edatabanben > shebeiedata || homefarmbanben > shebeihomefarm)
                {
                    msg = Message.obtain();
                    msg.what = 5;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void xizai()
    {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url + "apk.apk").build();
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {

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

                if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
                {
                    sdPath = Environment.getExternalStorageDirectory() + "/homefarm/";
                } else
                {
                    sdPath = getFilesDir().getPath() + "/homefarm/";
                }

                try
                {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(sdPath, "apk.apk");
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1)
                    {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("apk", "progress=" + progress);
                        msg = Message.obtain();
                        msg.what = 1;
                        msg.arg1 = progress;
                        handler.sendMessage(msg);
                    }
                    fos.flush();
                    Log.d("apk", "文件下载成功");
                    msg = Message.obtain();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
                catch (Exception e)
                {
                    Log.d("apk", "文件下载失败");
                    msg = Message.obtain();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
                finally
                {
                    try
                    {
                        if (is != null)
                        { is.close(); }
                    }
                    catch (Exception e)
                    {
                    }
                    try
                    {
                        if (fos != null)
                        { fos.close(); }
                    }
                    catch (Exception e)
                    {
                    }
                }
            }
        });
    }

    public void shebeixizai()
    {
//        if (edatabanben > shebeiedata)
//        {
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url + "edata.apk").build();
            mOkHttpClient.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    Log.d("edata", "onFailure");
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

                    if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
                    {
                        sdPath = Environment.getExternalStorageDirectory() + "/homefarm/";
                    } else
                    {
                        sdPath = getFilesDir().getPath() + "/homefarm/";
                    }

                    try
                    {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
                        File file = new File(sdPath, "edata");
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1)
                        {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            Log.d("edata", "progress=" + progress);
                            msg = Message.obtain();
                            msg.what = 1;
                            msg.arg1 = progress;
                            handler.sendMessage(msg);
                        }
                        fos.flush();
                        Log.d("edata", "文件下载成功");
                        if (homefarmbanben <= shebeihomefarm)
                        {
                            msg = Message.obtain();
                            msg.what = 6;
                            handler.sendMessage(msg);
                        }else
                        {
                            {
                                OkHttpClient mOkHttpClient = new OkHttpClient();
                                Request request = new Request.Builder().url(url + "homefarm.apk").build();
                                mOkHttpClient.newCall(request).enqueue(new Callback()
                                {
                                    @Override
                                    public void onFailure(Call call, IOException e)
                                    {
                                        Log.d("homefarm", "onFailure");
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
                                        FileOutputStream fos1 = null;
                                        try
                                        {
                                            is = response.body().byteStream();
                                            long total = response.body().contentLength();
                                            File file = new File(sdPath, "homefarm");
                                            fos1 = new FileOutputStream(file);
                                            long sum = 0;
                                            while ((len = is.read(buf)) != -1)
                                            {
                                                fos1.write(buf, 0, len);
                                                sum += len;
                                                int progress = (int) (sum * 1.0f / total * 100);
                                                Log.d("homefarm", "progress=" + progress);
                                                msg = Message.obtain();
                                                msg.what = 1;
                                                msg.arg1 = progress;
                                                handler.sendMessage(msg);
                                            }
                                            fos1.flush();
                                            Log.d("homefarm", "文件下载成功");
                                            msg = Message.obtain();
                                            msg.what = 6;
                                            handler.sendMessage(msg);
                                        }
                                        catch (Exception e)
                                        {
                                            Log.d("homefarm", "文件下载失败");
                                            msg = Message.obtain();
                                            msg.what = 3;
                                            handler.sendMessage(msg);
                                        }
                                        finally
                                        {
                                            try
                                            {
                                                if (is != null)
                                                { is.close(); }
                                            }
                                            catch (Exception e)
                                            {
                                            }
                                            try
                                            {
                                                if (fos1 != null)
                                                { fos1.close(); }
                                            }
                                            catch (Exception e)
                                            {
                                            }
                                        }
                                    }
                                });
                            }
                        }

                    }
                    catch (Exception e)
                    {
                        Log.d("edata", "文件下载失败");
                        msg = Message.obtain();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }
                    finally
                    {
                        try
                        {
                            if (is != null)
                            { is.close(); }
                        }
                        catch (Exception e)
                        {
                        }
                        try
                        {
                            if (fos != null)
                            { fos.close(); }
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }
            });
//        }
    }

    //双击退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
        {
            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else
            {
//                unregisterReceiver(guangbo);
                finish();
                //在登录界面退出完全杀掉线程
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy()
    {
        try
        {
            unregisterReceiver(guangbo);
        }
        catch (Exception e) {}
        super.onDestroy();
    }
}
