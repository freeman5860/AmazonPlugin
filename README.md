AmazonPlugin
============

Implement amazon ad and game circle wrapper for cocos2d-x

How to integrate:

1. copy "GameCircleSDK" folder to proj.android folder
2. copy "amazon-ads-5.1.236.jar" to proj.android/libs folder
3. copy "AmazonPlugin.java" to proj.android/src/com/freeman/cocos2dx folder
3. copy "AmazonWrapper.h" to Class folder
4. copy "AmazonWrapper.cpp" to proj.android/jni folder
5. edit Android.mk under proj.android/jni folder, add "AmazonWrapper.cpp"
6. edit AndroidManifest.xml, type in the content:

	<activity android:name="com.amazon.ags.html5.overlay.GameCircleUserInterface"
                 android:theme="@style/GCOverlay" android:hardwareAccelerated="false"></activity>
        <activity
                 android:name="com.amazon.identity.auth.device.authorization.AuthorizationActivity"
                 android:theme="@android:style/Theme.NoDisplay"
                 android:allowTaskReparenting="true"
                 android:launchMode="singleTask">
                 <intent-filter>
                     <action android:name="android.intent.action.VIEW" />
                     <category android:name="android.intent.category.DEFAULT" />
                     <category android:name="android.intent.category.BROWSABLE" />
                     <data android:host="com.eureka.poweroftwo" android:scheme="amzn" />
                 </intent-filter>
        </activity>
        <activity android:name="com.amazon.ags.html5.overlay.GameCircleAlertUserInterface"
                 android:theme="@style/GCAlert" android:hardwareAccelerated="false"></activity>
        <receiver
                 android:name="com.amazon.identity.auth.device.authorization.PackageIntentReceiver"
                 android:enabled="true">
                 <intent-filter>
                     <action android:name="android.intent.action.PACKAGE_INSTALL" />
                     <action android:name="android.intent.action.PACKAGE_ADDED" />
                     <data android:scheme="package" />
                 </intent-filter>
        </receiver>
        <activity android:name="com.amazon.device.ads.AdActivity" android:configChanges="keyboardHidden|orientation|screenSize"/>
        
7. on the Activity call:

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);	
        AmazonPlugin.onCreate(this);
	}
    
    @Override
	public void onResume() {
	    super.onResume();
        AmazonPlugin.onResume();
    }
    
    @Override
	public void onPause() {
	    super.onPause();
        AmazonPlugin.onPause();
    }
    
    @Override
	public void onDestroy() {
	    super.onDestroy();
        AmazonPlugin.onDestroy();
    }
    
8. import GameCircleSDK library