package com.example.pcplanner.MainMenu;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoreDescriptionActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String collectionpath;
    private String subdocumentId;
    private ListView subdocumentListView;
    private LinearLayout fieldListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moredescription);
        firestore = FirebaseFirestore.getInstance();
        subdocumentListView = findViewById(R.id.subdocument_listview);
        fieldListLayout = findViewById(R.id.field_list_layout);

        collectionpath = getIntent().getStringExtra("collectionpath");
        subdocumentId = getIntent().getStringExtra("subdocumentId");

        CollectionReference documentRef = firestore.collection(collectionpath).document(subdocumentId).collection("Characteristics");

        documentRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    List<String> subdocumentList = new ArrayList<>();
                    for (DocumentSnapshot document : documents) {
                        subdocumentList.add(document.getId());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MoreDescriptionActivity.this, android.R.layout.simple_list_item_1, subdocumentList);
                    subdocumentListView.setAdapter(adapter);

                    // Set click listener for subdocumentListView
                    subdocumentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Get the clicked subdocument
                            DocumentSnapshot subdocument = documents.get(position);

                            // Get the fields inside the subdocument
                            Map<String, Object> fields = subdocument.getData();

                            // Clear existing views from fieldListLayout
                            fieldListLayout.removeAllViews();

                            // Create and add TextViews for each field to fieldListLayout
                            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                                String field = entry.getKey() + ": " + entry.getValue();
                                TextView textView = new TextView(MoreDescriptionActivity.this);
                                textView.setText(field);
                                fieldListLayout.addView(textView);
                            }
                        }
                    });
                } else {
                    // Handle error
                }
            }
        });

    }
}
