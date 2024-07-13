package com.muhammadazzazy.unifinder;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UniversitiesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universities);

        final ArrayList<String> universityNames = new ArrayList<>();
        final ArrayList<String> universityDomains = new ArrayList<>();
        final ArrayList<String> universityWebpages = new ArrayList<>();

        final ListView listView = findViewById(R.id.universities_list);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, universityNames);
        listView.setAdapter(adapter);

        String country = getIntent().getStringExtra("country");
        System.out.println(country);

        if (country != null && country.contains(" ")) {
            String[] words = country.split(" ");
            String separator = "+";
            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 0; i < words.length; i++) {
                stringBuilder.append(words[i]);
                if (i < words.length - 1) {
                    stringBuilder.append(separator);
                }
            }
            country = stringBuilder.toString();
        }
        System.out.println(country);
        new FetchUniversitiesTask(this, universityNames, universityDomains, universityWebpages, adapter).execute(country);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = universityNames.get(position);
                String domain = universityDomains.get(position);
                String webpage = universityWebpages.get(position);
                Intent intent = new Intent(UniversitiesActivity.this, UniversityDetailsActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("domains", domain);
                intent.putExtra("web_pages", webpage);
                startActivity(intent);
            }
        });
    }
    private static class FetchUniversitiesTask extends AsyncTask<String, Void, String> {
        private final WeakReference<Context> contextRef;
        private final List<String> universityNames;
        private final List<String> universityDomains;
        private final List<String> universityWebpages;
        private final ArrayAdapter<String> adapter;

        public FetchUniversitiesTask(Context context, List<String> universityNames, List<String> universityDomains, List<String> universityWebpages, ArrayAdapter<String> adapter) {
            this.contextRef = new WeakReference<>(context);
            this.universityNames = universityNames;
            this.universityDomains = universityDomains;
            this.universityWebpages = universityWebpages;
            this.adapter = adapter;
        }

        @Override
        protected String doInBackground(String... params) {
            final String URL = "http://universities.hipolabs.com/search?country=";
            String country = params[0];
            String urlString = URL + country;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String response) {
            Context context = contextRef.get();
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    System.out.print(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonUniversityObject = jsonArray.getJSONObject(i);
                        String name = jsonUniversityObject.getString("name");
                        JSONArray domains = jsonUniversityObject.getJSONArray("domains");
                        JSONArray webpages = jsonUniversityObject.getJSONArray("web_pages");
                        System.out.println(name);
                        universityNames.add(name);
                        universityDomains.add(domains.getString(0));
                        universityWebpages.add(webpages.getString(0));
                        System.out.println(universityDomains.get(0));
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(context, "Something went wrong ", Toast.LENGTH_LONG).show();
            }
        }
    }
}