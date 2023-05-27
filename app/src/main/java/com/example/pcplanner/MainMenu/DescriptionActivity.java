package com.example.pcplanner.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.CpuActivity.IntelActivity;
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

public class DescriptionActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String documentId;
    private String collectionPath;
    private ListView subdocumentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        firestore = FirebaseFirestore.getInstance();
        subdocumentListView = findViewById(R.id.subdocument_listview);


        documentId = getIntent().getStringExtra("documentId");
        collectionPath = getIntent().getStringExtra("collectionPath");

        CollectionReference documentRef = firestore.collection(collectionPath).document(documentId).collection("sub");


        documentRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    List<String> subdocumentList = new ArrayList<>();
                    for (DocumentSnapshot document : documents) {
                        subdocumentList.add(document.getId());
                    }
                    if(collectionPath.contains("INTEL")) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DescriptionActivity.this, R.layout.desc_list_item, subdocumentList);
                        subdocumentListView.setAdapter(adapter);
                    }
                    if(collectionPath.contains("AMD")) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DescriptionActivity.this, R.layout.desc_list_item2, subdocumentList);
                        subdocumentListView.setAdapter(adapter);
                    }




                    // Add item click listener to open sub-collection documents
                    subdocumentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            String subdocumentId = subdocumentList.get(position);
                            Intent intent = new Intent(DescriptionActivity.this, MoreDescriptionActivity.class);
                            intent.putExtra("subdocumentId", subdocumentId);
                            intent.putExtra("documentId", documentId);
                            intent.putExtra("collectionPath",collectionPath);
                            intent.putExtra("collectionpath", collectionPath + "/"+documentId + "/sub/");
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
