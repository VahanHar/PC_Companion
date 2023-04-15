package com.example.pcplanner.CpuActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.R;

public class CpuActivity extends AppCompatActivity {

        private Button Intel;
        private Button Amd;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cpu);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back button

            // Find buttons by ID and set onClickListeners for each
            Intel = findViewById(R.id.intel_button);
            Amd = findViewById(R.id.amd_button);


            Intel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CpuActivity.this, IntelActivity.class));
                }
            });
            Amd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CpuActivity.this, AmdActivity.class));
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
