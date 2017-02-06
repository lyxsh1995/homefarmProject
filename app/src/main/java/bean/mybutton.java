package bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

public class Mybutton extends FrameLayout {

    private int width = -1;
    private int height = -1;
    private Bitmap bitmap;

    public Mybutton(Context context) {
        super( context);
    }

    public Mybutton(Context context, AttributeSet attrs, int defStyle) {
        super( context, attrs, defStyle);
    }

    public Mybutton(Context context, AttributeSet attrs) {
        super( context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action != MotionEvent.ACTION_DOWN) {
            return super.onTouchEvent(event);
        }
        int x = (int)event.getX();
        int y = (int)event.getY();
        if(width == -1 || height == -1) {
            Drawable drawable = getBackground().getCurrent();
            bitmap = ((BitmapDrawable)drawable).getBitmap();
            width = getWidth();
            height = getHeight();
        }
        float fx = (float)x*((float)666/width);//按原图比例还原的真实高度
        float fy = (float)y*((float)666/height);//按原图比例还原的真实宽度

        if(null == bitmap || fx < 0 || fy < 0 || fx >= width || fy >= height) {
            return false;
        }
        int pixel = bitmap.getPixel( (int)fx, (int)fy);
        if(Color.TRANSPARENT == pixel)
        {
            return false;
        }
        return super.onTouchEvent(event);
    }
}