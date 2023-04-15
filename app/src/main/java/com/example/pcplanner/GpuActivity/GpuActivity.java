package com.example.pcplanner.GpuActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.R;

public class GpuActivity extends AppCompatActivity {

    private Button Intel;
    private Button Amd;
    private Button Nvidia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // enable back button

        // Find buttons by ID and set onClickListeners for each
        Nvidia = findViewById(R.id.nvidia_button);
        Amd = findViewById(R.id.amdradeon_button);
        Intel = findViewById(R.id.intelarc_button);



        Nvidia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GpuActivity.this, NvidiaActivity.class));
            }
        });
        Amd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GpuActivity.this, AmdRadeonActivity.class));
            }
        });
        Intel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GpuActivity.this, IntelArcActivity.class));
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
