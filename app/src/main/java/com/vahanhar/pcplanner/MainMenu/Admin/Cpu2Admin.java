package com.vahanhar.pcplanner.MainMenu.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.vahanhar.pcplanner.R;

public class Cpu2Admin extends AdminActivity {

    TextView textView;
    private Button Intel;
    private Button Amd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cpu_admin_activity);


        // Find buttons by ID and set onClickListeners for each
        Intel = findViewById(R.id.intel_button);
        Amd = findViewById(R.id.amd_button);
        textView = findViewById(R.id.add_panel_text);

        Intel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cpu2Admin.this, Intel2Admin.class));
            }
        });
        Amd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Cpu2Admin.this, Amd2Admin.class));
            }
        });
    }
}