package hanwenjiaoyu.homefarm;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import bean.BaseUiListener;
import bean.Cdjson;
import bean.Cljson;
import bean.Lastjson;
import bean.Termparamjson;
import bean.MD5;
import bean.Mybutton;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
{
    //全局变量
    public static MainActivity mainActivitythis;
    public IWXAPI api;
    public Tencent mTencent;
    private Uri mUri;
    private File file;
    private Bitmap bp;

    private TextView wendu_shuju;
    private TextView shidu_shuju;
    private TextView lixiantext;
    private ContextMenuDialogFragment mMenuDialogFragment;

    public String url = Login.loginthis.url;

    public Response response;
    public OkHttpClient mOkHttpClient = new OkHttpClient();
    private Request request;
    private RequestBody requestBody;

    public boolean dakaiguanbi = false;//当前状态,真为打开,假为关闭

    public TimerTask task;
    public Timer timer;

    public String EQID;
    public String EQIDMD5;
    Gson gson = new Gson();
    Message msg = new Message();
    Cdjson cdjson = new Cdjson();
    private List<Cljson> rs;
    private List<Lastjson> rslist;
    private List<Termparamjson> termparamjsonrs;

    private long exitTime = 0;
    private LinearLayout window_layout;
    private LinearLayout zhuanpanlayout;
    private FrameLayout shejiaolayout;
    public Switch kaiguan;
    private Switch lixiankaiguan;
    private Mybutton wendu_button;
    private Mybutton zhuanpan;
    private Mybutton stop_button;
    private Mybutton shishui_button;
    private Mybutton shifei_button;
    private Mybutton tongfeng_button;
    private Mybutton buguang_button;

    //按钮状态 0白色 1绿色 2黄色
    public int wenduzhuangtai = 0;
    public int shishuizhuangtai = 0;
    public int shifeizhuangtai = 0;
    public int tongfengzhuangtai = 0;
    public int buguangzhuangtai = 0;

    //UDP变量
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private String wifiad = "255.255.255.255";
    private String myip;
    private byte[] message = new byte[1024];
    int server_port = 48899;
    Integer port = 8899;
    Boolean tongxingmod = false;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {super.handleMessage(msg);
            try
            {
                switch (msg.what)
                {
                    //判断施水施肥等模块是否可用
                    case 0:
                        if (cdjson.buguang.equals("dis"))
                        {
                            buguang_button.setVisibility(View.GONE);
                        }
                        if (cdjson.penshui.equals("dis"))
                        {
                            wendu_button.setVisibility(View.GONE);
                        }
                        if (cdjson.shifei.equals("dis"))
                        {
                            shifei_button.setVisibility(View.GONE);
                        }
                        if (cdjson.shishui.equals("dis"))
                        {
                            shishui_button.setVisibility(View.GONE);
                        }
                        break;
                    //更新本地悬浮窗数据
                    case 1:
                        //计算平均值
                        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
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
                            wendu_shuju.setText(wendu_shuju.getText() + "   良好");
                        } else
                        {
                            wendu_shuju.setText(wendu_shuju.getText() + "   恶劣");
                        }
                        if (20 <= b && b <= 40)
                        {
                            shidu_shuju.setText(shidu_shuju.getText() + "   良好");
                        } else
                        {
                            shidu_shuju.setText(shidu_shuju.getText() + "   恶劣");
                        }
                        break;
                    case 2:
                        String zhuangtai = "";
                        if (wenduzhuangtai != 2)
                            wendu_button.setBackgroundResource(R.mipmap.wendu_button);
                        if (shifeizhuangtai != 2)
                        shifei_button.setBackgroundResource(R.mipmap.shifei_button);
                        if (shishuizhuangtai != 2)
                        shishui_button.setBackgroundResource(R.mipmap.shishui_button);
                        if (buguangzhuangtai != 2)
                        buguang_button.setBackgroundResource(R.mipmap.buguang_button);
                        if (tongfengzhuangtai != 2)
                        tongfeng_button.setBackgroundResource(R.mipmap.tongfeng_button);
                        for (int i = 0; i < rslist.size(); i++)
                        {
                            switch (rslist.get(i).FTypeID)
                            {
                                case "1":
                                    //降温
                                case "5":
                                    //升温
                                    wendu_button.setBackgroundResource(R.mipmap.wendu_button_pro);
                                    wenduzhuangtai = 0;
//                                zhuangtai += "正在喷水\n";
                                    break;
                                case "2":
                                    //施肥
                                    shifei_button.setBackgroundResource(R.mipmap.shifei_button_pro);
                                    shifeizhuangtai = 0;
                                    break;
                                case "3":
                                    //施水
                                    shishui_button.setBackgroundResource(R.mipmap.shishui_button_pro);
                                    shishuizhuangtai = 0;
                                    break;
                                case "4":
                                    //补光:
                                    buguang_button.setBackgroundResource(R.mipmap.buguang_button_pro);
                                    buguangzhuangtai = 0;
                                    break;
                                case "7":
                                    //通风
                                    tongfeng_button.setBackgroundResource(R.mipmap.tongfeng_button_pro);
                                    tongfengzhuangtai = 0;
                                    break;
                            }
                        }
                        break;
                    case 3:
                        //查询设备是否关闭
                        if (termparamjsonrs.get(0).getP_value1().equals("dis") && termparamjsonrs.get(0).getP_value2().equals("dis") && termparamjsonrs.get(0).getP_value3().equals("dis"))
                        {
                            //设备关闭状态
                            stop_button.setBackgroundResource(R.mipmap.start_button);
                            dakaiguanbi = false;
                        }
                        else if (termparamjsonrs.get(0).getP_value1().equals("en") || termparamjsonrs.get(0).getP_value2().equals("en") || termparamjsonrs.get(0).getP_value3().equals("en"))
                        {
                            //设备打开状态
                            stop_button.setBackgroundResource(R.mipmap.stop_button);
                            dakaiguanbi = true;
                        }
                        break;
                    case 4:
                        //设备打开关闭
                        String shebeiyunxing = (String) msg.obj;
                        if (shebeiyunxing.equals("ok"))
                        {
                            if (dakaiguanbi)
                            {
                                stop_button.setBackgroundResource(R.mipmap.start_button);
                                dakaiguanbi = false;
                            }else
                            {
                                stop_button.setBackgroundResource(R.mipmap.stop_button);
                                dakaiguanbi = true;
                            }
                        }else
                        {
                            Toast.makeText(getApplicationContext(),"设备打开关闭失败",Toast.LENGTH_SHORT).show();
                        }
                }
            }
            catch (Exception e)
            {
                Log.e("handlr ERROR","数据异常:" + e.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivitythis = this;

        api = WXAPIFactory.createWXAPI(this, "wx6dd2baabb3de7c7b", true);
        api.registerApp("wx6dd2baabb3de7c7b");

        mTencent = Tencent.createInstance("1105607320", getApplicationContext());

        final Intent intent =  getIntent();
        EQID = intent.getStringExtra("EQID");
        EQIDMD5 = MD5.jiami(EQID);

        //读取本地IP
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        final int ipAddress = wifiInfo.getIpAddress();
        myip = (ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff);
        Random rand = new Random();
        port = rand.nextInt(8899);
        port += 40000;
        try
        {
            datagramSocket = new DatagramSocket(port);
            datagramSocket.setSoTimeout(6000);
            datagramPacket = new DatagramPacket(message, message.length);
        }
        catch (SocketException e1)
        {
            e1.printStackTrace();
        }


        //设备管理对象
        WindowManager wm = this.getWindowManager();
        //转盘layout
        zhuanpanlayout = (LinearLayout) findViewById(R.id.zhuanpanlayout);
        //把转盘高设为屏幕最大宽度
        zhuanpanlayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, wm.getDefaultDisplay().getWidth()));
        //悬浮窗
        kaiguan = (Switch) findViewById(R.id.kaiguan);
        kaiguan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (kaiguan.isChecked())
                {
                    //判断是否安卓6.0
                    if (Build.VERSION.SDK_INT >= 23)
                    {
                        //判断是否已经授权
                        if (Settings.canDrawOverlays(MainActivity.this))
                        {
                            Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                            intent.putExtra("EQID", EQID);
                            startService(intent);
                        } else
                        {
                            //打开系统悬浮窗设置页面
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            startActivity(intent);
                            kaiguan.setChecked(false);
                        }
                    } else
                    {
                        Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                        intent.putExtra("EQID", EQID);
                        startService(intent);
                    }
                } else
                {
                    try
                    {
                        Intent intent = new Intent(getApplicationContext(), FloatWindowService.class);
                        FloatView.timer.cancel();
                        stopService(intent);
                    }
                    catch (Exception e) {}
                }
            }
        });

        //判断悬浮窗service是否已经启动

        ActivityManager myManager = (ActivityManager) getApplication().getSystemService(getApplication().ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++)
        {
            if (runningService.get(i).service.getClassName().toString().equals("hanwenjiaoyu.homefarm.FloatWindowService"))
            {
                kaiguan.setChecked(true);
            }
        }

        //离线模式
        lixiankaiguan = (Switch) findViewById(R.id.lixiankaiguan);
        lixiantext = (TextView) findViewById(R.id.lixiantext);
        lixiankaiguan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                lixiantext.setText("正在切换模式");
                lixiankaiguan.setClickable(false);
                if(lixiankaiguan.isChecked())
                {
                    //开始进入离线模式
                    new AsyncTask<String, Void, Boolean>()
                    {
                        @Override
                        protected Boolean doInBackground(String... params)
                        {
                            UDPsend(params[0],false);
                            String receivedata = UDPreceive(false);
                            try
                            {
                                wifiad = receivedata.substring(0, receivedata.indexOf(","));
                                UDPsend(params[1],false);
                                if (UDPreceive(false).contains("+ok"))
                                {
                                    UDPsend(params[2],false);
                                    if(UDPreceive(false).contains("+ok"))
                                    {
                                        UDPsend(params[3],false);
                                        //让设备第一次启动UDP()
                                        UDPsend("UDP:F",true);
                                        int i = 0;
                                        while (!UDPreceive(true).contains("ok"))
                                        {
                                            UDPsend("UDP:F",true);
                                            i++;
                                            if (i>=3)
                                            {
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                                }
                            }catch (Exception e){}//第一条指令出错 局域网广播
                            return false;
                        }

                        @Override
                        protected void onPostExecute(Boolean b)
                        {
                            if (b)
                            {
                                Toast.makeText(getApplicationContext(),"进入离线模式成功",Toast.LENGTH_SHORT).show();
                                lixiantext.setText("离线模式");
                                lixiankaiguan.setClickable(true);
                                tongxingmod = true;
                            }else
                            {
                                lixiankaiguan.setChecked(false);
                                Toast.makeText(getApplicationContext(),"进入离线模式失败",Toast.LENGTH_SHORT).show();
                                lixiantext.setText("离线模式");
                                lixiankaiguan.setClickable(true);
                            }
                        }
                    }.execute("csncat@yzr", "AT+NETP=UDP,SERVER," + port + "," + myip + "\r","AT+TMODE=throughput\r","AT+Z\r");
                } else
                {
                    //取消离线模式
                    new AsyncTask<String, Void, Boolean>()
                    {
                        @Override
                        protected Boolean doInBackground(String... params)
                        {
                            UDPsend(params[0],true);
                            if(UDPreceive(true).contains("ok"))
                            {
                                UDPsend("csncat@yzr",false);
                                UDPreceive(false);
                                UDPsend(params[1], false);//进入HTTP模式
                                if (UDPreceive(false).contains("+ok"))
                                {
                                    UDPsend(params[2].substring(0, 3), false);//进入HTTP模式
                                    return true;
                                }
                            }
                            return  false;
                        }

                        @Override
                        protected void onPostExecute(Boolean b)
                        {
                            if (b)
                            {
                                Toast.makeText(getApplicationContext(),"退出离线模式成功",Toast.LENGTH_SHORT).show();
                                lixiantext.setText("离线模式");
                                lixiankaiguan.setClickable(true);
                                tongxingmod = false;
                            }else
                            {
                                lixiankaiguan.setChecked(true);
                                Toast.makeText(getApplicationContext(),"退出离线模式失败",Toast.LENGTH_SHORT).show();
                                lixiantext.setText("离线模式");
                                lixiankaiguan.setClickable(true);
                            }
                        }
                    }.execute("UDP:S", "AT+TMODE=htpc\r","AT+Z\r");
                }
            }
        });

        shishui_button = (Mybutton) findViewById(R.id.shishui_button);
        shishui_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "施水");
                intent.putExtra("image", R.mipmap.shishui);
                intent.putExtra("color", 0xFF00BB9C);
                intent.putExtra("buttonid", v.getId());
                intent.putExtra("res", R.mipmap.shishui_button);
                intent.putExtra("respro", R.mipmap.shishui_button_wating);
                startActivity(intent);
            }
        });

        shifei_button = (Mybutton) findViewById(R.id.shifei_button);
        shifei_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "施肥");
                intent.putExtra("image", R.mipmap.shifei);
                intent.putExtra("color", 0xFFF4C600);
                intent.putExtra("buttonid", v.getId());
                intent.putExtra("res", R.mipmap.shifei_button);
                intent.putExtra("respro", R.mipmap.shifei_button_wating);
                startActivity(intent);
            }
        });

        tongfeng_button = (Mybutton) findViewById(R.id.tongfeng_button);
        tongfeng_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "通风");
                intent.putExtra("image", R.mipmap.tongfeng);
                intent.putExtra("color", 0xFF5AB3F0);
                intent.putExtra("buttonid", v.getId());
                intent.putExtra("res", R.mipmap.tongfeng_button);
                intent.putExtra("respro", R.mipmap.tongfeng_button_wating);
                startActivity(intent);
            }
        });

        wendu_button = (Mybutton) findViewById(R.id.wendu_button);
        wendu_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "温度");
                intent.putExtra("image", R.mipmap.wendu);
                intent.putExtra("color", 0xFFEB4F38);
                intent.putExtra("buttonid", v.getId());
                intent.putExtra("res", R.mipmap.wendu_button);
                intent.putExtra("respro", R.mipmap.wendu_button_wating);
                startActivity(intent);
            }
        });

        buguang_button = (Mybutton) findViewById(R.id.buguang_button);
        buguang_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "补光");
                intent.putExtra("image", R.mipmap.buguang);
                intent.putExtra("color", 0xFFA65AC3);
                intent.putExtra("buttonid", v.getId());
                intent.putExtra("res", R.mipmap.buguang_button);
                intent.putExtra("respro", R.mipmap.buguang_button_wating);
                startActivity(intent);
            }
        });

        stop_button = (Mybutton) findViewById(R.id.stop_button);
        stop_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String cz;
                cz =(dakaiguanbi)? "dakai":"guanbi";
                requestBody = new FormBody.Builder()
                        .add("fangfa", "kaiguan")
                        .add("EQID", EQID)
                        .add("EQIDMD5", MD5.jiami(EQID))
                        .add("caozuo",cz)
                        .build();
                request = new Request.Builder()
                        .url(url)
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
                                msg.what = 4;
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

        zhuanpan = (Mybutton) findViewById(R.id.zhuanpan);

        zhuanpan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "您没有定制该功能", Toast.LENGTH_SHORT).show();
            }
        });

        //右上角菜单

        final FragmentManager fragmentManager = getSupportFragmentManager();
        ImageButton menu_button = (ImageButton) findViewById(R.id.menu_button);
        menu_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //显示菜单
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
            }
        });
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize(100);
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        //按下某一项
        mMenuDialogFragment.setItemClickListener(new OnMenuItemClickListener()
        {
            @Override
            public void onMenuItemClick(View clickedView, int position)
            {
                switch (position)
                {
//                    case 1:
//                        //详细状态
//                        Intent intent = new Intent(MainActivity.this, Zhuangtai.class);
//                        startActivity(intent);
//                        break;
                    case 1:
                        //我的菜园
                        Intent intent = new Intent(MainActivity.this, Caiyuan.class);
                        startActivity(intent);
                        break;
                    case 2:
                        //辅助设备联网
                        intent = new Intent(MainActivity.this, com.rtk.simpleconfig_wizard.MainActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        //退出登录
                        getApplication().deleteDatabase("homefarm");
                        intent = new Intent(getApplicationContext(), FloatWindowService.class);
                        stopService(intent);
                        finish();
                        System.exit(0);
                        break;
                    case 5:
                        //关于
                        break;
                }
            }
        });

        //拍照
        ImageButton paizhao = (ImageButton) findViewById(R.id.paizhao);
        paizhao.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File appDir = new File(Environment.getExternalStorageDirectory() + "/homefarm");
                if (!appDir.exists())
                {
                    appDir.mkdir();
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                //CAMERA_WITH_DATA = 3023
                startActivityForResult(cameraIntent, 3023);
            }
        });

        //相册
        ImageButton xiangce = (ImageButton)findViewById(R.id.xiangce);
        xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                file = new File(Environment.getExternalStorageDirectory() + "/homefarm/", String.valueOf(System.currentTimeMillis()) + ".jpg");
//                mUri = Uri.fromFile(file);
//                Uri data = Uri.parse(Environment.getExternalStorageDirectory() + "/homefarm");
//                intent.setData(data);
                startActivityForResult(intent,1);
            }
        });

        shejiaolayout = (FrameLayout) findViewById(R.id.shejiaolayout);
        shejiaolayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                shejiaolayout.setVisibility(View.GONE);
            }
        });
        //分享
        ImageButton fenxiang = (ImageButton)findViewById(R.id.fenxiang);
        fenxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                shejiaolayout.setVisibility(View.VISIBLE);
            }
        });

        ImageButton weixin = (ImageButton) findViewById(R.id.weixin);
        weixin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                weixinfengxiang(0);
            }
        });

        ImageButton pengyouquan = (ImageButton) findViewById(R.id.pengyouquan);
        pengyouquan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                weixinfengxiang(1);
            }
        });

        ImageButton qqhaoyou = (ImageButton) findViewById(R.id.qqhaoyou);
        qqhaoyou.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle params = new Bundle();
                //bp是在应用拍照的图片
                if (mUri != null)
                {
                    //分享在应用里拍的图片
                    bp = getimage(file.getPath());
                }
                if (bp != null)
                {
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, file.getPath());
                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "城市农场");
                    mTencent.shareToQQ(MainActivity.this, params, new BaseUiListener());
                } else
                {
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                    params.putString(QQShare.SHARE_TO_QQ_TITLE, "来自城市农场的消息");
                    params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "城市农场是弟一科技城市农场项目的APP端");
                    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://sj.qq.com/myapp/detail.htm?apkName=hanwenjiaoyu.homefarm");
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://chuantu.biz/t5/42/1479975573x1996140247.png");
                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "城市农场");
                    mTencent.shareToQQ(MainActivity.this, params, new BaseUiListener());
                }
            }
        });

        ImageButton qqkongjian = (ImageButton) findViewById(R.id.qqkongjian);
        qqkongjian.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle params = new Bundle();
                //bp是在应用拍照的图片
                if (mUri != null)
                {
                    //分享在应用里拍的图片
                    bp = getimage(file.getPath());
                }
                if (bp != null)
                {
                    //把分享到QQ,添加一个自动分享到QQ空间的flag
                    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
                    params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
                    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, file.getPath());
                    params.putString(QQShare.SHARE_TO_QQ_TITLE, "来自城市农场的消息");
                    params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "城市农场");
                    mTencent.shareToQQ(MainActivity.this, params, new BaseUiListener());
                } else
                {
                    params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
                    params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "来自城市农场的消息");
                    params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "城市农场是弟一科技城市农场项目的APP端");
                    params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://sj.qq.com/myapp/detail.htm?apkName=hanwenjiaoyu.homefarm");
                    //图片要做处理
                    ArrayList<String> path_arr = new ArrayList<>();
                    path_arr.add("http://chuantu.biz/t5/42/1479975573x1996140247.png");
                    params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, path_arr);
                    mTencent.shareToQzone(MainActivity.this, params, new BaseUiListener());
                }

            }
        });

        ImageButton zhilian = (ImageButton) findViewById(R.id.zhilian);
        zhilian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this,Zhilian.class);
                startActivity(intent);
            }
        });

        //获取设备可操作模块(转盘)
        requestBody = new FormBody.Builder()
                .add("fangfa", "termparam")
                .add("EQID", EQID)
                .add("EQIDMD5", MD5.jiami(EQID))
                .add("p_type", "cd")
                .build();
        request = new Request.Builder()
                .url(url)
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
                        Type type = new TypeToken<Cdjson>() {}.getType();
                        cdjson = gson.fromJson(resstr, type);
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

        //温度湿度数据更新(悬浮窗)
        wendu_shuju = (TextView) findViewById(R.id.wendu_shuju);
        shidu_shuju = (TextView) findViewById(R.id.shidu_shuju);

        String sqlstr = "SELECT d_name,d_lastvalue FROM device where d_type = 'cl' and EQID = '" + EQID + "' and (d_name like 'turangshidu%' or d_name like 'turangwendu%')";
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
                        rs = new ArrayList<Cljson>();
                        Type type = new TypeToken<List<Cljson>>() {}.getType();
                        rs = gson.fromJson(resstr, type);
                        msg = Message.obtain();
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

        //更新正在运行的操作
        Createtask();
        timer = new Timer(true);
        //一分钟刷新一次
        timer.schedule(task, 0, 60 * 1000);
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
            }
            else
            {
//                unregisterReceiver(guangbo);
                finish();
                if (!kaiguan.isChecked())
                {
                    System.exit(0);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void weixinfengxiang(int flag)
    {
        //flag 0是朋友,1是朋友圈
        if (!api.isWXAppInstalled())
        {
            Toast.makeText(MainActivity.this, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
            return;
        }
        //bp是在应用拍照的图片
        if (mUri != null)
        {
            //分享在应用里拍的图片
            bp = getimage(file.getPath());
        }
        if (bp != null)
        {
            WXImageObject imgObj = new WXImageObject(bp);
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = imgObj;

            Bitmap thumbBmp = Bitmap.createScaledBitmap(bp, 120, 120, true);
            bp.recycle();
            msg.thumbData = bmpToByteArray(thumbBmp, true);  // 设置缩略图

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("img");
            req.message = msg;
            req.scene = flag;
            api.sendReq(req);
        } else
        {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = "http://sj.qq.com/myapp/detail.htm?apkName=hanwenjiaoyu.homefarm";
            WXMediaMessage msg = new WXMediaMessage(webpage);

            msg.title = "城市农场";
            msg.description = "弟一科技城市农场项目微信分享";
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.logo_144);
            msg.setThumbImage(thumb);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = flag;
            api.sendReq(req);
        }
    }

    //微信开发文档什么也不写,都要从网上找
    //设置缩略图
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle)
        {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try
        {
            output.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    //构建一个唯一标志
    private static String buildTransaction(String type)
    {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 根据bitmap压缩图片质量
     *
     * @param bitmap 未压缩的bitmap
     * @return 压缩后的bitmap
     */
    public static Bitmap cQuality(Bitmap bitmap)
    {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        int beginRate = 30;
        //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bOut);
        while (bOut.size() / 1024 / 1024 > 100)
        {  //如果压缩后大于100Kb，则提高压缩率，重新压缩
            beginRate -= 10;
            bOut.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, beginRate, bOut);
        }
        ByteArrayInputStream bInt = new ByteArrayInputStream(bOut.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(bInt);
        if (newBitmap != null)
        {
            return newBitmap;
        } else
        {
            return bitmap;
        }
    }

    //图片比例压缩
    private Bitmap getimage(String srcPath)
    {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww)
        {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh)
        {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
        { be = 1; }
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return cQuality(bitmap);//压缩好比例大小后再进行质量压缩
    }

    //创建定时器任务
    protected void Createtask()
    {
        task = new TimerTask()
        {
            @Override
            public void run()
            {
                zhuanpan();
            }
        };
    }

    //刷新转盘
    public void zhuanpan()
    {
        if (tongxingmod)
        {
            new AsyncTask<String, Void, String>()
            {
                @Override
                protected String doInBackground(String... params)
                {
                    UDPsend(params[0],true);
                    return UDPreceive(true);
                }

                @Override
                protected void onPostExecute(String data)
                {
                    termparamjsonrs = new ArrayList<Termparamjson>();
                    Type type = new TypeToken<List<Termparamjson>>() {}.getType();
                    termparamjsonrs = gson.fromJson(data, type);
                    msg = Message.obtain();
                    msg.what = 3;
                    handler.sendMessage(msg);
                }
            }.execute("UDP:SELECT p_value1,p_value2,p_value3 FROM termparam where p_name = 'yunxingfangshi'");
        }
        else
        {
            //查询设备是否关闭
            String sqlstr = "SELECT p_value1,p_value2,p_value3 FROM termparam where p_name = 'yunxingfangshi' and EQID = '"+EQID+"'";
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
                            Log.i("chaxun", resstr);
                            termparamjsonrs = new ArrayList<Termparamjson>();
                            Type type = new TypeToken<List<Termparamjson>>() {}.getType();
                            termparamjsonrs = gson.fromJson(resstr, type);
                            msg = Message.obtain();
                            msg.what = 3;
                            handler.sendMessage(msg);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });

            requestBody = new FormBody.Builder()
                    .add("fangfa", "yunxing")
                    .add("EQID", EQID)
                    .add("EQIDMD5", MD5.jiami(EQID))
                    .build();
            request = new Request.Builder()
                    .url(url)
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
                            Log.i("zuoyeshixu", resstr);
                            rslist = new ArrayList<Lastjson>();
                            java.lang.reflect.Type type = new TypeToken<List<Lastjson>>() {}.getType();
                            rslist = gson.fromJson(resstr, type);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    msg = Message.obtain();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            });
        }
    }

    //回调监听
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        switch (requestCode)
        {
            //摄像头调用之后
            case 3023:
                try
                {
                    //是否拍照并选取了图片
                    if (resultCode == RESULT_OK)
                    {
                        file = new File(Environment.getExternalStorageDirectory() + "/homefarm/", String.valueOf(System.currentTimeMillis()) + ".jpg");
                        mUri = Uri.fromFile(file);
                        Toast.makeText(getApplicationContext(), "请选择需要分享的社交工具", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        mUri = null;
                        file = null;
                        bp = null;
                        Toast.makeText(getApplicationContext(), "您未选择照片", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            //相册选取照片
            case 1:
                if (resultCode == RESULT_OK)
                {
                    mUri = data.getData();
                    file = new File(getRealPathFromURI(mUri));
                    Toast.makeText(getApplicationContext(), "请选择需要分享的社交工具", Toast.LENGTH_SHORT).show();
                } else
                {
                    mUri = null;
                    file = null;
                    bp = null;
                    Toast.makeText(getApplicationContext(), "您未选择图片", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        //微信分享
        if (requestCode == 10103 || requestCode == 10104)
        {
            Tencent.onActivityResultData(requestCode, resultCode, data, new BaseUiListener());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //右上角菜单列表
    private List<MenuObject> getMenuObjects()
    {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();
        //缩放
        Matrix matrix = new Matrix();
        Bitmap bitmap;

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

//        MenuObject send = new MenuObject("详细状态");
//        bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.nongye));
//        //设置缩放比例
//        matrix.postScale(70f/bitmap.getWidth(),70f/bitmap.getHeight());
//        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//        send.setBitmap(bitmap);

        MenuObject like = new MenuObject("我的菜园");
        bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.nongtian));
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        like.setBitmap(bitmap);


        MenuObject addFr = new MenuObject("辅助设备联网");
        bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.wifi));
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        addFr.setBitmap(bitmap);

//        //图片太小不缩放
//        MenuObject addFav = new MenuObject("关    于");
//        bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.guanyu));
////        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//        addFav.setBitmap(bitmap);

        MenuObject block = new MenuObject("退出登录");
        bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.tuichu));
//        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        block.setBitmap(bitmap);

        menuObjects.add(close);
//        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
//        menuObjects.add(addFav);
        menuObjects.add(block);
        return menuObjects;
    }

    //获取绝对路径
    public String getRealPathFromURI(Uri contentUri)
    {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }


    //取消等待 caozuojiemian
    public void quxiaodengdai(final String title2)
    {
        switch (title2)
        {
            case "施水":
                shishuizhuangtai = 2;
                break;
            case "施肥":
                shifeizhuangtai = 2;
                break;
            case "补光":
                buguangzhuangtai = 2;
                break;
            case "通风":
                tongfengzhuangtai = 2;
                break;
            case "温度":
                wenduzhuangtai = 2;
                break;
        }
        xiaoshuaxin();
        final TimerTask task = new TimerTask() {
            @Override
            public void run()
            {
                switch (title2)
                {
                    case "施水":
                        shishuizhuangtai = 0;
                        break;
                    case "施肥":
                        shifeizhuangtai = 0;
                        break;
                    case "补光":
                        buguangzhuangtai = 0;
                        break;
                    case "通风":
                        tongfengzhuangtai = 0;
                        break;
                    case "温度":
                        wenduzhuangtai = 0;
                        break;
                }
            }
        };
        Timer timer2 = new Timer(true);
        timer2.schedule(task,15000);
    }

    //小刷新
    int xiaoshuaxinjishu = 0;
    public void xiaoshuaxin()
    {
        final Timer xiaotimer = new Timer(true);
        final TimerTask xiaoshuaxinxunhuan = new TimerTask()
        {
            @Override
            public void run()
            {
                if (xiaoshuaxinjishu > 3)
                {
                    xiaoshuaxinjishu = 0;
                    xiaotimer.cancel();
                }
                xiaoshuaxinjishu++;
                zhuanpan();
            }
        };
        xiaotimer.schedule(xiaoshuaxinxunhuan,0,5000);
    }

    //UDP监听  head

    /**
     * 监听UDP接收,需要在子线程里进行
     * @param head 为真时要求内容前4个字符为 "UDP:"
     * @return 返回接收到的内容
     */
    public String UDPreceive(Boolean head)
    {
        String rdata = "";
        try
        {
            // 准备接收数据
            message = new byte[1024];
            datagramPacket = new DatagramPacket(message, message.length);
            datagramSocket.receive(datagramPacket);
            rdata = new String(datagramPacket.getData());
            if (head)
            {
                if (!rdata.contains("UDP:"))
                {
                    return "";
                }
            }
            Log.d("UDP Demo", rdata);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return rdata;
    }

    /**
     * UDP发送数据
     * @param senddata 需要发送的数据
     * @param leixing false为发送命令到48899端口
     *                 ture为发送数据到8899端口
     */
    public void UDPsend(final String senddata, final Boolean leixing)
    {
        //发送广播连接设备
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                String data = senddata;
                InetAddress local = null;
                try
                {
                    // 换成服务器端IP
                     local = InetAddress.getByName(wifiad);
                }
                catch (UnknownHostException e)
                {
                    e.printStackTrace();
                }
                if(leixing)
                {
                    //添加包头包尾
                    data = "^"+senddata+"|";
                }
                int msg_length = data.length();
                byte[] messagemessageByte = data.getBytes();
                DatagramPacket p = new DatagramPacket(messagemessageByte, msg_length, local, leixing?port:server_port);
                try
                {
                    datagramSocket.send(p);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
