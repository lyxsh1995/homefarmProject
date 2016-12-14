package hanwenjiaoyu.homefarm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/12/14.
 */

public class tupian extends Dialog
{
    Context context;
    public tupian(Context context)
    {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //去标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置背景黑暗
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.dimAmount=0.7f;
        getWindow().setAttributes(lp);
        //背景模糊
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.tupian);

    }
}
