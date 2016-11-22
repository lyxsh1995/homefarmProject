package hanwenjiaoyu.homefarmproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/11/22.
 */

public class Login extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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
}
