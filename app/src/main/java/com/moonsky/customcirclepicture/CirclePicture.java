package com.moonsky.customcirclepicture;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

import com.moonsky.customcirclepicture.utils.BitmapUtils;

/**
 * 自定义圆形图像
 * 支持圆形和圆角图形
 * Created by Nick on 2017/9/10.
 */
public class CirclePicture extends android.support.v7.widget.AppCompatImageView {


    private static final String TAG = "CirclePicture";

    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    //显示图片的类型
    private int type;
    //圆角的大小
    private int circleCorner;
    //默认圆角的大小
    private static final int DEFAULT_ROUND_CORNER = 10;

    //椭圆的范围
    private RectF roundReact;

    //圆的半径
    private int radius;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CirclePicture(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePicture);

        //获取显示类型，默认为圆
        type = typedArray.getInt(R.styleable.CirclePicture_type, TYPE_CIRCLE);

        //圆角大小
        circleCorner = typedArray.getDimensionPixelSize(R.styleable.CirclePicture_roundCorner,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_ROUND_CORNER, getResources().getDisplayMetrics()));

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: ");

        //圆形图形
        if (type == TYPE_CIRCLE) {
            int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
            radius = size / 2;
            setMeasuredDimension(size, size);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(TAG, "onSizeChanged: ");
        //椭圆图形---圆角图形
        if (type == TYPE_ROUND) {
            roundReact = new RectF(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw: ");
        processBitmap();

        if (type == TYPE_CIRCLE) {
            canvas.drawCircle(radius, radius, radius, paint);
        }else {
            canvas.drawRoundRect(roundReact,circleCorner,circleCorner,paint);
        }
    }

    private void processBitmap() {

        Bitmap bitmap = BitmapUtils.drawableToBitmap(getDrawable());

        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale;
        Matrix matrix = new Matrix();
        if (type == TYPE_CIRCLE) {
            //计算缩放比例
            scale = (radius * 2.0f) / Math.min(bitmap.getWidth(), bitmap.getHeight());
            matrix.setScale(scale, scale);
        }else {
            //计算缩放比例
            matrix.setScale((getWidth()*1.0f)/bitmap.getWidth(), (getHeight()*1.0f)/bitmap.getHeight());
        }

        bitmapShader.setLocalMatrix(matrix);

        paint.setShader(bitmapShader);
    }

    /**
     * 设置圆的类型
     * @param typeValue
     */
    public void setType(int typeValue) {

        if (typeValue != type) {
            type=typeValue;
            requestLayout();
        }
    }

    /**
     * 设置圆角
     * @param dpValue
     */
    public void setCircleCorner(int dpValue) {
        int pxValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
        if (pxValue != circleCorner) {
            circleCorner=pxValue;
            invalidate();
        }
    }

}
