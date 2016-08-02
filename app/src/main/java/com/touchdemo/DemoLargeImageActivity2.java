package com.touchdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class DemoLargeImageActivity2 extends AppCompatActivity {
    //    private LargeImageView mLargeImageView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        mLargeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
//        try {
//            InputStream inputStream = getAssets().open("world.jpg");
//
//            //获得图片的宽、高
//            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
//            tmpOptions.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(inputStream, null, tmpOptions);
//            int width = tmpOptions.outWidth;
//            int height = tmpOptions.outHeight;
//
//            //设置显示图片的中心区域
//            BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect(width / 2 - 100, height / 2 - 100, width / 2 + 100, height / 2 + 100), options);
////            mLargeImageView.setImageBitmap(bitmap);
////            mL
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    long preTmie = 0;
    private LargeImageView mLargeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_largeimage_activity2);

        mLargeImageView = (LargeImageView) findViewById(R.id.id_largetImageview);
        mLargeImageView.setFullScreen(true);//初始默认不是全屏的

        try {
            InputStream inputStream = getAssets().open("world.jpg");
            int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
            int screenHeight = getWindowManager().getDefaultDisplay().getHeight(); // 屏幕高（像素，如：800p）
            mLargeImageView.setScreenWidth(screenWidth);
            mLargeImageView.setScreenHeight(screenHeight);

            mLargeImageView.setInputStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
