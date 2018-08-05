package oregonstate.chews.myagilitytracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

//import org.w3c.dom.Text;


public class AddLocationActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView mLatText;
    private TextView mLonText;
    private Location mLastLocation;
    private LocationListener mLocationListener;
    private static final int LOCATION_PERMISSION_RESULT = 17;

    //Create a Log TAG [ref: 1]
    private static final String TAG = "AddLocationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlocation);

        //Log Entry
        Log.d(TAG, "AddLocation onCreate: Started");

        mLatText = (TextView) findViewById(R.id.textLat);
        mLonText = (TextView) findViewById(R.id.textLon);

        //   Setup GoogleAPI and Location Functionality
        if(mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mLatText.setText("GoogleAPIClient Built");
        }

        //  Instantiate LocationRequest Object
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);

        //  Instantiate a custom LocationListener
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    mLonText.setText(String.valueOf(location.getLongitude()));
                    mLatText.setText(String.valueOf(location.getLatitude()));
                    //mLonText.setText("Location is Available");
                } else {
                    mLatText.setText("Location Information");
                    mLonText.setText("Temporarily Unavailable");
                }
            }
        };




    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        mLatText.setText("GoogleApi Started");
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLatText.setText("onConnected");


        //Check if either Location permissions have been granted
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)) {
            // Permission is already granted
            updateLocation();

        } else {
            // Permission is currently missing
            //   Use hardcoded values
            mLatText.setText("Permissions Missing");
            mLonText.setText("Using Hardcoded Data");
            mLatText.setText("44.5");
            mLonText.setText("-123.2");
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,mLocationListener);
    }
}
