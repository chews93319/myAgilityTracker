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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
//import java.util.List;
//import oregonstate.chews.myagilitytracker.R;




public class QualHistActivity extends AppCompatActivity {
    ArrayList<QualEvent> Quals;
    ArrayAdapter<String> QualsAdapter;
    ListView lvQuals;

    //Create a Log TAG [ref: 1]
    private static final String TAG = "QualHistActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualhist);

        //Log Entry
        Log.d(TAG, "QualHistory onCreate: Started");

        populateItems();

        lvQuals = (ListView) findViewById(R.id.listQuals);
        //QualsAdapter = new ArrayAdapter<>(this,
        //        android.R.layout.simple_list_item_1, Quals);

        QualEventListAdapter QualsAdapter = new QualEventListAdapter(this, R.layout.adapter_qlist, Quals);

        lvQuals.setAdapter(QualsAdapter);

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