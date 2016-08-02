package com.touchdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhy on 15/5/16.
 */
public class LargeImageView extends View {
    private BitmapRegionDecoder mDecoder;
    /**
     * 图片的宽度和高度
     */
    private int mImageWidth, mImageHeight;
    /**
     * 绘制的区域
     */
    private volatile Rect mRect = new Rect();

    private MoveGestureDetector mDetector;

    private boolean mFullScreen = false;

    //屏幕尺寸
    private int mScreenHeight;
    private int mScreenWidth;

    //中间过渡的图片尺寸
    private int mMiddleHeight;
    private int mMiddleWidth;


    public void setScreenWidth(int screenWidth) {
        mScreenWidth = screenWidth;
    }


    public void setScreenHeight(int screenHeight) {
        mScreenHeight = screenHeight;
    }

    private InputStream mIs;
    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    public boolean isFullScreen() {
        return mFullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        mFullScreen = fullScreen;
    }

    static {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public void setInputStream(InputStream is) {

        mIs = is;
        Log.d("4+++++++++++++", "进来了");
        try {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            // Grab the bounds for the scene dimensions
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;

            requestLayout();
            invalidate();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }


    public void init() {
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector() {
            @Override
            public boolean onMove(MoveGestureDetector detector) {
                int moveX = (int) detector.getMoveX();
                int moveY = (int) detector.getMoveY();

                if (mImageWidth > getWidth()) {
                    mRect.offset(-moveX, 0);
                    checkWidth();
                    invalidate();
                }
                if (mImageHeight > getHeight()) {
                    mRect.offset(0, -moveY);
                    checkHeight();
                    invalidate();
                }

                return true;
            }
        });
    }


    private void checkWidth() {


        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.right > imageWidth) {
            rect.right = imageWidth;
            rect.left = imageWidth - getWidth();
        }

        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
        }
    }


    private void checkHeight() {

        Rect rect = mRect;
        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();
        }

        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }


    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    long preTmie = 0;
    float x1 = 0;
    float y1 = 0;
    float x2 = 0;
    float y2 = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            y1 = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x2 = event.getX();
            y2 = event.getY();
            if (Math.abs(x1 - x2) < 6) {
                Log.d("++++++++", "touch 单击事件");

                long currentTimeMillis = System.currentTimeMillis();

                if (currentTimeMillis - preTmie < 500) {
                    Log.d("++++++++", "touch 双击事件");
                    if (isFullScreen()) {
                        setFullScreen(false);
                    } else {
                        setFullScreen(true);
                    }
                    requestLayout();
                    invalidate();

                } else {
                    //不是双击
                    preTmie = currentTimeMillis;
                }

                return false;// 距离较小，当作click事件来处理
            }
            if (Math.abs(x1 - x2) > 60) {  // 真正的onTouch事件

                Log.d("++++++++", "触摸事件");
            }
        }
        mDetector.onToucEvent(event);
        return true;// 返回true，不执行click事件
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bm;
        // TODO: 2016/8/2  0.1 没有居中显示， 0.2 外部局域依旧响应点击事件

        if (isFullScreen()) {
            bm = mDecoder.decodeRegion(mRect, options);
        } else {
            bm = mDecoder.decodeRegion(new Rect(mScreenWidth / 2 - 100, mScreenHeight / 2 - 100, mScreenWidth / 2 + 100, mScreenHeight / 2 + 100), options);
        }
        canvas.drawBitmap(bm, 0, 0, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        //默认直接显示图片的中心区域，可以自己去调节
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;
    }

}
