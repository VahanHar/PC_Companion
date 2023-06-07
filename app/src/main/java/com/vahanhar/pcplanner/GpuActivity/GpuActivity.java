package com.vahanhar.pcplanner.GpuActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.vahanhar.pcplanner.R;

public class GpuActivity extends AppCompatActivity {

    private Button IntelArc;
    private Button AmdRadeon;
    private Button Nvidia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu);


        // Find buttons by ID and set onClickListeners for each
        IntelArc = findViewById(R.id.intel_arc_button);
        AmdRadeon = findViewById(R.id.amd_radeon_button);
        Nvidia = findViewById(R.id.nvidia_button);

        ImageView imageView = findViewById(R.id.btn_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AmdRadeon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GpuActivity.this, AmdRadeonActivity.class));
            }
        });
        IntelArc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GpuActivity.this, IntelArcActivity.class));
            }
        });
        Nvidia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GpuActivity.this, NvidiaActivity.class));
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
