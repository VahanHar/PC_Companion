package com.example.pcplanner.MainMenu;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SuggestPc extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String CPU_DOCUMENT_REF_PATH_KEY = "documentRefPath";


    private TextView cpuTextView;
    private TextView cpupriceTextView;
    private TextView finalpriceView;
    private TextView gpuTextView;
    private String documentRefPath;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestpc);

        cpuTextView = findViewById(R.id.cpu_textview);

        gpuTextView = findViewById(R.id.gpu_textview);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Retrieve the cpuDocumentRefPath from SharedPreferences
        documentRefPath = sharedPreferences.getString(CPU_DOCUMENT_REF_PATH_KEY, "");


        cpupriceTextView = findViewById(R.id.cpu_price);

        finalpriceView = findViewById(R.id.final_price);

        AtomicReference<Float> fprice = new AtomicReference<>((float) 0);

        // Retrieve the cpuDocumentRefPath from the intent
        documentRefPath = getIntent().getStringExtra("documentRefPath");

        // Set the subdocumentId as the text of the CPU TextView
        cpuTextView.setText(getSubdocumentId(documentRefPath));

        // Query the document in the "Characteristics" collection
        FirebaseFirestore.getInstance().document(documentRefPath).collection("Characteristics")
                .document("Characteristics")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve the map of fields
                        Map<String, Object> fields = documentSnapshot.getData();
                        if (fields != null) {
                            String pricee = null;

                            // Iterate through the fields
                            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                                String fieldName = entry.getKey();
                                Object fieldValue = entry.getValue();

                                // Check if the field value contains "$"
                                if (fieldValue instanceof String && ((String) fieldValue).contains("$")) {
                                    pricee = (String) fieldValue;
                                    break;

                                }
                            }

                            if (pricee != null) {
                                // Pass the documentRef path and pricee to the SuggestPc activity

                                Log.d("PPPP",pricee);
                                cpupriceTextView.setText(pricee);
                                // Remove "$" symbol from pricee and parse as float
                                String priceValue = pricee.substring(1);
                                fprice.set(Float.parseFloat(priceValue));
                                // Set finalpriceView to the price value
                                finalpriceView.setText(String.valueOf(fprice));

                            } else {
                                // No field with "$" found
                                // Handle the case where no field contains "$"
                            }
                        } else {
                            // No fields found
                            // Handle the case where no fields are available in the document
                        }
                    } else {
                        // Document does not exist
                        // Handle the case where the document does not exist
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure case
                    }
                });



    }
    @Override
    protected void onPause() {
        super.onPause();

        // Save the cpuDocumentRefPath to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CPU_DOCUMENT_REF_PATH_KEY, documentRefPath);
        editor.apply();
    }

    private String getSubdocumentId(String documentRefPath) {
        // Extract the subdocumentId from the CPU document reference path
        String[] pathComponents = documentRefPath.split("/");
        if (pathComponents.length >= 3) {
            String pathcomponents = pathComponents[3] +" "+ pathComponents[5];
            return pathcomponents;
        } else {
            return "";
        }
    }
}
