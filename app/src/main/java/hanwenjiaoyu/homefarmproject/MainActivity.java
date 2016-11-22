package hanwenjiaoyu.homefarmproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import bean.mybutton;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
{
    Response response;
    OkHttpClient mOkHttpClient;
    private Request request;
    private Bitmap bitmap;
    private mybutton wendu_button;
    private mybutton zhuanpan;
    private mybutton shishui_button;
    private mybutton shifei_button;
    private mybutton tongfeng_button;
    private mybutton buguang_button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button button1 = (Button) findViewById(R.id.button1);
//        button1.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                String url = "http://192.168.1.100/";
//                mOkHttpClient = new OkHttpClient();
//
//                RequestBody requestBody =   new FormBody.Builder()
//                        .add("method", "Query")
//                        .add("methodType", "Q")
//                        .add("sql", "select d_bianhao,d_name,d_type from device")
//                        .build();
//                request = new Request.Builder()
//                        .url(url)
//                        .post(requestBody)
//                        .build();
//
//                response = null;
//
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
////                mOkHttpClient.newCall(request).enqueue(new Callback()
////                {
////                    @Override
////                    public void onFailure(Call call, IOException e)
////                    {
////                        Log.e("jieshou1", "testHttpPost ... onFailure() e=" + e);
////                    }
////
////
////                    @Override
////                    public void onResponse(Call call, Response response) throws IOException
////                    {
////                        try
////                        {
////                            if (response.isSuccessful())
////                            {
////                                //The call was successful. print it to the log
////                                Log.i("OKHttp", response.body().string());
////                            }
////                        }
////                        catch (IOException e)
////                        {
////                            e.printStackTrace();
////                        }
////                    }
////                });
//            }
//        });

        shishui_button = (mybutton) findViewById(R.id.shishui_button);
        shishui_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "施水");
                intent.putExtra("image", R.mipmap.shishui);
                intent.putExtra("color", 0xFF00BB9C);
                startActivity(intent);
            }
        });

        shifei_button = (mybutton) findViewById(R.id.shifei_button);
        shifei_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "施肥");
                intent.putExtra("image", R.mipmap.shifei);
                intent.putExtra("color", 0xFFF4C600);
                startActivity(intent);
            }
        });

        tongfeng_button = (mybutton) findViewById(R.id.tongfeng_button);
        tongfeng_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "通风");
                intent.putExtra("image", R.mipmap.tongfeng);
                intent.putExtra("color", 0xFF5AB3F0);
                startActivity(intent);
            }
        });

        wendu_button = (mybutton) findViewById(R.id.wendu_button);
        wendu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "温度");
                intent.putExtra("image", R.mipmap.wendu);
                intent.putExtra("color", 0xFFEB4F38);
                startActivity(intent);
            }
        });

        buguang_button = (mybutton) findViewById(R.id.buguang_button);
        buguang_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Caozuojiemian.class);
                intent.putExtra("title", "补光");
                intent.putExtra("image", R.mipmap.buguang);
                intent.putExtra("color", 0xFFA65AC3);
                startActivity(intent);
            }
        });
//        wendu_button.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                if (event.getAction() == MotionEvent.ACTION_DOWN)
//                {
//                    bitmap = ((BitmapDrawable)getResources().getDrawable(R.mipmap.zhuanpan)).getBitmap();
//                    //懒得判断位置小于图片框,大于图片,直接try
//                    try
//                    {
//                        //判断点击处像素的颜色是否为0，0表示没
//                        int a = bitmap.getPixel((int) (event.getX()), ((int) event.getY()));
//                        if (bitmap.getPixel((int) (event.getX()), ((int) event.getY())) != 0)
//                        {
//
//                        }
//                    }
//                    catch (Exception e) {}
//                }
//                return false;
//            }
//        });

        zhuanpan = (mybutton) findViewById(R.id.zhuanpan);
        zhuanpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(),"dianjiguanbi",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
