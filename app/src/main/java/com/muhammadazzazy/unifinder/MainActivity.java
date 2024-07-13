package com.muhammadazzazy.unifinder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.countries_list);
        final String[] countries = getResources().getStringArray(R.array.countries);
        int[] flags = {
                R.drawable.af_flag,
                R.drawable.al_flag,
                R.drawable.an_flag,
                R.drawable.ac_flag,
                R.drawable.am_flag,
                R.drawable.aj_flag,
                R.drawable.bf_flag,
                R.drawable.ba_flag
        };

        List<Map<String, Object>> data = new ArrayList<>();
        for (int i = 0; i < countries.length; i++) {
            Map<String, Object> datum = new HashMap<>();
            datum.put("image", flags[i]);
            datum.put("text", countries[i]);
            data.add(datum);
        }

        String[] from = {"image", "text"};
        int[] to = {R.id.image_view, R.id.text_view};

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.activity_list_item, from, to);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String country = countries[position];
                Intent intent = new Intent(MainActivity.this, UniversitiesActivity.class);
                intent.putExtra("country", country);
                startActivity(intent);
            }
        });
    }
}