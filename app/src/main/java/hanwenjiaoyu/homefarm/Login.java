package hanwenjiaoyu.homefarm;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import bean.Sqlite;

/**
 * Created by Administrator on 2016/11/22.
 */

public class Login extends Activity
{
    public static Login loginthis;

    public Sqlite sqlite;
    public SQLiteDatabase db;
    private Cursor cursor;

    public String EQID,EQIDMD5;
    private LinearLayout login_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loginthis = this;

        //创建数据库
        sqlite = new Sqlite(getApplication(), "homefarm", null, 1);
        db = sqlite.getWritableDatabase();
        //读取EQID
        String sqlstr = "select * from xinxi where _id = 1";
        cursor = db.rawQuery(sqlstr,null);
        if(cursor.getCount() == 1)
        {
            cursor.move(1);
            EQID = cursor.getString(1);
            EQIDMD5 = cursor.getString(2);
        }
        if (EQID != null)
        {
            Intent intent = new Intent(Login.this,MainActivity.class);
            intent.putExtra("EQID", EQID);
            intent.putExtra("EQIDMD5", EQIDMD5);
            startActivity(intent);
            finish();
        }

        //做动画的layout容器
        login_layout = (LinearLayout) findViewById(R.id.login_layout);
        //临时登录按钮,以后删除
        Button denglu = (Button) findViewById(R.id.denglu);
        denglu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this,MainActivity.class);
                intent.putExtra("EQID", "1111111111");
                intent.putExtra("EQIDMD5", "737207bfff986b451956db85a7c8d380");
                Login.loginthis.db.execSQL("update xinxi set EQID = '1111111111', EQIDMD5 = '737207bfff986b451956db85a7c8d380' where _id =1");
                startActivity(intent);
                finish();
            }
        });

        Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getApplicationContext(), "扫描二维码", Toast.LENGTH_SHORT).show();
                if (Build.VERSION.SDK_INT >= 23) {
                    ActivityCompat.requestPermissions(Login.this,new String[]{Manifest.permission.CAMERA}, 4);
                }else
                {
                    Intent intent = new Intent(Login.this,com.dtr.zxing.activity.CaptureActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        Button wifi_button = (Button) findViewById(R.id.wifi_button);
        wifi_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this,com.rtk.simpleconfig_wizard.MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        donghua();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 4) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Login.this,com.dtr.zxing.activity.CaptureActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),"未成功获取权限,请手动打开",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void donghua()
    {
//        AlphaAnimation myAnimation_Alpha;
//        myAnimation_Alpha=new AlphaAnimation(0.1f, 1.0f);
//        myAnimation_Alpha.setDuration(5000);
        Animation myAnimation= AnimationUtils.loadAnimation(this, R.anim.alpha);
        login_layout.setAnimation(myAnimation);

    }
}
