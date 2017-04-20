package bean;

import android.util.Log;
import android.widget.Toast;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import com.diyikeji.homefarm.MainActivity;

/**
 * Created by Administrator on 2016/11/24.
 */

public class BaseUiListener implements IUiListener {
    public String result;
    @Override
    public void onComplete(Object o)
    {
        Toast.makeText(MainActivity.mainActivitythis.getApplicationContext(),"QQ分享成功",Toast.LENGTH_SHORT).show();
        Log.i("QQ",o.toString());
    }

    @Override
    public void onError(UiError e) {
        Toast.makeText(MainActivity.mainActivitythis.getApplicationContext(),"QQ分享错误",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCancel() {
        Toast.makeText(MainActivity.mainActivitythis.getApplicationContext(),"QQ分享失败",Toast.LENGTH_SHORT).show();
    }
}