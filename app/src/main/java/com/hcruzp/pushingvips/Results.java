package com.hcruzp.pushingvips;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Results extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.results);

		int killedBad = getIntent().getExtras().getInt("killedBad");
		int killedGood = getIntent().getExtras().getInt("killedGood");
		String[] pushedVipsArray = getIntent().getExtras().getStringArray("pushedVipsArray");

//		List<String> pushedVips = Arrays.asList(pushedVipsArray);
		int killedTilin = pushedVipsArray.length;

        int totalKilledGood = killedGood * 7;
        int totalKilledTilin = killedTilin * 5;
        int total = killedBad - (totalKilledGood) + totalKilledTilin;

		TextView tvBad = (TextView) findViewById(R.id.txtRecordBad);
		tvBad.setTypeface(null, Typeface.BOLD);
		tvBad.setText("✓ : " + killedBad + " = " + killedBad);
		TextView tvGood = (TextView) findViewById(R.id.txtRecordGood);
		tvGood.setTypeface(null, Typeface.BOLD);
		tvGood.setText("✗: " + killedGood + " = " + (totalKilledGood > 0 ? "-" : "") + totalKilledGood);
		TextView tvTilin = (TextView) findViewById(R.id.txtRecordTilin);
		tvTilin.setTypeface(null, Typeface.BOLD);
		tvTilin.setText("VIPs: " + killedTilin + " = " + totalKilledTilin);
		TextView tvTotal = (TextView) findViewById(R.id.txtRecordTotal);
		tvTotal.setTypeface(null, Typeface.BOLD);
		tvTotal.setText("Total: " + total);
		ViewFlipper mFlipper = (ViewFlipper) findViewById(R.id.myViewFlipper);

/*		RelativeLayout rl = (RelativeLayout) findViewById(R.id.idLinearLayout);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, R.id.myViewFlipper);

		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.vips_eso_tilin_32_g);
		imageView.setLayoutParams(params);
		rl.addView(imageView);*/

		ResultsView resultsView = new ResultsView(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, R.id.myViewFlipper);

		Bundle bndlPushedVips = new Bundle();
		bndlPushedVips.putStringArray("pushedVipsArray", pushedVipsArray);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.idLinearLayout);

		int id = 97030478;
		resultsView.setId(id);
		resultsView.setLayoutParams(params);
		resultsView.setBundle(bndlPushedVips);
		rl.addView(resultsView);

		Button btnRestart = (Button) findViewById(R.id.btnRestart);
		btnRestart.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
/*				Intent intent = new Intent(Main.this, Results.class);
//				intent.putExtra("thetext", et.getText().toString());
				startActivity(intent);*/

				setContentView(new GameView(Results.this));
				mFlipper.showNext();
//				finish();
				return false;
			}
		});

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    //Handle the back button
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	    	this.finish();
	    	return true;
	    } else {
	    	return false;
	    }
	}
	/*
	@Override
	public void onStop() {
		System.out.println("LLEGA onStop");
//        if (isFinishing()) {
//        	super.onStop();
        	android.os.Process.killProcess(android.os.Process.myPid());
        //}
	}*/
}
