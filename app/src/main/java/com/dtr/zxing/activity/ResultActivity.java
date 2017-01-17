package com.dtr.zxing.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import bean.MD5;
import hanwenjiaoyu.homefarm.Login;
import hanwenjiaoyu.homefarm.MainActivity;
import hanwenjiaoyu.homefarm.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.dtr.zxing.decode.DecodeThread;

import java.io.IOException;
import java.util.Date;

public class ResultActivity extends Activity {
    String url = Login.loginthis.url;
	private ImageView mResultImage;
	private TextView mResultText;

	OkHttpClient mOkHttpClient;
	Request request;
	Response response;

    private Message msg;
	private String mresult;
	private String eqid;
	private String eqidmd5;

    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                //登陆成功
                case 0:
                    Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                    Login.loginthis.db.execSQL("update xinxi set EQID = '"+eqid+"' where _id =1");
                    intent.putExtra("EQID", eqid);
                    startActivity(intent);
                    finish();
                    break;
                //登录失败
                case 1:
                    Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		Bundle extras = getIntent().getExtras();

		mResultImage = (ImageView) findViewById(R.id.result_image);
		mResultText = (TextView) findViewById(R.id.result_text);

		if (null != extras) {
			int width = extras.getInt("width");
			int height = extras.getInt("height");

			LayoutParams lps = new LayoutParams(width, height);
			lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
			lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
			lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
			
			mResultImage.setLayoutParams(lps);

			mresult = extras.getString("result");
			try
			{
                int start = mresult.indexOf("?");
				eqid = mresult.substring(start+1, mresult.indexOf("|"));
				eqidmd5 = MD5.jiami(eqid);
                mResultText.setText("设备ID:"+ eqid);
			}catch (Exception e)
			{
				Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
			}


			Bitmap barcode = null;
			byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
			if (compressedBitmap != null) {
				barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
				// Mutable copy:
				barcode = barcode.copy(Bitmap.Config.RGB_565, true);
			}
			mResultImage.setImageBitmap(barcode);

            mOkHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("fangfa", "denglu")
                    .add("EQID", eqid)
                    .add("EQIDMD5",MD5.jiami(eqid))
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
                            if (resstr.equals("1"))
                            {
                                msg = Message.obtain();
                                msg.what = 0;
                                handler.sendMessage(msg);
                            }
                            else
                            {
                                msg = Message.obtain();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			Intent intent = new Intent(ResultActivity.this, Login.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
