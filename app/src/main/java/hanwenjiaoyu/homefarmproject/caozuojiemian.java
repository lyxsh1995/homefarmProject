package hanwenjiaoyu.homefarmproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import bean.mybutton;

/**
 * Created by Administrator on 2016/11/21.
 */

public class Caozuojiemian extends Activity
{

    private Button checkButton1;
    private Button checkButton2;
    private Button checkButton3;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private int buttonid;
    private int res;
    private int respro;

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
        buttonid = intent.getIntExtra("buttonid", 0);
        res = intent.getIntExtra("res", 0);
        respro = intent.getIntExtra("respro",0);

        checkButton1 = (Button) findViewById(R.id.checkButton1);
        checkButton2 = (Button) findViewById(R.id.checkButton2);
        checkButton3 = (Button) findViewById(R.id.checkButton3);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);

        checkButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (checkBox1.isChecked())
                {
                    checkBox1.setChecked(false);
                }else
                {
                    checkBox1.setChecked(true);
                }
            }
        });

        checkButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (checkBox2.isChecked())
                {
                    checkBox2.setChecked(false);
                }else
                {
                    checkBox2.setChecked(true);
                }
            }
        });

        checkButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (checkBox3.isChecked())
                {
                    checkBox3.setChecked(false);
                }else
                {
                    checkBox3.setChecked(true);
                }
            }
        });

        Button kaishi = (Button) findViewById(R.id.kaishi);
        kaishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (res != 0)
                {
                    mybutton button = (mybutton) MainActivity.mainActivitythis.findViewById(buttonid);
                    button.setBackgroundResource(respro);
                    Intent intent = new Intent(Caozuojiemian.this,Zhuangtai.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button tingzhi = (Button) findViewById(R.id.tingzhi);
        tingzhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (res != 0)
                {
                    mybutton button = (mybutton) MainActivity.mainActivitythis.findViewById(buttonid);
                    button.setBackgroundResource(res);
                    finish();
                }
            }
        });
    }
}
