package oregonstate.chews.myagilitytracker;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

//import org.w3c.dom.Text;


public class AddLocationActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // GeoLocation Variable Set
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView mLatText;
    private TextView mLonText;
    private Location mLastLocation;
    private LocationListener mLocationListener;
    private static final int LOCATION_PERMISSION_RESULT = 17;

    // SQLite Variable Set
    AgilityDatabaseHelper agilityDBHelper;
    SQLiteDatabase mDB_EventsTbl;
    Cursor mSQLCursor;
    SimpleCursorAdapter mSQLCursorAdapter;
    Button mSQLSubmitButton;

    //Create a Log TAG [ref: 1]
    private static final String TAG = "AddLocationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlocation);

        //Log Entry
        Log.d(TAG, "AddLocation onCreate: Started");

        //  Setup SQLite Dbs
        agilityDBHelper = AgilityDatabaseHelper.getInstance(this);
        mDB_EventsTbl = agilityDBHelper.getWritableDatabase();



        //  Identify the effected Views
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

        populateLV_Events();

        mSQLSubmitButton = (Button) findViewById(R.id.btnAddLocation);
        mSQLSubmitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(mDB_EventsTbl != null){
                    ContentValues vals = new ContentValues();
                    vals.put(EventsTblContract.EventsTable.COL_EVENTNAME,
                            ((EditText)findViewById(R.id.inputEventName)).getText().toString());
                    vals.put(EventsTblContract.EventsTable.COL_LOCLAT, mLatText.getText().toString());
                    vals.put(EventsTblContract.EventsTable.COL_LOCLON, mLonText.getText().toString());

                    mDB_EventsTbl.insert(EventsTblContract.EventsTable.TABLE_NAME,null, vals);
                    populateLV_Events();

                    //  Clear the editText View after data insertion
                    ((EditText)findViewById(R.id.inputEventName)).setText("");
                } else {
                   Log.d(TAG, "Unable to access database for writing");
                }
            }
        });


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



    private void populateLV_Events(){
        if(mDB_EventsTbl != null) {
            try {
                if(mSQLCursorAdapter != null && mSQLCursorAdapter.getCursor() != null){
                    if(!mSQLCursorAdapter.getCursor().isClosed()){
                        mSQLCursorAdapter.getCursor().close();
                    }
                }
                mSQLCursor = mDB_EventsTbl.query(EventsTblContract.EventsTable.TABLE_NAME,
                        new String[]{EventsTblContract.EventsTable._ID,
                                EventsTblContract.EventsTable.COL_EVENTNAME,
                                EventsTblContract.EventsTable.COL_LOCLAT,
                                EventsTblContract.EventsTable.COL_LOCLON},
                        null,
                        null,
                        null, null,null);

                //  collect the ref to the destination View Object
                ListView SQLListView = (ListView) findViewById(R.id.sql_list_view);

                //  Instantiate an SQL Cursor Adaptor for a customized view
                //  current context, customized layout of each row, iterating cursor, source columns, respective destination views
                mSQLCursorAdapter = new SimpleCursorAdapter(this,
                        R.layout.sql_item,
                        mSQLCursor,
                        new String[]{EventsTblContract.EventsTable.COL_EVENTNAME,
                                EventsTblContract.EventsTable.COL_LOCLAT,
                                EventsTblContract.EventsTable.COL_LOCLON},
                        new int[]{R.id.sql_lv_col1, R.id.sql_lv_col2, R.id.sql_lv_col3},
                        0);
                SQLListView.setAdapter(mSQLCursorAdapter);
            } catch (Exception e) {
                Log.d(TAG, "Error loading data from database");
            }
        }
    }
}
