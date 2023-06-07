package com.vahanhar.pcplanner.PSU;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.vahanhar.pcplanner.R;

public class PsuActivity extends AppCompatActivity {

    private Button cheap;
    private Button medium;
    private Button expensive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psu);


        // Find buttons by ID and set onClickListeners for each
        cheap = findViewById(R.id.cheap_button);
        medium = findViewById(R.id.medium_button);
        expensive = findViewById(R.id.expensive_button);

        ImageView imageView = findViewById(R.id.btn_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cheap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PsuActivity.this, cheap.class));
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PsuActivity.this, medium.class));
            }
        });
        expensive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PsuActivity.this, expensive.class));
            }
        });
    }
    // Handle back button press
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
