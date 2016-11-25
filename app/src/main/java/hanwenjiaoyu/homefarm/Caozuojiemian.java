package hanwenjiaoyu.homefarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import bean.Mybutton;
import bean.Myjson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/11/21.
 */

public class Caozuojiemian extends Activity
{

    private Button checkButton1;
    private Button checkButton2;
    private Button checkButton3;
    private Button checkButton4;
    private Button checkButton5;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private int buttonid;
    private int res;
    private int respro;


    String Json = "";
    private OkHttpClient mOkHttpClient;
    Request request;
    Gson gson = new Gson();
    Message msg = new Message();
    public Myjson myjson;
    private LinearLayout diyiceng;
    private LinearLayout dierceng;
    private LinearLayout disanceng;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                //判断第一层第二层第三层
                case 0:
                    if (myjson.FFloorOne.equals("2"))
                    {
                        diyiceng.setVisibility(View.GONE);
                    }
                    if (myjson.FFloorTwo.equals("2"))
                    {
                        dierceng.setVisibility(View.GONE);
                    }
                    if (myjson.FFloorThree.equals("2"))
                    {
                        disanceng.setVisibility(View.GONE);
                    }
                    if (myjson.FDouyaji.equals("2"))
                    {
                        disanceng.setVisibility(View.GONE);
                    }
                    if (myjson.FMG.equals("2"))
                    {
                        disanceng.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caozuojiemian);

        TextView title2_text = (TextView) findViewById(R.id.title2_text);
        ImageView title2_image = (ImageView) findViewById(R.id.tiele2_image);

        Intent intent = getIntent();
        title2_text.setTextColor(intent.getIntExtra("color", 0xFF03BB9C));
        title2_text.setText(intent.getStringExtra("title"));
        title2_image.setImageDrawable(getResources().getDrawable(intent.getIntExtra("image", R.mipmap.shishui)));
        buttonid = intent.getIntExtra("buttonid", 0);
        res = intent.getIntExtra("res", 0);
        respro = intent.getIntExtra("respro", 0);

        diyiceng = (LinearLayout) findViewById(R.id.diyiceng);
        dierceng = (LinearLayout) findViewById(R.id.dierceng);
        disanceng = (LinearLayout) findViewById(R.id.disanceng);
        checkButton1 = (Button) findViewById(R.id.checkButton1);
        checkButton2 = (Button) findViewById(R.id.checkButton2);
        checkButton3 = (Button) findViewById(R.id.checkButton3);
        checkButton4 = (Button) findViewById(R.id.checkButton4);
        checkButton5 = (Button) findViewById(R.id.checkButton5);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) findViewById(R.id.checkBox5);

        checkButton1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkBox1.isChecked())
                {
                    checkBox1.setChecked(false);
                } else
                {
                    checkBox1.setChecked(true);
                }
            }
        });

        checkButton2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkBox2.isChecked())
                {
                    checkBox2.setChecked(false);
                } else
                {
                    checkBox2.setChecked(true);
                }
            }
        });

        checkButton3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkBox3.isChecked())
                {
                    checkBox3.setChecked(false);
                } else
                {
                    checkBox3.setChecked(true);
                }
            }
        });

        checkButton4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkBox4.isChecked())
                {
                    checkBox4.setChecked(false);
                } else
                {
                    checkBox4.setChecked(true);
                }
            }
        });

        checkButton5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (checkBox5.isChecked())
                {
                    checkBox5.setChecked(false);
                } else
                {
                    checkBox5.setChecked(true);
                }
            }
        });

        mOkHttpClient = new OkHttpClient();

        //读取可操作层数
        RequestBody requestBody =   new FormBody.Builder()
                .add("fangfa", "chaxun")
                .add("sqlstr", "select * from shoudongapp Order by FInterID desc LIMIT 1")
                .build();
        request = new Request.Builder()
                .url(MainActivity.mainActivitythis.url)
                .post(requestBody)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                Log.e("jieshou", "testHttpPost ... onFailure() e=" + e);
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
                        java.lang.reflect.Type type = new TypeToken<Myjson>() {}.getType();
                        myjson = gson.fromJson(resstr, type);
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

        Button kaishi = (Button) findViewById(R.id.kaishi);
        kaishi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (res != 0)
                {
                    Mybutton button = (Mybutton) MainActivity.mainActivitythis.findViewById(buttonid);
                    button.setBackgroundResource(respro);
                    Intent intent = new Intent(Caozuojiemian.this, Zhuangtai.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button tingzhi = (Button) findViewById(R.id.tingzhi);
        tingzhi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (res != 0)
                {
                    Mybutton button = (Mybutton) MainActivity.mainActivitythis.findViewById(buttonid);
                    button.setBackgroundResource(res);
                    finish();
                }
            }
        });
    }
}
