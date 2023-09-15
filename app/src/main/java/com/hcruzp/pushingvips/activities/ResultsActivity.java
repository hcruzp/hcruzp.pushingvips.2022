package com.hcruzp.pushingvips.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.hcruzp.pushingvips.R;
import com.hcruzp.pushingvips.views.ResultsView;

public class ResultsActivity extends Activity {

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_results);

		int widthPixels = getIntent().getExtras().getInt("widthPixels");
		int heightPixels = getIntent().getExtras().getInt("heightPixels");
		int killedBad = getIntent().getExtras().getInt("killedBad");
		int killedGood = getIntent().getExtras().getInt("killedGood");
		String[] pushedVipsArray = getIntent().getExtras().getStringArray("pushedVipsArray");

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

		Button btnRestart = (Button) findViewById(R.id.btnRestart);
		btnRestart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Button btnRestart =  (Button) findViewById(R.id.btnRestart);
				btnRestart.setVisibility(View.INVISIBLE);
				ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
				progressBar.setVisibility(View.VISIBLE);

				Intent gameIntent = new Intent(ResultsActivity.this, GameActivity.class);
				startActivity(gameIntent);
				finish();
			}
		});

		/* ------------ Google AdMobs ------------ */
		List<String> testDevices = new ArrayList<>();
		String testDevicePhone = getResources().getString(R.string.test_device_phone);
		String testDeviceTablet = getResources().getString(R.string.test_device_tablet);
		testDevices.add(testDevicePhone);	/* My Galaxy S8 	*/
		testDevices.add(testDeviceTablet);	/* My Galaxy Tab A	*/

		RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
//				.setTestDeviceIds(testDevices)
				.build();
		MobileAds.setRequestConfiguration(requestConfiguration);

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);
		mAdView.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// Code to be executed when an ad finishes loading.
				loadResultsView(widthPixels, heightPixels, pushedVipsArray);
			}

			@Override
			public void onAdFailedToLoad(LoadAdError adError) {
				// Code to be executed when an ad request fails.
				loadResultsView(widthPixels, heightPixels, pushedVipsArray);
			}

			@Override
			public void onAdOpened() {
				// Code to be executed when an ad opens an overlay that
				// covers the screen.
			}

			@Override
			public void onAdClicked() {
				// Code to be executed when the user clicks on an ad.
			}

			@Override
			public void onAdClosed() {
				// Code to be executed when the user is about to return
				// to the app after tapping on an ad.
			}
		});
		/* ------------ Google AdMobs ------------ */
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    //Handle the back button
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
			finishAffinity();
//        	android.os.Process.killProcess(android.os.Process.myPid());
	    	return true;
	    } else {
	    	return false;
	    }
	}

	private void loadResultsView(int widthPixels, int heightPixels, String[] pushedVipsArray) {
		Button btnRestart = (Button) findViewById(R.id.btnRestart);
		btnRestart.setVisibility(View.VISIBLE);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.INVISIBLE);

		ResultsView resultsView = new ResultsView(ResultsActivity.this, widthPixels, heightPixels);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, R.id.btnRestart);

		Bundle bndlPushedVips = new Bundle();
		bndlPushedVips.putStringArray("pushedVipsArray", pushedVipsArray);

		RelativeLayout rl = (RelativeLayout) findViewById(R.id.idLinearLayout);

		resultsView.setId((int) 97030478);
		resultsView.setLayoutParams(params);
		resultsView.setBundle(bndlPushedVips);
		rl.addView(resultsView);
	}
}
