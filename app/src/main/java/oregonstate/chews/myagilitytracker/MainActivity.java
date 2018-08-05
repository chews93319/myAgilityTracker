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



public class MainActivity extends AppCompatActivity
        implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSION_RESULT = 17;

    private TextView myDebugMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDebugMsg = (TextView) findViewById(R.id.myDebugMsg);
        myDebugMsg.setText("Main onCreate Started");
        checkLocationPermission();


        Button btnDogList = (Button) findViewById(R.id.btnDogList);
        btnDogList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DogListActivity.class);
                startActivity(intent);
            }
        });

        Button btnQualList = (Button) findViewById(R.id.btnQualList);
        btnQualList.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QualHistActivity.class);
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
            myDebugMsg.setText("Pre-existing Permission");
            btnAddLocation.setVisibility(View.VISIBLE);
        } else {
            // Permission is currently missing and must be requested
            myDebugMsg.setText("No Existing Permission; Starting Request");
            btnAddLocation.setVisibility(View.INVISIBLE);
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
                    myDebugMsg.setText("Request Allowed");
                    btnAddLocation.setVisibility(View.VISIBLE);
                } else {
                    myDebugMsg.setText("Request Denied");
                }
                return;
            }
        }
    }
}

