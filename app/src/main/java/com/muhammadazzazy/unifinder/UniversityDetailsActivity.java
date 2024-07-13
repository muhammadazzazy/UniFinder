package com.muhammadazzazy.unifinder;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UniversityDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_details);

        String name = getIntent().getStringExtra("name");
        TextView nameTextView = findViewById(R.id.university_name);
        nameTextView.setText(name);

        String domain = getIntent().getStringExtra("domains");
        TextView domainTextView = findViewById(R.id.university_domain);
        domainTextView.setText(domain);

        Button okButton = findViewById(R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button visitButton = findViewById(R.id.visit_button);
        visitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String webpage = getIntent().getStringExtra("web_pages");
                if (webpage != null && !webpage.isEmpty()) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webpage));
                    startActivity(browserIntent);
                }
            }
        });
    }
}