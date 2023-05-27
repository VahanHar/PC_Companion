package com.example.pcplanner.CpuActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.MainMenu.DescriptionActivity;
import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class IntelActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private CollectionReference intelCollectionRef;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intel);

        firestore = FirebaseFirestore.getInstance();
        CollectionReference parentCollectionRef = firestore.collection("PC Components");
        DocumentReference cpuDocRef = parentCollectionRef.document("CPU");
        intelCollectionRef = cpuDocRef.collection("INTEL");

        listView = findViewById(R.id.listView);

        intelCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    List<String> documentIds = new ArrayList<>();
                    for (DocumentSnapshot document : documents) {
                        documentIds.add(document.getId());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(IntelActivity.this, R.layout.desc_list_item, documentIds);
                    listView.setAdapter(adapter);

                    // Add item click listener to open sub-collection documents
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String documentId = documentIds.get(position);
                            Intent intent = new Intent(IntelActivity.this, DescriptionActivity.class);
                            intent.putExtra("documentId", documentId);
                            intent.putExtra("collectionPath", "PC Components/CPU/INTEL");
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
