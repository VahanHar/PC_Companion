package com.vahanhar.pcplanner.PSU;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vahanhar.pcplanner.MainMenu.DescriptionActivity;
import com.vahanhar.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class cheap extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private CollectionReference intelCollectionRef;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intel);

        firestore = FirebaseFirestore.getInstance();




        CollectionReference parentCollectionRef = firestore.collection("PC Components");
        DocumentReference cpuDocRef = parentCollectionRef.document("PSU");
        intelCollectionRef = cpuDocRef.collection("Cheap");

        listView = findViewById(R.id.listView);


        ImageView imageView = findViewById(R.id.btn_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        intelCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    List<String> documentIds = new ArrayList<>();
                    for (DocumentSnapshot document : documents) {
                        documentIds.add(document.getId());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(cheap.this, R.layout.desc_list_item, documentIds);
                    listView.setAdapter(adapter);

                    // Add item click listener to open sub-collection documents
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String documentId = documentIds.get(position);
                            Intent intent = new Intent(cheap.this, DescriptionActivity.class);
                            intent.putExtra("documentId", documentId);
                            intent.putExtra("collectionPath", "PC Components/PSU/Cheap");
                            startActivity(intent);
                        }
                    });
                } else {
                    // Handle error
                }
            }
        });
    }
}