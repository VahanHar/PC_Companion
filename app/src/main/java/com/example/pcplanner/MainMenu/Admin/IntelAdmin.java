package com.example.pcplanner.MainMenu.Admin;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class IntelAdmin extends AddActivity {

    TextInputEditText newSubcollectionEditText;
    Button addItemButton;
    FirebaseFirestore firestore;
    ListView documentListView;
    List<String> documentList = new ArrayList<>();
    ArrayAdapter<String> documentAdapter;
    CollectionReference subCollectionRef;
    int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cpu);

        firestore = FirebaseFirestore.getInstance();
        newSubcollectionEditText = findViewById(R.id.editText_itemName);
        addItemButton = findViewById(R.id.button_addItem);
        documentListView = findViewById(R.id.document_list_view);

        documentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, documentList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position == selectedPosition) {
                    int color = Color.parseColor("#87a96b");
                    view.setBackgroundColor(color);
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
                return view;
            }
        };

        documentListView.setAdapter(documentAdapter);

        CollectionReference parentCollectionRef = firestore.collection("PC Components");
        DocumentReference documentRef = parentCollectionRef.document("CPU");
        subCollectionRef = documentRef.collection("INTEL");

        subCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String documentName = document.getId();
                        documentList.add(documentName);
                    }
                    documentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Error getting documents.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        documentListView.setOnItemClickListener((parent, view, position, id) -> {
            String documentName = documentList.get(position);
            DocumentReference subDocumentRef = subCollectionRef.document(documentName);

            selectedPosition = position;
            documentAdapter.notifyDataSetChanged();

            //addBUTTON
            addItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newSubcollectionName = newSubcollectionEditText.getText().toString();

                    if (newSubcollectionName.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please enter collection name", Toast.LENGTH_SHORT).show();
                    } else {
                        subDocumentRef.collection(newSubcollectionName).document().set(new HashMap<String, Object>())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Collection added successfully", Toast.LENGTH_SHORT).show();
                                        newSubcollectionEditText.setText("");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error adding collection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });

        });

    }
}

