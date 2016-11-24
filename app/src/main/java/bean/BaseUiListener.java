package bean;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import hanwenjiaoyu.homefarm.MainActivity;

/**
 * Created by Administrator on 2016/11/24.
 */

public class BaseUiListener implements IUiListener {
    private String scope;

    public BaseUiListener() {
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onError(UiError arg0) {
        System.err.println(arg0.errorCode);
    }

    @Override
    public void onComplete(Object arg0) {

    }
}