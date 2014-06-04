package com.freeman.cocos2dx;

import java.lang.ref.WeakReference;
import java.util.EnumSet;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.amazon.ags.api.AGResponseCallback;
import com.amazon.ags.api.AGResponseHandle;
import com.amazon.ags.api.AmazonGamesCallback;
import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.AmazonGamesFeature;
import com.amazon.ags.api.AmazonGamesStatus;
import com.amazon.ags.api.achievements.AchievementsClient;
import com.amazon.ags.api.achievements.UpdateProgressResponse;
import com.amazon.device.ads.AdError;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdListener;
import com.amazon.device.ads.AdProperties;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdSize;
import com.amazon.device.ads.AdTargetingOptions;

public class AmazonPlugin implements AdListener,AmazonGamesCallback {
	private static final String TAG = "AmazonPlugin";
	// reference to main activity
	private static WeakReference<Activity> mActivity;
	// reference to ad
	private static AdLayout mAdLayout;
	// reference to instance
	private static AmazonPlugin mInstance;
	// reference to container layout
	private static RelativeLayout mContainer;
	// reference to layout parameter
	private static RelativeLayout.LayoutParams params;
	
	//reference to the agsClient
	private static AmazonGamesClient agsClient;
	//list of features your game uses (in this example, achievements and leaderboards)
	private static EnumSet<AmazonGamesFeature> myGameFeatures = EnumSet.of(
		        AmazonGamesFeature.Achievements, AmazonGamesFeature.Leaderboards);
	
	public static void onCreate(Activity act){
		mActivity = new WeakReference<Activity>(act);
		mInstance = new AmazonPlugin();
		
		mContainer = new RelativeLayout(act);
		
		// set parameters
		params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		mContainer.setHorizontalGravity(Gravity.CENTER);
		mContainer.setVerticalGravity(Gravity.BOTTOM);
		// reference to main layout
		FrameLayout mainLayout = (FrameLayout)act.getWindow().getDecorView();
		// add ad layout to main layout
		mainLayout.addView(mContainer);
	}
	
	public static void onDestroy(){
		removeAd();
	}
	
	public static void onResume(){
		initGameClient();
	}
	
	public static void onPause(){
		releaseGameClient();
	}
	
	/**
	 * show the ad with appKey
	 * @param appKey
	 */
	public static void showAd(final String appKey){
		try{
			mActivity.get().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					AdRegistration.setAppKey("e2e62acaf429424faac4d2348e66fa2d");
					//AdRegistration.setAppKey(appKey);
					
					// Programmatically create the AmazonAdLayout      
					mAdLayout = new AdLayout(mActivity.get(), AdSize.SIZE_320x50);
					// add ad view
				    mContainer.addView(mAdLayout,params);
				    mAdLayout.setListener(mInstance);
				    mAdLayout.loadAd(new AdTargetingOptions()); // This AsyncTask retrieves an ad
				}
			});
		}catch(Exception e){
			Log.e(TAG,e.toString());
		}
	}
	
	/**
	 * remove the ad
	 */
	private static void removeAd(){
		try{
			mActivity.get().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mAdLayout.destroy();
				}
			});
		}catch(Exception e){
			Log.e(TAG,e.toString());
		}
	}
	
	/**
	 * init the game client
	 */
	private static void initGameClient(){
		try{
			mActivity.get().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					AmazonGamesClient.initialize(mActivity.get(), mInstance, myGameFeatures);
				}
			});
		}catch(Exception e){
			Log.e(TAG,e.toString());
		}
	}
	
	/**
	 * post the achievement to server
	 * @param id of achievement 
	 * @param val of achievement currently get
	 */
	public static void postAchievement(final String id,final float val){
		try{
			mActivity.get().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if(agsClient != null){
				    	// Replace YOUR_ACHIEVEMENT_ID with an actual achievement ID from your game.
				    	AchievementsClient acClient = agsClient.getAchievementsClient();
				    	AGResponseHandle<UpdateProgressResponse> handle = acClient.updateProgress("poweroftwo_firstlaunch", 100.0f);
				    	//AGResponseHandle<UpdateProgressResponse> handle = acClient.updateProgress(id, val);
				    	
				    	// Optional callback to receive notification of success/failure.
				    	handle.setCallback(new AGResponseCallback<UpdateProgressResponse>() {
					 
				    		@Override
				    		public void onComplete(UpdateProgressResponse result) {
				    			if (result.isError()) {
				    				// Add optional error handling here.  Not strictly required
				    				// since retries and on-device request caching are automatic.
				    				Log.e("GameCircle",result.getError().toString());
				    			} else {
				    				// Continue game flow.
				    				Log.d("GameCircle",result.toString());
				    			}
				    		}
				    	});
				    }
				}
			});
		}catch(Exception e){
			Log.e(TAG,e.toString());
		}
	}
	
	/**
	 * release the game client
	 */
	private static void releaseGameClient(){
		try{
			mActivity.get().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if (agsClient != null) {
						AmazonGamesClient.release();
						agsClient = null;
					}
				}
			});
		}catch(Exception e){
			Log.e(TAG,e.toString());
		}
	}
	
	//--------------------------------------------------------------------------
	// AdListener
	@Override
	public void onAdLoaded(AdLayout arg0, AdProperties arg1) {
		Log.d(TAG, arg1.getAdType().toString() + " Ad loaded successfully.");
	}
	
	@Override
	public void onAdFailedToLoad(AdLayout arg0, AdError arg1) {
		Log.w(TAG, "Ad failed to load. Code: " + arg1.getCode() + ", Message: " + arg1.getMessage());
	}
	
	@Override
	public void onAdExpanded(AdLayout arg0) {
		Log.d(TAG, "Ad expanded.");
	}
	
	@Override
	public void onAdCollapsed(AdLayout arg0) {
		Log.d(TAG, "Ad collapsed.");
	}

	
	//--------------------------------------------------------------------------
	// AmazonGamesCallback
	@Override
	public void onServiceNotReady(AmazonGamesStatus arg0) {
		Log.e(TAG,"onServiceNotReady");
	}

	@Override
	public void onServiceReady(AmazonGamesClient amazonGamesClient) {
		agsClient = amazonGamesClient;
	}
}
