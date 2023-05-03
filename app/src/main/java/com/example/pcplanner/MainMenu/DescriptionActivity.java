package com.example.pcplanner.MainMenu;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DescriptionActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        firestore = FirebaseFirestore.getInstance();

        String documentId = getIntent().getStringExtra("documentId");

        DocumentReference documentRef = firestore.collection("PC Components").document("CPU").collection("INTEL").document(documentId);

        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        //String #### = document.getString("NAME OF FIELD");
                        String name = document.getString("name");




                        TextView textView = findViewById(R.id.textView);



                        //textView.setText(HERE WRITE NAME)
                        textView.setText(name);
                    }
                } else {
                    // Handle error
                }
            }
        });
    }
}

