package com.example.pcplanner.MainMenu.Admin;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcplanner.CpuActivity.AmdActivity;
import com.example.pcplanner.CpuActivity.CpuActivity;
import com.example.pcplanner.CpuActivity.IntelActivity;
import com.example.pcplanner.MainMenu.Admin.AdminActivity;
import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CpuAdmin extends AdminActivity {

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
                startActivity(new Intent(CpuAdmin.this, IntelAdmin.class));
            }
        });
        Amd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CpuAdmin.this, AmdAdmin.class));
            }
        });
    }
}
