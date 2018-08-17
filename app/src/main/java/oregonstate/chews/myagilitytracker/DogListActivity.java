package oregonstate.chews.myagilitytracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

/*  References:
*     [1] https://stackoverflow.com/questions/32748482/get-variable-from-another-activity-android-studio/32748568
*
*
* */

public class DogListActivity extends AppCompatActivity {
    //Create a Log TAG
    private static final String TAG = "APIActivity";

    String userID = null;
    ListView lvDogs;

    //  create OkHttpClient for http functionality
    private OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doglist);

        // Ref [1]
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            userID = extras.getString("userid");
        }

        ((TextView)findViewById(R.id.tv_apiUserId)).setText(userID);

        mOkHttpClient = new OkHttpClient();
        HttpUrl reqUrl = HttpUrl.parse("https://cs496-chewsfinal-agilityapi.appspot.com/dogs/?userID="+userID);
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
                Log.d(TAG, "https://cs496-chewsfinal-agilityapi.appspot.com/dogs/?userID="+userID);
                Log.d(TAG, r);

                try {
                    //JSONObject j = new JSONObject(r);
                    JSONArray dogs = new JSONArray(r);

                    List<Map<String,String>> names = new ArrayList<>();
                    for(int i = 0; i < dogs.length(); i++){
                        HashMap<String, String> m = new HashMap<>();
                        m.put("name", dogs.getJSONObject(i).getString("callname"));
                        m.put("dob",dogs.getJSONObject(i).getString("birthdate"));
                        m.put("id",dogs.getJSONObject(i).getString("id"));
                        names.add(m);
                    }
                    for(int i=0; i < names.size();i++){
                        Log.d(TAG, names.get(i).toString());
                    }

                    final SimpleAdapter namesAdapter = new SimpleAdapter(
                            DogListActivity.this,
                            names,
                            R.layout.dogname_item,
                            new String[]{"name","dob","id"},
                            new int[]{R.id.dogname_text, R.id.dogdob_text, R.id.dogid_text});


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {lvDogs = (ListView)findViewById(R.id.doglist_names_list);
                            lvDogs.setAdapter(namesAdapter);
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
        lvDogs.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View item, int pos, long id) {
                        Log.d(TAG, ((TextView) item.findViewById(R.id.dogid_text)).getText().toString());
                        return true;
                    }
                }
        );

        lvDogs.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View item, int pos, long id) {
                        Log.d(TAG, ((TextView) item.findViewById(R.id.dogid_text)).getText().toString());
                    }
                }

        );
    }
}
