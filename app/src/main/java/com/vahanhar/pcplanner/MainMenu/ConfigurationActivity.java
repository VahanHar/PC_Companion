package com.vahanhar.pcplanner.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;

import com.vahanhar.pcplanner.CpuActivity.CpuActivity;
import com.vahanhar.pcplanner.GpuActivity.GpuActivity;
import com.vahanhar.pcplanner.MotherboardActivity;
import com.vahanhar.pcplanner.PSU.PsuActivity;
import com.vahanhar.pcplanner.R;
import com.vahanhar.pcplanner.Ram.RamActivity;
import com.vahanhar.pcplanner.StorageActivity;

public class ConfigurationActivity extends AppCompatActivity {

    private Button cpuButton;
    private Button gpuButton;
    private Button ramButton;
    private Button motherboardButton;
    private Button psuButton;
    private Button storageButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);


        SearchView searchView = findViewById(R.id.search_view);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(ContextCompat.getColor(this, R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search
                Toast.makeText(ConfigurationActivity.this, "Search: " + query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle text change
                return false;
            }
        });

        ImageView imageView = findViewById(R.id.btn_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        cpuButton = findViewById(R.id.cpu_button);
        gpuButton = findViewById(R.id.gpu_button);
        ramButton = findViewById(R.id.ram_button);
        motherboardButton = findViewById(R.id.motherboard_button);
        psuButton = findViewById(R.id.psu_button);
        storageButton = findViewById(R.id.storage_button);



        cpuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigurationActivity.this, CpuActivity.class));
            }
        });

        gpuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigurationActivity.this, GpuActivity.class));
            }
        });

        ramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigurationActivity.this, RamActivity.class));
            }
        });

        motherboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigurationActivity.this, MotherboardActivity.class));
            }
        });

        psuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigurationActivity.this, PsuActivity.class));
            }
        });
        storageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfigurationActivity.this, StorageActivity.class));
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
