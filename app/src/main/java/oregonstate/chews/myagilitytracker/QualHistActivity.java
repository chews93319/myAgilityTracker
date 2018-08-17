/*
* Reference List
*
* 1. CodingWithMitch Android Beginner Tutorial #8 - Custom ListView Adapter]
*    https://www.youtube.com/watch?annotation_id=annotation_3104328239&feature=iv&src_vid=8K-6gdTlGEA&v=E6vE8fqQPTE
*
*
*
* */



package oregonstate.chews.myagilitytracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
//import java.util.List;
//import oregonstate.chews.myagilitytracker.R;




public class QualHistActivity extends AppCompatActivity {
    //Create a Log TAG [ref: 1]
    private static final String TAG = "QualHistActivity";

    ArrayList<QualEvent> Quals;
    ArrayAdapter<String> QualsAdapter;
    String dogID = null;
    String dogNAME = null;
    String strURL = null;
    ListView lvQuals;

    //  create OkHttpClient for http functionality
    private OkHttpClient mOkHttpClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualhist);

        //Log Entry
        Log.d(TAG, "QualHistory onCreate: Started");

        //  parse the extras bundle
        Bundle extras = getIntent().getExtras();
        TextView mTitle = (TextView)findViewById(R.id.TitleQualHist);
        if (extras != null){
            dogID = extras.getString("dogid");
            Log.d(TAG,dogID);
            if (dogID.length()>0) {
                strURL = "https://cs496-chewsfinal-agilityapi.appspot.com/quals/?dogID="+dogID;
                Log.d(TAG, strURL);

                dogNAME = extras.getString("dogname");
                mTitle.setText(dogNAME + "'s " + mTitle.getText());
            } else {
                strURL = "https://cs496-chewsfinal-agilityapi.appspot.com/quals";
                Log.d(TAG, strURL);

                mTitle.setText("All " + mTitle.getText());
            }
        }

        mOkHttpClient = new OkHttpClient();
        HttpUrl reqUrl = HttpUrl.parse(strURL);
        Request mrequest = new Request.Builder()
                .url(reqUrl)
                .build();
        mOkHttpClient.newCall(mrequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String r = response.body().string();
                Quals = new ArrayList<>();

                try {
                    Log.d(TAG, r);

                    JSONArray quals = new JSONArray(r);
                    for(int i = 0; i < quals.length(); i++){

                        String tmpname = quals.getJSONObject(i).getString("dogname");
                        String tmptype = quals.getJSONObject(i).getString("runtype");
                        String tmpdate = quals.getJSONObject(i).getString("rundate");
                        String tmpscore = quals.getJSONObject(i).getString("qtime");

                        //  build a qualevent object
                        QualEvent tmp = new QualEvent(
                                tmpname,
                                tmptype,
                                tmpdate,
                                tmpscore);

                        //  add tmp object to array
                        Quals.add(tmp);
                    }

                    for(int i = 0; i < Quals.size(); i++){
                        Log.d(TAG,Quals.get(i).getDogname());
                        Log.d(TAG,Quals.get(i).getDate());
                        Log.d(TAG,Quals.get(i).getGame());
                        Log.d(TAG,Quals.get(i).getPoints());
                    }

                    //  populate hardcoded information (for debug testing)
                    //populateItems();

                    final QualEventListAdapter QualsAdapter = new QualEventListAdapter(
                            QualHistActivity.this,
                            R.layout.adapter_qlist,
                            Quals);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lvQuals = (ListView) findViewById(R.id.listQuals);
                            lvQuals.setAdapter(QualsAdapter);
                            setupListViewListener();
                        }
                    });


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public void setupListViewListener(){
        lvQuals.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View item, int pos, long id) {
                        Log.d(TAG, ((TextView) item.findViewById(R.id.textView1)).getText().toString());
                        return true;
                    }
                }
        );

        lvQuals.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View item, int pos, long id) {
                        String apiDogID = ((TextView) item.findViewById(R.id.textView1)).getText().toString();
                        String apiDogName = ((TextView) item.findViewById(R.id.textView2)).getText().toString();

                        /*
                        //  Open QualHistory for the specific Dog
                        Intent intent = new Intent(DogListActivity.this, QualHistActivity.class);
                        intent.putExtra("dogid",apiDogID);
                        intent.putExtra("dogname",apiDogName);
                        startActivity(intent);
                        */
                    }
                }

        );
    }

    private void populateItems() {
        QualEvent new00 = new QualEvent("Luna","JWW","18/7/11","15");
        QualEvent new01 = new QualEvent("Luna","JWW","18/7/12","17");
        QualEvent new02 = new QualEvent("Luna","STD","18/7/11","6");
        QualEvent new03 = new QualEvent("Luna","STD","18/7/12","7");
        QualEvent new04 = new QualEvent("Sola","FAST","18/7/11","--");
        QualEvent new05 = new QualEvent("Usher","JWW","18/7/11","18");
        QualEvent new06 = new QualEvent("Usher","JWW","18/7/12","17");
        QualEvent new07 = new QualEvent("Usher","STD","18/7/11","5");
        QualEvent new08 = new QualEvent("Usher","STD","18/7/12","8");
        QualEvent new09 = new QualEvent("Mynx","JWW","18/7/11","25");
        QualEvent new10 = new QualEvent("Mynx","JWW","18/7/12","25");
        QualEvent new11 = new QualEvent("Mynx","STD","18/7/11","9");
        QualEvent new12 = new QualEvent("Mynx","STD","18/7/12","10");
        QualEvent new13 = new QualEvent("Bling","JWW","18/7/11","9");
        QualEvent new14 = new QualEvent("Bling","JWW","18/7/12","12");
        QualEvent new15 = new QualEvent("Bling","STD","18/7/11","11");
        QualEvent new16 = new QualEvent("Bling","STD","18/7/12","12");

        Quals = new ArrayList<>();
        Quals.add(new00);
        Quals.add(new01);
        Quals.add(new02);
        Quals.add(new03);
        Quals.add(new04);
        Quals.add(new05);
        Quals.add(new06);
        Quals.add(new07);
        Quals.add(new08);
        Quals.add(new09);
        Quals.add(new10);
        Quals.add(new11);
        Quals.add(new12);
        Quals.add(new13);
        Quals.add(new14);
        Quals.add(new15);
        Quals.add(new16);
    }
}