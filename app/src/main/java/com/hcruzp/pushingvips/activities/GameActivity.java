package com.hcruzp.pushingvips.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.hcruzp.pushingvips.R;
import com.hcruzp.pushingvips.views.GameView;

public class GameActivity extends Activity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
    }

    @Override
    public void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        setContentView(new GameView(this));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            super.onDestroy();
            finishAffinity();
//            android.os.Process.killProcess(android.os.Process.myPid());
            return true;
        } else {
            return false;
        }
    }
}