package com.example.administrator.homefarmproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/21.
 */

public class caozuojiemian extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caozuojiemian);

        TextView title2_text = (TextView) findViewById(R.id.title2_text);
        ImageView title2_image = (ImageView) findViewById(R.id.tiele2_image);

        Intent intent = getIntent();
        title2_text.setTextColor(intent.getIntExtra("color",0xFF03BB9C));
        title2_text.setText(intent.getStringExtra("title"));
        title2_image.setImageDrawable(getResources().getDrawable(intent.getIntExtra("image",R.mipmap.shishui)));
    }
}
