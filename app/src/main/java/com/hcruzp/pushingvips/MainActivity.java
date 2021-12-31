package com.hcruzp.pushingvips;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ImageView iv = (ImageView) findViewById(R.id.idImgInicio);
        iv.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
/*				Intent intent = new Intent(Main.this, Results.class);
//				intent.putExtra("thetext", et.getText().toString());
				startActivity(intent);*/

                setContentView(new GameView(MainActivity.this));
                return false;
            }
        });
/*		Button b = (Button) findViewById(R.id.button1);
//        ImageView iv = (ImageView) findViewById(R.id.idImgInicio);
		b.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, Results.class);
				startActivity(intent);
			}
		});*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onStop() {
        System.out.println("LLEGA onStop");
//        if (isFinishing()) {
        super.onStop();
        finish();
//        	android.os.Process.killProcess(android.os.Process.myPid());
        //}
    }

    @Override
    public void onDestroy() {
        System.out.println("LLEGA onDestroy");
//        if (isFinishing()) {
        super.onDestroy();
        finish();
//        	android.os.Process.killProcess(android.os.Process.myPid());
        //}
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            System.out.println("Fue el back button desde MAIN");
            super.onDestroy();
            finish();
            return true;
        } else {
            System.out.println("NO fue el back button desde MAIN");
            return false;
        }
    }
}