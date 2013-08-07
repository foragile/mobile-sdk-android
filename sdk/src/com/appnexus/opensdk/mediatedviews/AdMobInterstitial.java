package com.appnexus.opensdk.mediatedviews;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;

import com.appnexus.opensdk.MediatedInterstitialAdView;
import com.appnexus.opensdk.MediatedInterstitialAdViewController;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

public class AdMobInterstitial implements MediatedInterstitialAdView,
		AdListener {
	InterstitialAd iad;
	MediatedInterstitialAdViewController mMediatedInterstitialAdViewController;
	
	public AdMobInterstitial() {
	}

	@Override
	public View requestAd(MediatedInterstitialAdViewController mIC, Activity activity, String parameter, String uid) {
		 iad = new InterstitialAd(activity, uid);

		AdRequest ar = new AdRequest();

		ar.addTestDevice(((TelephonyManager) activity.getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());

		iad.setAdListener(this);
		
		iad.loadAd(ar);
		mMediatedInterstitialAdViewController=mIC;
		return null;
	}

	@Override
	public void onReceiveAd(Ad ad) {
		mMediatedInterstitialAdViewController.onAdLoaded();
	}

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		mMediatedInterstitialAdViewController.onAdFailed();
		
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		if(iad!=null){
			iad.show();
		}
	}

}