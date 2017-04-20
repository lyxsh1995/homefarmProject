package com.diyikeji.homefarm.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.diyikeji.homefarm.MainActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by Administrator on 2016/11/30.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MainActivity.mainActivitythis.api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        MainActivity.mainActivitythis.api.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp baseResp)
    {
        String str = "";
        switch (baseResp.errCode)
        {
            case BaseResp.ErrCode.ERR_OK:
                str = "微信分享成功"; ;//成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                str = "微信取消分享";//取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                str = "微信分享被拒绝";
                break;
            default:
                str = "微信分享失败";//返回
                break;
        }
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onReq(BaseReq baseResp)
    {
        try
        {
            Intent intent = new Intent(WXEntryActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch (Exception e)
        {
        }
    }
}
