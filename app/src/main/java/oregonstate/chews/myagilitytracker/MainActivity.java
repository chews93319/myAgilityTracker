package oregonstate.chews.myagilitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
}

