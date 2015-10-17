package com.anddevbg.lawa.ui.activity.weather;


import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.anddevbg.lawa.R;
import com.anddevbg.lawa.adapter.WeatherFragmentAdapter;
import com.anddevbg.lawa.animation.ZoomPagerTransformation;
import com.anddevbg.lawa.database.WeatherDatabaseManager;
import com.anddevbg.lawa.model.SearchActivity;
import com.anddevbg.lawa.model.WeatherData;
import com.anddevbg.lawa.networking.Connectivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class WeatherActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {


    private WeatherFragmentAdapter mWeatherAdapter;
    private ViewPager mViewPager;
    private List<WeatherData> mResult;
    private int search_request_code = 1;
    //    private SearchView searchView;
    private double mLocationLatitude;
    private double mLocationLongitude;
    private GoogleApiClient mGoogleClient;
    private Location mLastKnownLocation;
    private WeatherDatabaseManager mWeatherDataBaseManager;
    private ShareButton mFacebookShareButton;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;

    private Bitmap mScreenshotBitmap;
    private int counter = 0;

    private static final String TAG = "connectiontest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        setContentView(R.layout.activity_weather);
        mResult = new ArrayList<>();
        initControls();
        mFacebookShareButton = (ShareButton) findViewById(R.id.button_facebook_share);
        mFacebookShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("facebook", "facebook share button clicked");
//                postPicture();
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("My App")
                        .setContentDescription("My Desc")
                        .setContentUrl(Uri.parse("https://myapp.net"))
                        .setImageUrl(Uri.parse("https://myapp.net/12.png"))
                        .build();
                mFacebookShareButton.setShareContent(linkContent);
            }
        });
        mFacebookShareButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.d("facebook", "success");
            }

            @Override
            public void onCancel() {
                Log.d("facebook", "cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebook", "error");
            }
        });
        isInternetEnabled();
    }

    private void postPicture() {
        if (counter == 0) {
            Log.d("facebook", "preparing screenshot");
            View rootView = findViewById(android.R.id.content).getRootView();
            rootView.setDrawingCacheEnabled(true);
            mScreenshotBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
            rootView.destroyDrawingCache();

            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Share content to Facebook");
            dialog.setMessage("Share image to Faccebok?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharePhoto photo = new SharePhoto.Builder().setBitmap(mScreenshotBitmap).build();
                    SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                    mFacebookShareButton.setShareContent(content);
                    counter = 1;
                    mFacebookShareButton.performClick();
                }
            });
            dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialog.show();
        } else
        {
            Log.d("facebook", "in else");
            counter = 0;
            mFacebookShareButton.setShareContent(null);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int widgetPosition = extras.getInt("position", 0);
            Log.d("widget", "widgett position in onNewIntent" + widgetPosition);
            mViewPager.setCurrentItem(widgetPosition);
        }
    }


    private void isInternetEnabled() {
        final Connectivity connectivity = Connectivity.getInstance(this);
        if (connectivity.isConnected()) {
            setUpGoogleApiClient();
            getManagerAndShowData();

        } else {
            CharSequence positive = "Enable";
            CharSequence negative = "Cancel";
            new AlertDialog.Builder(this)
                    .setTitle("No connection available")
                    .setMessage("Please enable internet access to continue")
                    .setNegativeButton(negative, null)
                    .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                        }
                    }).show();

            Log.d(TAG, "between AlertDialog and Thread");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (!connectivity.isConnected()) {
                            Thread.sleep(1000);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setUpGoogleApiClient();
                                getManagerAndShowData();
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

    private void getManagerAndShowData() {
        mWeatherDataBaseManager = WeatherDatabaseManager.getInstance();
        mResult = mWeatherDataBaseManager.showAll();
        mWeatherAdapter.setWeatherData(mResult);
    }

    private void setUpGoogleApiClient() {
        mGoogleClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onSearchRequested() {
        Intent i = new Intent(this, SearchActivity.class);
//        i.putExtra("name", searchView.getQuery());
        startActivity(i);
        return super.onSearchRequested();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weather, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent searchActivityIntent = new Intent(WeatherActivity.this, SearchCityActivity.class);
                startActivityForResult(searchActivityIntent, search_request_code);
                break;
            case R.id.action_remove:
                new AlertDialog.Builder(this)
                        .setTitle("Delete this city?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        })
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int index = mViewPager.getCurrentItem();
                                String cityNameForDeletion = mResult.get(index).getCityName();
                                mWeatherDataBaseManager.deleteData(cityNameForDeletion);
                                mWeatherAdapter.removeView(index);
                                mResult.remove(index);
                                mWeatherAdapter.notifyDataSetChanged();
                                Toast.makeText(WeatherActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case R.id.action_location:
                WeatherData currentLocationWeatherData = new WeatherData();
                if (mLastKnownLocation != null) {
                    currentLocationWeatherData.setLatitude(mLastKnownLocation.getLatitude());
                    currentLocationWeatherData.setLongitude(mLastKnownLocation.getLongitude());
                    mResult.add(currentLocationWeatherData);
                    mWeatherAdapter.setWeatherData(mResult);
                    mViewPager.setCurrentItem(mResult.size());
                    mWeatherDataBaseManager.insertData("city", mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude());
                } else {
                    Toast.makeText(this, "Unknown location. Please try again.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == search_request_code) {
            if (resultCode == RESULT_OK) {
                String locationName = data.getStringExtra("c1name");
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(locationName, 1);
                    Address address = addresses.get(0);
                    mLocationLongitude = address.getLongitude();
                    mLocationLatitude = address.getLatitude();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                WeatherData data1 = new WeatherData();
                data1.setLongitude(mLocationLongitude);
                data1.setLatitude(mLocationLatitude);
                data1.setCityName(locationName);
                mResult.add(data1);
                mWeatherAdapter.setWeatherData(mResult);
                mViewPager.setCurrentItem(mResult.size());
                mWeatherDataBaseManager.insertData(locationName, mLocationLatitude, mLocationLongitude);
            }
        }
    }

    private void initControls() {
        mWeatherAdapter = new WeatherFragmentAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(mWeatherAdapter);
        mViewPager.setOffscreenPageLimit(5);
        mViewPager.setPageTransformer(false, new ZoomPagerTransformation());
        Intent widgetIntent = getIntent();
        Bundle extras = widgetIntent.getExtras();
        if (extras != null) {
            int widgetPosition = extras.getInt("position", 0);
            Log.d("widget", "widgetPosition is " + widgetIntent);
            mViewPager.setCurrentItem(widgetPosition);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient, this);
        }
    }

}

