public class MainActivity extends Fragment {
  static final LatLng HAMBURG = new LatLng(53.558, 9.927);
  static final LatLng KIEL = new LatLng(53.551, 9.993);
  private GoogleMap map;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.map_fragment);
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
        .getMap();
    
    if (map!=null){
      Marker hamburg = map.addMarker(new MarkerOptions().position(HAMBURG)
          .title("Hamburg"));
    }
    
  //Move the camera instantly to hamburg with a zoom of 15.
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

    // Zoom in, animating the camera.
    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null); 


    
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }
}
  
  /*
  <?xml version="1.0" encoding="utf-8"?>
  <manifest xmlns:android="http://schemas.android.com/apk/res/android"
      package="com.vogella.android.locationapi.maps"
      android:versionCode="1"
      android:versionName="1.0" >

      <uses-sdk
          android:minSdkVersion="17"
          android:targetSdkVersion="17" />

      <permission
          android:name="com.vogella.android.locationapi.maps.permission.MAPS_RECEIVE"
          android:protectionLevel="signature" />

      <uses-feature
          android:glEsVersion="0x00020000"
          android:required="true" />

      <uses-permission android:name="com.vogella.android.locationapi.maps.permission.MAPS_RECEIVE" />
      <uses-permission android:name="android.permission.INTERNET" />
      <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
      <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
      <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

      <application
          android:allowBackup="true"
          android:icon="@drawable/ic_launcher"
          android:label="@string/app_name"
          android:theme="@style/AppTheme" >
          <activity
              android:name="com.vogella.android.locationapi.maps.MainActivity"
              android:label="@string/app_name" >
              <intent-filter>
                  <action android:name="android.intent.action.MAIN" />

                  <category android:name="android.intent.category.LAUNCHER" />
              </intent-filter>
          </activity>

          <meta-data
              android:name="com.google.android.maps.v2.API_KEY"
              android:value="your_apikey" />
      </application>

  </manifest>
  
  */