package hanwenjiaoyu.homefarm;

import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.Cljson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/3.
 */

public class Mycaiyuan extends Fragment
{
    View view;
    public Mycaiyuan mycaiyuanthis;
    public TextView mingcheng,jianjie;

    public Response response;
    public OkHttpClient mOkHttpClient = new OkHttpClient();
    private Request request;
    private RequestBody requestBody;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mycaiyuanthis = this;

        if (view == null)
        {
            view = inflater.inflate(R.layout.mycaiyuan, null);
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null)
        {
            parent.removeView(view);
        }

        mingcheng = (TextView) view.findViewById(R.id.mingcheng);
        jianjie = (TextView) view.findViewById(R.id.jianjie);

        Button daoru = (Button) view.findViewById(R.id.daoru);
        daoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(),Fangan.class);
                startActivity(intent);
            }
        });

        Button quedingdaoru = (Button) view.findViewById(R.id.quedingdaoru);
        quedingdaoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                requestBody = new FormBody.Builder()
                        .add("fangfa", "fangan")
                        .add("EQID", MainActivity.mainActivitythis.EQID)
                        .add("EQIDMD5",MainActivity.mainActivitythis.EQIDMD5)
                        .add("fanganid", Fangan.fanganthis.fanganid)
                        .build();
                request = new Request.Builder()
                        .url(Login.loginthis.url)
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

        return view;
    }
}
