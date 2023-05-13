package com.example.pcplanner.MainMenu.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.CoolerActivity;

import com.example.pcplanner.GpuActivity.GpuActivity;
import com.example.pcplanner.MotherboardActivity;
import com.example.pcplanner.PsuActivity;
import com.example.pcplanner.R;
import com.example.pcplanner.RamActivity;
import com.example.pcplanner.StorageActivity;

public class LinkActivity extends AppCompatActivity {

    private Button cpuButton;
    private Button gpuButton;
    private Button ramButton;
    private Button motherboardButton;
    private Button psuButton;
    private Button storageButton;
    private Button coolerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);





        cpuButton = findViewById(R.id.cpu_button);
        gpuButton = findViewById(R.id.gpu_button);
        ramButton = findViewById(R.id.ram_button);
        motherboardButton = findViewById(R.id.motherboard_button);
        psuButton = findViewById(R.id.psu_button);
        storageButton = findViewById(R.id.storage_button);
        coolerButton = findViewById(R.id.cooler_button);


        cpuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LinkActivity.this, Cpu3Admin.class));
            }
        });

        gpuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LinkActivity.this, GpuActivity.class));
            }
        });

        ramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LinkActivity.this, RamActivity.class));
            }
        });

        motherboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LinkActivity.this, MotherboardActivity.class));
            }
        });

        psuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LinkActivity.this, PsuActivity.class));
            }
        });

        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LinkActivity.this, StorageActivity.class));
            }
        });

        coolerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LinkActivity.this, CoolerActivity.class));
            }
        });


    }

    // Handle back button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
