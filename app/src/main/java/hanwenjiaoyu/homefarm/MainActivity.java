package hanwenjiaoyu.homefarm;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import bean.BaseUiListener;
import bean.Cdjson;
import bean.Fwjson;
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
    public static MainActivity mainActivitythis;
    public IWXAPI api;
    public Tencent mTencent;


    public String url = "http://192.168.1.100:8011/";
    public Response response;
    public OkHttpClient mOkHttpClient = new OkHttpClient();

    public String EQID;
    public String EQIDMD5;
    Gson gson = new Gson();
    Message msg = new Message();
    Cdjson cdjson = new Cdjson();
    private Request request;
    private Bitmap bitmap;
    private long exitTime = 0;
    private LinearLayout window_layout;
    public Switch kaiguan;
    private Mybutton wendu_button;
    private Mybutton zhuanpan;
    private Mybutton stop_button;
    private Mybutton shishui_button;
    private Mybutton shifei_button;
    private Mybutton tongfeng_button;
    private Mybutton buguang_button;
    private RequestBody requestBody;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
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

        final Intent intent = getIntent();
        EQID = intent.getStringExtra("EQID");
        EQIDMD5 = intent.getStringExtra("EQIDMD5");

        kaiguan = (Switch) findViewById(R.id.kaiguan);
        kaiguan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (kaiguan.isChecked())
                {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (Settings.canDrawOverlays(MainActivity.this)) {
                            Intent intent = new Intent(getApplicationContext(),FloatWindowService.class);
                            intent.putExtra("EQID",EQID);
                            intent.putExtra("EQIDMD5",EQIDMD5);
                            startService(intent);
                        } else {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(getApplicationContext(),FloatWindowService.class);
                        intent.putExtra("EQID",EQID);
                        intent.putExtra("EQIDMD5",EQIDMD5);
                        startService(intent);
                    }
                }else
                {
                    Intent intent = new Intent(getApplicationContext(),FloatWindowService.class);
                    stopService(intent);
                }
            }
        });
        ActivityManager myManager = (ActivityManager) getApplication().getSystemService(getApplication().ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for(int i = 0 ; i<runningService.size();i++)
        {
            if(runningService.get(i).service.getClassName().toString().equals("hanwenjiaoyu.homefarm.FloatWindowService"))
            {
                kaiguan.setChecked(true);
            }
        }

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
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(),"点击关闭",Toast.LENGTH_SHORT).show();
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

        Button tuichu = (Button) findViewById(R.id.tuichu);
        tuichu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getApplication().deleteDatabase("homefarm");
                finish();
                System.exit(0);
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
                final Bundle params = new Bundle();
                params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
                params.putString(QQShare.SHARE_TO_QQ_TITLE, "来自城市农场的消息");
                params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "城市农场是弟一科技城市农场项目的APP端");
                params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://www.baidu.com/");
                params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://chuantu.biz/t5/42/1479975573x1996140247.png");
                params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "城市农场");
                mTencent.shareToQQ(MainActivity.this, params, new BaseUiListener());
            }
        });

        requestBody = new FormBody.Builder()
                .add("fangfa", "termparam")
                .add("EQID", EQID)
                .add("EQIDMD5",EQIDMD5)
                .add("p_type","cd")
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
                        Log.i("jieshou",resstr);
                        java.lang.reflect.Type type = new TypeToken<Cdjson>() {}.getType();
                        cdjson = gson.fromJson(resstr, type);
                        msg = Message.obtain();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

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
                finish();
                System.exit(0);
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

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://www.baidu.com";
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
