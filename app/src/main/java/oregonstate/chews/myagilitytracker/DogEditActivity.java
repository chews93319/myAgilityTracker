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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*  References:
 *     [1a] https://dzone.com/articles/what-is-eventbus-library-and-how-does-it-work
 *     [1b] https://stackoverflow.com/questions/41582766/how-to-set-content-in-spinner-dynamicly-using-async-task
 *
 *
 * */

public class DogEditActivity extends AppCompatActivity {
    private static final String TAG = "DogEditActivity";

    Spinner mSpinner;

    String dogID = null;
    String userID = null;
    String dogNAME = null;
    String strURL = null;

    String tmpname;
    String tmpbreed;
    String tmpdate;
    String tmpsex;
    String tmpjump;
    String tmpuser;


    //  create OkHttpClient for http functionality
    private OkHttpClient mSaveEditClient;
    private OkHttpClient mUserListClient;
    private OkHttpClient mInitialInfoClient;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dogedit);


        Bundle extras = getIntent().getExtras();
        TextView mTitle = (TextView)findViewById(R.id.TitleEditDog);
        if (extras != null){
            dogID = extras.getString("dogid");

            //  Use DogEdit for 'Edit specific Dog' Mode (PATCH)
            if (dogID.length()>0) {
                strURL = "https://cs496-chewsfinal-agilityapi.appspot.com/dogs/"+dogID;

                mTitle.setText("Edit " + mTitle.getText());

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
                            JSONObject dog = new JSONObject(r);

                            tmpname = dog.getString("callname");
                            tmpbreed = dog.getString("breed");
                            tmpdate = dog.getString("birthdate");
                            tmpsex = dog.getString("gender");
                            tmpjump = dog.getString("jumpheight");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((EditText)findViewById(R.id.inputDogName)).setText(tmpname);
                                    ((EditText)findViewById(R.id.inputDogBreed)).setText(tmpbreed);
                                    ((EditText)findViewById(R.id.inputDogDOB)).setText(tmpdate);
                                    ((EditText)findViewById(R.id.inputDogSex)).setText(tmpsex);
                                    ((EditText)findViewById(R.id.inputDogJump)).setText(tmpjump);
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
                mTitle.setText("New " + mTitle.getText());
                userID = extras.getString("userid");

                //  the user id is not provided, then a selection is required
                if ((TextUtils.isEmpty(userID))|(userID.length()==0)){
                    ((LinearLayout)findViewById(R.id.selectHuman)).setVisibility(View.VISIBLE);
                    mSpinner = (Spinner)findViewById(R.id.inputHuman);
                    loadSpinnerData();
                    mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            String userchoice = mSpinner.getItemAtPosition(mSpinner.getSelectedItemPosition()).toString();
                            if (userchoice == "[Select a Name]"){
                                ((Button)findViewById(R.id.btnDogSave)).setVisibility(View.INVISIBLE);
                            } else {
                                ((Button)findViewById(R.id.btnDogSave)).setVisibility(View.VISIBLE);
                            }

                            //  Using String.split()
                            //    Ref:https://developer.android.com/reference/java/util/regex/Pattern#sum
                            String[] splitchoice = userchoice.split("\\p{Punct}");
                            tmpuser = splitchoice[1];
                            Log.d(TAG,tmpuser);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Do Nothing Here
                        }
                    });
                } else {
                    tmpuser = extras.getString("userid");
                }

                strURL = "https://cs496-chewsfinal-agilityapi.appspot.com/dogs";

            }
        }

        ((Button)findViewById(R.id.btnDogSave)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");

                tmpname=((EditText)findViewById(R.id.inputDogName)).getText().toString();
                tmpbreed=((EditText)findViewById(R.id.inputDogBreed)).getText().toString();
                tmpdate=((EditText)findViewById(R.id.inputDogDOB)).getText().toString();
                tmpsex=((EditText)findViewById(R.id.inputDogSex)).getText().toString();
                tmpjump=((EditText)findViewById(R.id.inputDogJump)).getText().toString();

                mSaveEditClient = new OkHttpClient();
                Request request;

                if (dogID.length()>0){
                    JSONObject mJ_new = new JSONObject();
                    try {
                        mJ_new.put("callname",tmpname);
                        mJ_new.put("birthdate",tmpdate);
                        mJ_new.put("gender",tmpsex);
                        mJ_new.put("jumpheight",Integer.parseInt(tmpjump));
                        mJ_new.put("breed",tmpbreed);
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
                        mJ_new.put("userid",tmpuser);
                        mJ_new.put("callname",tmpname);
                        mJ_new.put("birthdate",tmpdate);
                        mJ_new.put("gender",tmpsex);
                        mJ_new.put("jumpheight",Integer.parseInt(tmpjump));
                        mJ_new.put("breed",tmpbreed);
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
                DogEditActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                optnames));



        //  setup a url to pull an available user list
        String tmpURL = "https://cs496-chewsfinal-agilityapi.appspot.com/users";

        mUserListClient = new OkHttpClient();
        HttpUrl reqUrl = HttpUrl.parse(tmpURL);
        Request mrequest = new Request.Builder()
                .url(reqUrl)
                .build();
        mUserListClient.newCall(mrequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String r = response.body().string();

                try {
                    Log.d(TAG, r);
                    JSONArray users = new JSONArray(r);

                    for(int i = 0; i < users.length();i++){
                        String tmp = users.getJSONObject(i).getString("fullname");
                        tmp = tmp + "     (" + users.getJSONObject(i).getString("id");
                        optnames.add(tmp);
                    }



                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            }
        });

    }
}
