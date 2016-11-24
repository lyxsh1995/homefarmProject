package hanwenjiaoyu.homefarm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

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

    public String url = "http://192.168.1.100:8011/";
    public Response response;
    public OkHttpClient mOkHttpClient = new OkHttpClient();
    private Request request;
    private Bitmap bitmap;
    private long exitTime = 0;
    private Mybutton wendu_button;
    private Mybutton zhuanpan;
    private Mybutton shishui_button;
    private Mybutton shifei_button;
    private Mybutton tongfeng_button;
    private Mybutton buguang_button;
    private RequestBody requestBody;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivitythis = this;

        Button button1 = (Button) findViewById(R.id.postbutton);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                requestBody = new FormBody.Builder()
                        .add("fangfa", "admin")
                        .add("sqlstr", "123")
                        .build();
                request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                response = null;

//                new Thread()
//                {
//                    public void run()
//                    {
//                        try
//                        {
//                            response = mOkHttpClient.newCall(request).execute();
//                            if (response.isSuccessful())
//                            {
//                                Log.i("信息", response.body().string());
//                            } else
//                            {
//                                throw new IOException("Unexpected code " + response);
//                            }
//                        }
//                        catch (IOException e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();

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
                                //The call was successful. print it to the log
                                Log.i("OKHttp", response.body().string());
                            }
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        shishui_button = (Mybutton) findViewById(R.id.shishui_button);
        shishui_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "施水");
                intent.putExtra("image", R.mipmap.shishui);
                intent.putExtra("color", 0xFF00BB9C);
                intent.putExtra("buttonid",v.getId());
                intent.putExtra("res",R.mipmap.shishui_button);
                intent.putExtra("respro",R.mipmap.shishui_button_pro);
                startActivity(intent);
            }
        });

        shifei_button = (Mybutton) findViewById(R.id.shifei_button);
        shifei_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "施肥");
                intent.putExtra("image", R.mipmap.shifei);
                intent.putExtra("color", 0xFFF4C600);
                intent.putExtra("buttonid",v.getId());
                intent.putExtra("res",R.mipmap.shifei_button);
                intent.putExtra("respro",R.mipmap.shifei_button_pro);
                startActivity(intent);
            }
        });

        tongfeng_button = (Mybutton) findViewById(R.id.tongfeng_button);
        tongfeng_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "通风");
                intent.putExtra("image", R.mipmap.tongfeng);
                intent.putExtra("color", 0xFF5AB3F0);
                intent.putExtra("buttonid",v.getId());
                intent.putExtra("res",R.mipmap.tongfeng_button);
                intent.putExtra("respro",R.mipmap.tongfeng_button_pro);
                startActivity(intent);
            }
        });

        wendu_button = (Mybutton) findViewById(R.id.wendu_button);
        wendu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "温度");
                intent.putExtra("image", R.mipmap.wendu);
                intent.putExtra("color", 0xFFEB4F38);
                intent.putExtra("buttonid",v.getId());
                intent.putExtra("res",R.mipmap.wendu_button);
                intent.putExtra("respro",R.mipmap.wendu_button_pro);
                startActivity(intent);
            }
        });

        buguang_button = (Mybutton) findViewById(R.id.buguang_button);
        buguang_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "补光");
                intent.putExtra("image", R.mipmap.buguang);
                intent.putExtra("color", 0xFFA65AC3);
                intent.putExtra("buttonid",v.getId());
                intent.putExtra("res",R.mipmap.buguang_button);
                intent.putExtra("respro",R.mipmap.buguang_button_pro);
                startActivity(intent);
            }
        });

        zhuanpan = (Mybutton) findViewById(R.id.zhuanpan);
        zhuanpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(),"dianjiguanbi",Toast.LENGTH_SHORT).show();
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
}
