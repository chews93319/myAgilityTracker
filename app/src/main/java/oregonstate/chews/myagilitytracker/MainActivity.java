package oregonstate.chews.myagilitytracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*  References:
 *     [1] https://stackoverflow.com/questions/32748482/get-variable-from-another-activity-android-studio/32748568
 *
 *
 * */

public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_RESULT = 17;

    private TextView myDebugMsg;
    private String Userid;
    private String apiUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDebugMsg = (TextView) findViewById(R.id.myDebugMsg);

        //  Obtain the Userid from Google OAuth

        //

        Userid = "933191613";
        apiUserID = "ah1tfmNzNDk2LWNoZXdzZmluYWwtYWdpbGl0eWFwaXIRCxIEVXNlchiAgICQsLOCCQw";
        myDebugMsg.setText(Userid);
        checkLocationPermission();


        Button btnUserDogs = (Button) findViewById(R.id.btnSamDogs);
        btnUserDogs.setText("Sam's " + btnUserDogs.getText());
        btnUserDogs.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DogListActivity.class);
                intent.putExtra("userid",apiUserID);   // Ref [1]
                startActivity(intent);
            }
        });

        Button btnDogList = (Button) findViewById(R.id.btnDogList);
        btnDogList.setText("All " + btnDogList.getText());
        btnDogList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DogListActivity.class);
                intent.putExtra("userid","");   // Ref [1]
                startActivity(intent);
            }
        });

        Button btnQualList = (Button) findViewById(R.id.btnQualList);
        btnQualList.setText("All " + btnQualList.getText());
        btnQualList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QualHistActivity.class);
                intent.putExtra("dogid","");
                startActivity(intent);
            }
        });

        Button btnTitlesProg = (Button) findViewById(R.id.btnTitlesProg);
        btnTitlesProg.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TitleProgressActivity.class);
                startActivity(intent);
            }
        });

        Button btnTitlesDone = (Button) findViewById(R.id.btnTitlesDone);
        btnTitlesDone.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TitlesDoneActivity.class);
                startActivity(intent);
            }
        });

        Button btnAddLocation = (Button) findViewById(R.id.btnAddLocation);
        btnAddLocation.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddLocationActivity.class);
                startActivity(intent);
            }
        });

        Button btnGoogleAPI = (Button) findViewById(R.id.btnGoogleAPI);
        btnGoogleAPI.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, APIActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getApiUserID(){
        return apiUserID;
    }

    private void checkLocationPermission() {

        myDebugMsg = (TextView) findViewById(R.id.myDebugMsg);
        Button btnAddLocation = (Button) findViewById(R.id.btnAddLocation);

        //Check if either Location permissions have been granted
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)) {
            // Permission is already granted
            myDebugMsg.setText(Userid + " Pre-existing Permission");
            // Display and Enable ButtonView
            //btnAddLocation.setVisibility(View.VISIBLE);
        } else {
            // Permission is currently missing and must be requested
            myDebugMsg.setText(Userid + " No Existing Permission; Starting Request");
            // Display and Enable ButtonView
            //btnAddLocation.setVisibility(View.INVISIBLE);
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_RESULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {

        myDebugMsg = (TextView) findViewById(R.id.myDebugMsg);
        Button btnAddLocation = (Button) findViewById(R.id.btnAddLocation);

        switch (requestCode) {
            case LOCATION_PERMISSION_RESULT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myDebugMsg.setText(Userid + " Request Allowed");
                    btnAddLocation.setVisibility(View.VISIBLE);
                } else {
                    myDebugMsg.setText(Userid + " Request Denied");
                }
                return;
            }
        }
    }
}

