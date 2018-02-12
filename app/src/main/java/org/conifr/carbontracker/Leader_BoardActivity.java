package org.conifr.carbontracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Leader_BoardActivity extends AppCompatActivity {
    String[] mobileArray = {"User1","User1","User1","User1",
            "User1","User1","User1","User1"};
    ListView lst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader__board);
        lst = (ListView) findViewById(R.id.mobile_list);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,  mobileArray  );


        lst.setAdapter(arrayAdapter);

    }
}
