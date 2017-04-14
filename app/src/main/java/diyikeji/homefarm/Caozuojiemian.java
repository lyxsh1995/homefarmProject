package diyikeji.homefarm;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import bean.Fwjson;
import bean.MD5;
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
    private RadioButton shengwen;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private RadioButton jiangwen;
    private EditText time;
    private int buttonid;

    private int zhuangtai;

    String Json = "";
    private OkHttpClient mOkHttpClient;
    Request request;
    private RequestBody requestBody;
    private int res;
    private int respro;
    Gson gson = new Gson();
    Message msg = new Message();
    Fwjson fwjson;
    private LinearLayout diyiceng;
    private LinearLayout dierceng;
    private LinearLayout disanceng;
    private LinearLayout douyaji;
    private LinearLayout moguxiang;
    private RadioGroup wenduradio;

    private TextView title2_text;
    private ImageView title2_image;
    public String fBillNo;

    Myjson json = new Myjson();

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
                    if (fwjson.ceng1.equals("dis"))
                    {
                        diyiceng.setVisibility(View.GONE);
                    }
                    if (fwjson.ceng2.equals("dis"))
                    {
                        dierceng.setVisibility(View.GONE);
                    }
                    if (fwjson.ceng3.equals("dis"))
                    {
                        disanceng.setVisibility(View.GONE);
                    }
                    if (fwjson.douyaji.equals("dis"))
                    {
                        douyaji.setVisibility(View.GONE);
                    }
                    if (fwjson.mogu.equals("dis"))
                    {
                        disanceng.setVisibility(View.GONE);
                    }
                    break;
                //接收服务器返回的操作成功或失败
                case 1:
                    try
                    {
                        if (msg.obj.toString().substring(0, 2).equals("ok"))
                        {
                            //更新Mainactivity图片
                            fBillNo = msg.obj.toString().substring(2);
                            Mybutton button = (Mybutton) MainActivity.mainActivitythis.findViewById(buttonid);
                            button.setBackgroundResource(respro);
                            Toast.makeText(getApplicationContext(), "已提交到服务器,等待执行", Toast.LENGTH_SHORT).show();

//                        chaxunfbillno();

                            //15秒后取消黄色状态
//                            MainActivity.mainActivitythis.quxiaodengdai(title2_text.getText().toString());
                            finish();

                            //开始读取刚刚操作是否被硬件执行
                        } else
                        {
                            Toast.makeText(getApplicationContext(), "提交到服务器失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                    catch (Exception e) {}

                    //该指令返回后的操作
                case 2:
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caozuojiemian);

        title2_text = (TextView) findViewById(R.id.title2_text);
        title2_image = (ImageView) findViewById(R.id.tiele2_image);

        Intent intent = getIntent();
        title2_text.setTextColor(intent.getIntExtra("color", 0xFF03BB9C));
        title2_text.setText(intent.getStringExtra("title"));
        title2_image.setImageDrawable(getResources().getDrawable(intent.getIntExtra("image", R.mipmap.shishui)));
        buttonid = intent.getIntExtra("buttonid", 0);
        res = intent.getIntExtra("res", 0);
        respro = intent.getIntExtra("respro", 0);

        time = (EditText) findViewById(R.id.time);
        diyiceng = (LinearLayout) findViewById(R.id.diyiceng);
        dierceng = (LinearLayout) findViewById(R.id.dierceng);
        disanceng = (LinearLayout) findViewById(R.id.disanceng);
        douyaji = (LinearLayout) findViewById(R.id.douyaji);
        moguxiang = (LinearLayout) findViewById(R.id.moguxiang);
        wenduradio = (RadioGroup) findViewById(R.id.wendu);
        checkButton1 = (Button) findViewById(R.id.checkButton1);
        checkButton2 = (Button) findViewById(R.id.checkButton2);
        checkButton3 = (Button) findViewById(R.id.checkButton3);
        checkButton4 = (Button) findViewById(R.id.checkButton4);
        checkButton5 = (Button) findViewById(R.id.checkButton5);
        shengwen = (RadioButton) findViewById(R.id.shengwen);

        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        jiangwen = (RadioButton) findViewById(R.id.jiangwen);
        jiangwen.setChecked(true);

        switch (title2_text.getText().toString())
        {
            case "施水":
            case "施肥":
            case "补光":
//                douyaji.setVisibility(View.GONE);
                moguxiang.setVisibility(View.GONE);
                break;
            case "通风":
                diyiceng.setVisibility(View.GONE);
                dierceng.setVisibility(View.GONE);
                disanceng.setVisibility(View.GONE);
            case "温度":
                douyaji.setVisibility(View.GONE);
                break;
        }
        if (title2_text.getText().toString().equals("温度"))
        {
            wenduradio.setVisibility(View.VISIBLE);
        }

        shengwen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                json.FTypeID = "1";
            }
        });

        jiangwen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                json.FTypeID = "5";
            }
        });

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
//        requestBody = new FormBody.Builder()
//                .add("fangfa", "termparam")
//                .add("EQID", MainActivity.mainActivitythis.EQID)
//                .add("EQIDMD5", MD5.jiami(MainActivity.mainActivitythis.EQID))
//                .add("p_type", "fw")
//                .build();
//        request = new Request.Builder()
//                .url(MainActivity.mainActivitythis.url)
//                .post(requestBody)
//                .build();
//        mOkHttpClient.newCall(request).enqueue(new Callback()
//        {
//            @Override
//            public void onFailure(Call call, IOException e)
//            {
//                Log.e("jieshou", "testHttpPost ... onFailure() e=" + e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException
//            {
//                try
//                {
//                    if (response.isSuccessful())
//                    {
//                        String resstr = response.body().string();
//                        Log.i("jieshou", resstr);
//                        java.lang.reflect.Type type = new TypeToken<Fwjson>() {}.getType();
//                        fwjson = gson.fromJson(resstr, type);
//                        msg = Message.obtain();
//                        msg.what = 0;
//                        handler.sendMessage(msg);
//                    }
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        });

        Button kaishi = (Button) findViewById(R.id.kaishi);
        kaishi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (time.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "请填写时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() && !checkBox5.isChecked())
                {
                    Toast.makeText(getApplicationContext(), "请勾选开始的层数", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (res != 0)
                {
                    //正式操作
                    caozuo();
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
                    if (!checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked() && !checkBox5.isChecked())
                    {
                        Toast.makeText(getApplicationContext(), "请勾选停止的层数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tingzhi();
                    Mybutton button = (Mybutton) MainActivity.mainActivitythis.findViewById(buttonid);
                    button.setBackgroundResource(res);
                    finish();
                }
            }
        });

        Button fanhui = (Button) findViewById(R.id.fanhui);
        fanhui.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void caozuo()
    {
        if (checkBox1.isChecked())
        {
            json.FFloorOne = "1";
        }
        if (checkBox2.isChecked())
        {
            json.FFloorTwo = "1";
        }
        if (checkBox3.isChecked())
        {
            json.FFloorThree = "1";
        }
        if (checkBox4.isChecked())
        {
            json.FDouyaji = "1";
        }
        if (checkBox5.isChecked())
        {
            json.FMG = "1";
        }
        switch (title2_text.getText().toString())
        {
            case "施水":
                json.FTypeID = "3";
                json.FFreq = "4";
                break;
            case "施肥":
                json.FTypeID = "2";
                break;
            case "通风":
                json.FTypeID = "7";
                break;
            case "温度":
                json.FTypeID = "1";
                json.FFreq = "2";
                break;
            case "补光":
                json.FTypeID = "4";
                json.FFloorOne = "1";
                json.FFloorTwo = "1";
                json.FFloorThree = "1";
                json.FDouyaji = "1";
                break;
        }
        json.FContinuePM = time.getText().toString();
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        json.FExcTime = t1;
        json.EQID = MainActivity.mainActivitythis.EQID;
        json.EQIDMD5 = MD5.jiami(json.EQID);

        if (MainActivity.mainActivitythis.udp.tongxingmod)
        {
            Toast.makeText(getApplicationContext(), "正在操作,请稍后", Toast.LENGTH_SHORT).show();
            new AsyncTask<String, Void, Boolean>()
            {
                @Override
                protected Boolean doInBackground(String... params)
                {
//                    long FBillNo = 3000000001L + new Random().nextInt(1000000000);
//                    String strsql = "UDP:EINSERT INTO shoudong VALUES (null,'" + FBillNo + "','0','" + json.FFloorOne + "','"
//                            + json.FFloorTwo + "','" + json.FFloorThree + "','" + json.FDouyaji + "','"
//                            + json.FMG + "','0','" + json.FTypeID + "','3','" + json.FFreq + "','"
//                            + json.FContinuePM + "','0','','','','','','" + json.FExcTime + "','0','0')";
                    String strsql = "UDP:Eupdate zuoyeshixu set FContinuePM = "+json.FContinuePM+ ",FExcTime = datetime('now','localtime'),FExcFlag = 0 where FFloorOne = " + json.FFloorOne+ " and FFloorTwo = "+ json.FFloorTwo+ " and FFloorThree = "+ json.FFloorThree + " and FTypeID = "+json.FTypeID + " and FSourceType = 1";
                    MainActivity.mainActivitythis.udp.UDPsend(strsql, true);
                    return MainActivity.mainActivitythis.udp.UDPreceive(true).contains("ok");
                }

                @Override
                protected void onPostExecute(Boolean b)
                {
                    if (b)
                    {
                        Toast.makeText(getApplicationContext(), "手动操作成功", Toast.LENGTH_SHORT).show();
                        //更新Mainactivity图片
                        Mybutton button = (Mybutton) MainActivity.mainActivitythis.findViewById(buttonid);
                        button.setBackgroundResource(respro);
                        //15秒后取消黄色状态
//                        MainActivity.mainActivitythis.quxiaodengdai(title2_text.getText().toString());
                        finish();
                    } else
                    {
                        Toast.makeText(getApplicationContext(), "手动操作失败", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }.execute();
        } else
        {
            requestBody = new FormBody.Builder()
                    .add("fangfa", "charu")
                    .add("FFloorOne", json.FFloorOne)
                    .add("FFloorTwo", json.FFloorTwo)
                    .add("FFloorThree", json.FFloorThree)
                    .add("FDouyaji", json.FDouyaji)
                    .add("FMG", json.FMG)
                    .add("FTypeID", json.FTypeID)
                    .add("FFreq", json.FFreq)
                    .add("FContinuePM", json.FContinuePM)
                    .add("FExcTime", json.FExcTime)
                    .add("EQID", json.EQID)
                    .add("EQIDMD5", MD5.jiami(json.EQID))
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
                            Log.i("jieshou", resstr);
//                        java.lang.reflect.Type type = new TypeToken<Fwjson>() {}.getType();
//                        fwjson = gson.fromJson(resstr, type);
                            msg = Message.obtain();
                            msg.what = 1;
                            msg.obj = resstr;
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
    }

    public void tingzhi()
    {
        if (checkBox1.isChecked())
        {
            json.FFloorOne = "1";
        }
        if (checkBox2.isChecked())
        {
            json.FFloorTwo = "1";
        }
        if (checkBox3.isChecked())
        {
            json.FFloorThree = "1";
        }
        if (checkBox4.isChecked())
        {
            json.FDouyaji = "1";
        }
        if (checkBox5.isChecked())
        {
            json.FMG = "1";
        }
        switch (title2_text.getText().toString())
        {
            case "施水":
                json.FTypeID = "3";
                json.FFreq = "4";
                break;
            case "施肥":
                json.FTypeID = "2";
                break;
            case "通风":
                json.FTypeID = "7";
                break;
            case "温度":
                json.FTypeID = "1";
                json.FFreq = "2";
                break;
            case "补光":
                json.FTypeID = "4";
                json.FFloorOne = "1";
                json.FFloorTwo = "1";
                json.FFloorThree = "1";
                json.FDouyaji = "1";
                break;
        }

        requestBody = new FormBody.Builder()
                .add("fangfa", "tingzhi")
                .add("FFloorOne", json.FFloorOne)
                .add("FFloorTwo", json.FFloorTwo)
                .add("FFloorThree", json.FFloorThree)
                .add("FDouyaji",json.FDouyaji)
                .add("FTypeID", json.FTypeID)
                .add("EQID", MainActivity.mainActivitythis.EQID)
                .add("EQIDMD5", MD5.jiami(MainActivity.mainActivitythis.EQID))
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
                        Log.i("jieshou", resstr);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    //当前操作指令是否执行
    public void chaxunfbillno()
    {
        final TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                requestBody = new FormBody.Builder()
                        .add("fangfa", "fbillno")
                        .add("EQID", MainActivity.mainActivitythis.EQID)
                        .add("EQIDMD5", MD5.jiami(MainActivity.mainActivitythis.EQID))
                        .add("FBillNo", fBillNo)
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
                                msg.what = 2;
                                msg.obj = resstr;
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
        };
        Timer timer = new Timer(true);
        timer.schedule(task, 3000);
    }
}
