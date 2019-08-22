package com.freak.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.freak.customview.custom.SDCardFileObserver;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SDCardFileObserver sdCardFileObserver = new SDCardFileObserver("");
//        sdCardFileObserver.startWatching();
//        sdCardFileObserver.stopWatching();
    }
}
