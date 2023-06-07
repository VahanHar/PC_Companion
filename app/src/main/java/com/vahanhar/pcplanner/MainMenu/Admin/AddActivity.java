package com.vahanhar.pcplanner.MainMenu.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.vahanhar.pcplanner.MotherboardActivity;
import com.vahanhar.pcplanner.PSU.PsuActivity;
import com.vahanhar.pcplanner.R;
import com.vahanhar.pcplanner.Ram.RamActivity;
import com.vahanhar.pcplanner.StorageActivity;

public class AddActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_add);





        cpuButton = findViewById(R.id.cpu_button);
        gpuButton = findViewById(R.id.gpu_button);
        ramButton = findViewById(R.id.ram_button);
        motherboardButton = findViewById(R.id.motherboard_button);
        psuButton = findViewById(R.id.psu_button);
        storageButton = findViewById(R.id.storage_button);


        cpuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, CpuAdmin.class));
            }
        });

        gpuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, GpuAdmin.class));
            }
        });

        ramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, RamActivity.class));
            }
        });

        motherboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, MotherboardActivity.class));
            }
        });

        psuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, PsuActivity.class));
            }
        });

        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddActivity.this, StorageActivity.class));
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
