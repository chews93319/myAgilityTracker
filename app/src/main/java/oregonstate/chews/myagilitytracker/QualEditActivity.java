package oregonstate.chews.myagilitytracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QualEditActivity extends AppCompatActivity {
    private static final String TAG = "QualEditActivity";

    Spinner mSpinner;

    String qualID = null;
    String dogID = null;
    String strURL = null;

    String tmpdog;
    String tmptype;
    String tmpdate;
    String tmpjudge;
    String tmpsct;
    String tmptime;


    //  create OkHttpClient for http functionality
    private OkHttpClient mSaveEditClient;
    private OkHttpClient mDogListClient;
    private OkHttpClient mInitialInfoClient;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualedit);


        Bundle extras = getIntent().getExtras();
        if (extras != null){
            qualID = extras.getString("qualid");

            //  Use QualEdit for 'Edit specific Qual' Mode (PATCH)
            if (qualID.length()>0) {
                strURL = "https://cs496-chewsfinal-agilityapi.appspot.com/quals/"+qualID;

                mInitialInfoClient = new OkHttpClient();
                HttpUrl reqUrl = HttpUrl.parse(strURL);
                Request mrequest = new Request.Builder()
                        .url(reqUrl)
                        .build();
                mInitialInfoClient.newCall(mrequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String r = response.body().string();

                        try {
                            Log.d(TAG, r);
                            JSONObject qual = new JSONObject(r);

                            tmptype = qual.getString("runtype");
                            tmpjudge = qual.getString("judge");
                            tmpdate = qual.getString("rundate");
                            tmpsct = qual.getString("sctime");
                            tmptime = qual.getString("qtime");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((EditText)findViewById(R.id.inputQType)).setText(tmptype);
                                    ((EditText)findViewById(R.id.inputQDate)).setText(tmpdate);
                                    ((EditText)findViewById(R.id.inputQJudge)).setText(tmpjudge);
                                    ((EditText)findViewById(R.id.inputQSCT)).setText(tmpsct);
                                    ((EditText)findViewById(R.id.inputQTime)).setText(tmptime);
                                }
                            });


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });



                //  Use DogEdit for 'Add New Dog' Mode (POST)
            } else {
                //  Customize the Activity Layout Views
                qualID = extras.getString("qualid");
                dogID = extras.getString("dogid");

                //  the user id is not provided, then a selection is required
                if ((TextUtils.isEmpty(dogID))|(dogID.length()==0)){
                    ((LinearLayout)findViewById(R.id.selectDog)).setVisibility(View.VISIBLE);
                    mSpinner = (Spinner)findViewById(R.id.inputDog);
                    loadSpinnerData();
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            String userchoice = mSpinner.getItemAtPosition(mSpinner.getSelectedItemPosition()).toString();
                            if (userchoice == "[Select a Name]"){
                                ((Button)findViewById(R.id.btnQualSave)).setVisibility(View.INVISIBLE);
                            } else {
                                ((Button)findViewById(R.id.btnQualSave)).setVisibility(View.VISIBLE);
                            }

                            //  Using String.split()
                            //    Ref:https://developer.android.com/reference/java/util/regex/Pattern#sum
                            String[] splitchoice = userchoice.split("\\p{Punct}");
                            tmpdog = splitchoice[1];
                            Log.d(TAG,tmpdog);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Do Nothing Here
                        }
                    });
                } else {
                    tmpdog = extras.getString("dogid");
                }

                strURL = "https://cs496-chewsfinal-agilityapi.appspot.com/quals";

            }
        }

        ((Button)findViewById(R.id.btnQualSave)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                tmptype=((EditText)findViewById(R.id.inputQType)).getText().toString();
                tmpdate=((EditText)findViewById(R.id.inputQDate)).getText().toString();
                tmpjudge=((EditText)findViewById(R.id.inputQJudge)).getText().toString();
                tmpsct=((EditText)findViewById(R.id.inputQSCT)).getText().toString();
                tmptime=((EditText)findViewById(R.id.inputQTime)).getText().toString();

                mSaveEditClient = new OkHttpClient();
                Request request;

                if (qualID.length()>0){
                    JSONObject mJ_new = new JSONObject();
                    try {
                        mJ_new.put("runtype",tmptype);
                        mJ_new.put("rundate",tmpdate);
                        mJ_new.put("judge",tmpjudge);
                        mJ_new.put("sctime",Integer.parseInt(tmpsct));
                        mJ_new.put("qtime",Integer.parseInt(tmptime));
                    } catch (JSONException ej1){
                        ej1.printStackTrace();
                    }

                    RequestBody mBody = RequestBody.create(JSON, mJ_new.toString());
                    request = new Request.Builder()
                            .url(strURL)
                            .patch(mBody)
                            .build();
                } else {
                    JSONObject mJ_new = new JSONObject();
                    try {
                        mJ_new.put("dogid",tmpdog);
                        mJ_new.put("runtype",tmptype);
                        mJ_new.put("rundate",tmpdate);
                        mJ_new.put("judge",tmpjudge);
                        mJ_new.put("sctime",Integer.parseInt(tmpsct));
                        mJ_new.put("qtime",Integer.parseInt(tmptime));
                    } catch (JSONException ej1){
                        ej1.printStackTrace();
                    }

                    RequestBody mBody = RequestBody.create(JSON, mJ_new.toString());
                    request = new Request.Builder()
                            .url(strURL)
                            .post(mBody)
                            .build();
                }






                mSaveEditClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String r = response.body().string();
                        Log.d(TAG, r);
                        finish();
                    }
                });
            }
        });

    }

    //  Using Dynamic Populating SpinnerData  [Ref.1a & Ref.1b]
    private void loadSpinnerData() {
        //  it is critical for the optnames to contain at least 1 element at time of creation
        final ArrayList<String> optnames = new ArrayList<>();
        optnames.add("[Select a Name]");

        mSpinner.setAdapter(new ArrayAdapter<String>(
                QualEditActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                optnames));



        //  setup a url to pull an available dog list
        String tmpURL = "https://cs496-chewsfinal-agilityapi.appspot.com/dogs";

        mDogListClient = new OkHttpClient();
        HttpUrl reqUrl = HttpUrl.parse(tmpURL);
        Request mrequest = new Request.Builder()
                .url(reqUrl)
                .build();
        mDogListClient.newCall(mrequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String r = response.body().string();

                try {
                    Log.d(TAG, r);
                    JSONArray dogs = new JSONArray(r);

                    for(int i = 0; i < dogs.length();i++){
                        String tmp = dogs.getJSONObject(i).getString("callname");
                        tmp = tmp + "     (" + dogs.getJSONObject(i).getString("id");
                        optnames.add(tmp);
                    }



                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            }
        });

    }
}

