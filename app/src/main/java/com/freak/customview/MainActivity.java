package com.freak.customview;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.freak.customview.custom.SDCardFileObserver;
import com.freak.httphelper.ApiCallback;
import com.freak.httphelper.SubscriberCallBack;
import com.freak.httphelper.log.LogUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SDCardFileObserver mSdCardFileObserver;
    private File mFile = null;
    private ImageView image_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RxPermissions(this)
                .requestEach(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new SubscriberCallBack<>(new ApiCallback<Permission>() {
                    @Override
                    public void onSuccess(Permission model) {
                        if (model.granted) {

                        } else {
//                            finish();
                        }
                    }

                    @Override
                    public void onFailure(String msg) {

                    }
                }));
        image_view = findViewById(R.id.image_view);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSdCardFileObserver.stopWatching();
    }

    public File save() {
        try {
            View contentView = findViewById(R.id.ll_layout);
            Bitmap bitmap = Bitmap.createBitmap(contentView.getWidth(), contentView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            contentView.draw(canvas);
            File fileDir = getExternalFilesDir(null);
            fileDir.mkdirs();
            File file = new File(fileDir, "test.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            Log.e("TAG", "保存成功");
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveOnClick(View view) {
        mFile = save();
    }

    public void lookOnClick(View view) {
        if (mSdCardFileObserver == null) {
            mSdCardFileObserver = new SDCardFileObserver("/storage/emulated/0/Android/data/com.freak.customview/files/", "test.png", FileObserver.ALL_EVENTS);
        }
        mSdCardFileObserver.startWatching();
        if (mFile != null) {
            final Uri uri = toURI(this, mFile);
            Log.e("TAG", String.valueOf(uri));
            Glide.with(this).load(uri).thumbnail(0.1f).into(image_view);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (fileIsExists(uri.toString())) {
                        Log.e("TAG", "存在");
                    } else {
                        Log.e("TAG", "不存在");
                        Glide.with(MainActivity.this).load("").error(R.drawable.ic_launcher_background).thumbnail(0.1f).into(image_view);
                    }
                }
            }, 5000);
        }
    }

    public static Uri toURI(Context mContext, File file) {
        return Uri.fromFile(file);
    }

    /**
     * 判断文件是否存在
     *
     * @param strFile
     * @return
     */
    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
