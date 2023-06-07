package com.vahanhar.pcplanner.MainMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vahanhar.pcplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class SuggestPc extends AppCompatActivity {

    private static final String PREFS_NAME = "MyPrefs";
    private static final String CPU_DOCUMENT_REF_PATH_KEY = "documentRefPath";
    private static final String GPU_DOCUMENT_REF_PATH_KEY = "documentRefPath2";
    private static final String RAM_DOCUMENT_REF_PATH_KEY = "documentRefPath3";
    private static final String MOTHERBOARD_DOCUMENT_REF_PATH_KEY = "documentRefPath4";
    private static final String PSU_DOCUMENT_REF_PATH_KEY = "documentRefPath5";
    private static final String STORAGE_DOCUMENT_REF_PATH_KEY = "documentRefPath6";


    private static final String CPU_PRICE = "pricee";
    private static final String GPU_PRICE = "pricee2";

    private static final String CPU_PRICE_final1 = "fprice1";
    private static final String CPU_PRICE_final2 = "fprice2";
    private static final String CPU_PRICE_final3 = "fprice3";
    private static final String CPU_PRICE_final4 = "fprice4";
    private static final String CPU_PRICE_final5= "fprice5";
    private static final String CPU_PRICE_final6= "fprice6";


    private static final String RAM_PRICE = "pricee3";
    private static final String MOTHERBOARD_PRICE = "pricee4";
    private static final String PSU_PRICE = "pricee5";
    private static final String STORAGE_PRICE = "pricee6";



    private TextView cpuTextView;
    private TextView cpupriceTextView;
    Button button;
    private TextView gpuTextView;
    private TextView gpupriceTextView;

    private TextView ramTextView;
    private TextView rampriceTextView;

    private TextView motherboardTextView;
    private TextView motherboardpriceTextView;

    private TextView psuTextView;
    private TextView psupriceTextView;

    private TextView storageTextView;
    private TextView storagepriceTextView;




    private String documentRefPath;
    private String documentRefPath2;
    private String documentRefPath3;
    private String documentRefPath4;
    private String documentRefPath5;
    private String documentRefPath6;


    private TextView finalpriceView;

    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestpc);

        cpuTextView = findViewById(R.id.cpu_textview);

        gpuTextView = findViewById(R.id.gpu_textview);

        ramTextView = findViewById(R.id.ram_textview);

        finalpriceView = findViewById(R.id.final_price);

        motherboardTextView = findViewById(R.id.motherboard_textview);

        psuTextView = findViewById(R.id.psu_textview);
        button = findViewById(R.id.button);
        storageTextView = findViewById(R.id.storage_textview);


        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        cpupriceTextView = findViewById(R.id.cpu_price);

        gpupriceTextView = findViewById(R.id.gpu_price);

        rampriceTextView = findViewById(R.id.ram_price);

        motherboardpriceTextView = findViewById(R.id.motherboard_price);

        psupriceTextView = findViewById(R.id.psu_price);

        storagepriceTextView = findViewById(R.id.storage_price);


        ImageView imageView = findViewById(R.id.btn_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Retrieve the cpuDocumentRefPath from the intent

        documentRefPath = getIntent().getStringExtra("documentRefPath");

        documentRefPath2 = getIntent().getStringExtra("documentRefPath2");

        documentRefPath3 = getIntent().getStringExtra("documentRefPath3");

        documentRefPath4 = getIntent().getStringExtra("documentRefPath4");

        documentRefPath5 = getIntent().getStringExtra("documentRefPath5");

        documentRefPath6 = getIntent().getStringExtra("documentRefPath6");

        // Retrieve the cpuDocumentRefPath from SharedPreferences

        if(documentRefPath==null){
            documentRefPath = sharedPreferences.getString(CPU_DOCUMENT_REF_PATH_KEY, "");
        }
        if(documentRefPath2==null){
            documentRefPath2 = sharedPreferences.getString(GPU_DOCUMENT_REF_PATH_KEY, "");
        }
        if(documentRefPath3==null){
            documentRefPath3 = sharedPreferences.getString(RAM_DOCUMENT_REF_PATH_KEY, "");
        }
        if(documentRefPath4==null){
            documentRefPath4 = sharedPreferences.getString(MOTHERBOARD_DOCUMENT_REF_PATH_KEY, "");
        }
        if(documentRefPath5==null){
            documentRefPath5 = sharedPreferences.getString(PSU_DOCUMENT_REF_PATH_KEY, "");
        }
        if(documentRefPath6==null){
            documentRefPath6 = sharedPreferences.getString(STORAGE_DOCUMENT_REF_PATH_KEY, "");
        }

        Log.d("LLLL44",documentRefPath3);

        Log.d("DOCREFP6",documentRefPath);


        // Set the subdocumentId as the text of the CPU TextView
        cpuTextView.setText(getSubdocumentId(documentRefPath));

        gpuTextView.setText(getSubdocumentId2(documentRefPath2));

        ramTextView.setText(getSubdocumentId3(documentRefPath3));

        motherboardTextView.setText(getSubdocumentId4(documentRefPath4));

        psuTextView.setText(getSubdocumentId5(documentRefPath5));

        storageTextView.setText(getSubdocumentId6(documentRefPath6));




        if(documentRefPath!=null) {
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
                                if(pricee == null || Objects.equals(pricee, "") || pricee.isEmpty() ){
                                    pricee = sharedPreferences.getString(CPU_PRICE,"");
                                }
                                if (pricee != null) {
                                    // Pass the documentRef path and pricee to the SuggestPc activity
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(CPU_PRICE, pricee);
                                    editor.apply();

                                    Log.d("PPPP666", pricee);
                                    cpupriceTextView.setText(pricee);
                                    String priceValue = pricee.substring(1);



                                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                    editor1.putString(CPU_PRICE_final1, priceValue);
                                    editor1.apply();

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

        if(documentRefPath2!=null) {
            // Query the document in the "Characteristics" collection
            FirebaseFirestore.getInstance().document(documentRefPath2).collection("Characteristics")
                    .document("Characteristics")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve the map of fields
                            Map<String, Object> fields = documentSnapshot.getData();
                            if (fields != null) {
                                String pricee2 = null;

                                // Iterate through the fields
                                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                                    String fieldName = entry.getKey();
                                    Object fieldValue = entry.getValue();

                                    // Check if the field value contains "$"
                                    if (fieldValue instanceof String && ((String) fieldValue).contains("$")) {
                                        pricee2 = (String) fieldValue;
                                        break;

                                    }
                                }
                                if(pricee2 == null || Objects.equals(pricee2, "") || pricee2.isEmpty() ){
                                    pricee2 = sharedPreferences.getString(GPU_PRICE,"");
                                }

                                if (pricee2 != null) {
                                    // Pass the documentRef path and pricee to the SuggestPc activity
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(GPU_PRICE, pricee2);
                                    editor.apply();

//                                    if(priceValue1[0].isEmpty()){
//                                        priceValue1[0] = sharedPreferences.getString(CPU_PRICE_final,"");
//                                    }
//                                    else{
//                                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
//                                        editor1.putString(CPU_PRICE_final, priceValue1[0]);
//                                        editor1.apply();
//                                    }

                                    Log.d("PPPP", pricee2);
                                    gpupriceTextView.setText(pricee2);
                                    // Remove "$" symbol from pricee and parse as float
                                    String priceValue2 = pricee2.substring(1);

                                    SharedPreferences.Editor editor2 = sharedPreferences.edit();
                                    editor2.putString(CPU_PRICE_final2, priceValue2);
                                    editor2.apply();


                                    Log.d("PRRR2",priceValue2);
                                    Float p1 = Float.valueOf(0);
//

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
        Log.d("LLLL7",documentRefPath3);
        if(documentRefPath3!=null) {
            // Query the document in the "Characteristics" collection
            FirebaseFirestore.getInstance().document(documentRefPath3).collection("Characteristics")
                    .document("Characteristics")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve the map of fields
                            Map<String, Object> fields = documentSnapshot.getData();
                            if (fields != null) {
                                String pricee3 = null;

                                // Iterate through the fields
                                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                                    String fieldName = entry.getKey();
                                    Object fieldValue = entry.getValue();

                                    // Check if the field value contains "$"
                                    if (fieldValue instanceof String && ((String) fieldValue).contains("$")) {
                                        pricee3 = (String) fieldValue;
                                        break;

                                    }
                                }
                                if(pricee3 == null || Objects.equals(pricee3, "") || pricee3.isEmpty() ){
                                    pricee3 = sharedPreferences.getString(RAM_PRICE,"");
                                    Log.d("LLLL1",pricee3);
                                }

                                if (pricee3 != null) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(RAM_PRICE, pricee3);
                                    Log.d("LLLL2",pricee3);
                                    editor.apply();


                                    rampriceTextView.setText(pricee3);
                                    // Remove "$" symbol from pricee and parse as float
                                    String priceValue3 = pricee3.substring(1);

                                    SharedPreferences.Editor editor3 = sharedPreferences.edit();
                                    editor3.putString(CPU_PRICE_final3, priceValue3);
                                    editor3.apply();


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


        if(documentRefPath4!=null) {
            // Query the document in the "Characteristics" collection
            FirebaseFirestore.getInstance().document(documentRefPath4).collection("Characteristics")
                    .document("Characteristics")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve the map of fields
                            Map<String, Object> fields = documentSnapshot.getData();
                            if (fields != null) {
                                String pricee4 = null;

                                // Iterate through the fields
                                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                                    String fieldName = entry.getKey();
                                    Object fieldValue = entry.getValue();

                                    // Check if the field value contains "$"
                                    if (fieldValue instanceof String && ((String) fieldValue).contains("$")) {
                                        pricee4 = (String) fieldValue;
                                        break;

                                    }
                                }
                                if(pricee4 == null || Objects.equals(pricee4, "") || pricee4.isEmpty() ){
                                    pricee4 = sharedPreferences.getString(MOTHERBOARD_PRICE,"");
                                }

                                if (pricee4 != null) {
                                    // Pass the documentRef path and pricee to the SuggestPc activity
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(MOTHERBOARD_PRICE, pricee4);
                                    editor.apply();


                                    motherboardpriceTextView.setText(pricee4);
                                    // Remove "$" symbol from pricee and parse as float
                                    String priceValue4 = pricee4.substring(1);


                                    SharedPreferences.Editor editor4 = sharedPreferences.edit();
                                    editor4.putString(CPU_PRICE_final4, priceValue4);
                                    editor4.apply();
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

        if(documentRefPath5!=null) {
            // Query the document in the "Characteristics" collection
            FirebaseFirestore.getInstance().document(documentRefPath5).collection("Characteristics")
                    .document("Characteristics")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve the map of fields
                            Map<String, Object> fields = documentSnapshot.getData();
                            if (fields != null) {
                                String pricee5 = null;

                                // Iterate through the fields
                                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                                    String fieldName = entry.getKey();
                                    Object fieldValue = entry.getValue();

                                    // Check if the field value contains "$"
                                    if (fieldValue instanceof String && ((String) fieldValue).contains("$")) {
                                        pricee5 = (String) fieldValue;
                                        break;

                                    }
                                }
                                if(pricee5 == null || Objects.equals(pricee5, "") || pricee5.isEmpty() ){
                                    pricee5 = sharedPreferences.getString(PSU_PRICE,"");
                                }

                                if (pricee5 != null) {
                                    // Pass the documentRef path and pricee to the SuggestPc activity
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(PSU_PRICE, pricee5);
                                    editor.apply();


                                    psupriceTextView.setText(pricee5);
                                    // Remove "$" symbol from pricee and parse as float
                                    String priceValue5 = pricee5.substring(1);


                                    SharedPreferences.Editor editor5 = sharedPreferences.edit();
                                    editor5.putString(CPU_PRICE_final5, priceValue5);
                                    editor5.apply();
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

        if(documentRefPath6!=null) {
            // Query the document in the "Characteristics" collection
            FirebaseFirestore.getInstance().document(documentRefPath6).collection("Characteristics")
                    .document("Characteristics")
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Retrieve the map of fields
                            Map<String, Object> fields = documentSnapshot.getData();
                            if (fields != null) {
                                String pricee6 = null;

                                // Iterate through the fields
                                for (Map.Entry<String, Object> entry : fields.entrySet()) {
                                    String fieldName = entry.getKey();
                                    Object fieldValue = entry.getValue();

                                    // Check if the field value contains "$"
                                    if (fieldValue instanceof String && ((String) fieldValue).contains("$")) {
                                        pricee6 = (String) fieldValue;
                                        break;

                                    }
                                }
                                if(pricee6 == null || Objects.equals(pricee6, "") || pricee6.isEmpty() ){
                                    pricee6 = sharedPreferences.getString(STORAGE_PRICE,"");
                                }

                                if (pricee6 != null) {
                                    // Pass the documentRef path and pricee to the SuggestPc activity
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(STORAGE_PRICE, pricee6);
                                    editor.apply();


                                    storagepriceTextView.setText(pricee6);
                                    // Remove "$" symbol from pricee and parse as float
                                    String priceValue6 = pricee6.substring(1);


                                    SharedPreferences.Editor editor6 = sharedPreferences.edit();
                                    editor6.putString(CPU_PRICE_final6, priceValue6);
                                    editor6.apply();

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
        String f8 = null;
        try {
            double f1;
            double f2;
            double f3;
            double f4;
            double f5;
            double f6;

            f1 = Double.parseDouble(sharedPreferences.getString(CPU_PRICE_final1, ""));
            f2 = Double.parseDouble(sharedPreferences.getString(CPU_PRICE_final2, ""));
            f3 = Double.parseDouble(sharedPreferences.getString(CPU_PRICE_final3, ""));
            f4 = Double.parseDouble(sharedPreferences.getString(CPU_PRICE_final4, ""));
            f5 = Double.parseDouble(sharedPreferences.getString(CPU_PRICE_final5, ""));
            f6 = Double.parseDouble(sharedPreferences.getString(CPU_PRICE_final6, ""));

            double f7 = f1 + f2 + f3 + f4 + f5 + f6;
            Log.d("TTTT", String.valueOf(f7));
            f8 = String.valueOf(f7);




        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(f8 != null) {
            String finalF = f8;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finalpriceView.setText(finalF);
                }
            });
        }
        else{
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
        }
    }





    @Override
    protected void onPause() {
        super.onPause();


        // Save the cpuDocumentRefPath to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CPU_DOCUMENT_REF_PATH_KEY, documentRefPath);
        editor.putString(GPU_DOCUMENT_REF_PATH_KEY, documentRefPath2);
        editor.putString(RAM_DOCUMENT_REF_PATH_KEY, documentRefPath3);
        editor.putString(MOTHERBOARD_DOCUMENT_REF_PATH_KEY, documentRefPath4);
        editor.putString(PSU_DOCUMENT_REF_PATH_KEY, documentRefPath5);
        editor.putString(STORAGE_DOCUMENT_REF_PATH_KEY, documentRefPath6);
        Log.d("DOCREFP",documentRefPath);
        editor.apply();
    }

    private String getSubdocumentId(String documentRefPath) {
        // Extract the subdocumentId from the CPU document reference path
        if(documentRefPath!=null) {
            Log.d("DOCREFP1", documentRefPath);
            String[] pathComponents = documentRefPath.split("/");
            if (pathComponents.length >= 3) {
                String pathcomponents = pathComponents[3] + " " + pathComponents[5];
                return pathcomponents;
            } else {
                return "";
            }
        }
        return "";
    }
    private String getSubdocumentId2(String documentRefPath2) {  /// DOCREF
        // Extract the subdocumentId from the GPu document reference path
        if(documentRefPath2!=null) {
            Log.d("DOCREFP99", documentRefPath2);
            String[] pathComponents2 = documentRefPath2.split("/");
            if (pathComponents2.length >= 3) {
                String pathcomponents2 = pathComponents2[3] + " " + pathComponents2[5];
                return pathcomponents2;
            } else {
                return "";
            }
        }
        return "";
    }
    private String getSubdocumentId3(String documentRefPath3) {
        // Extract the subdocumentId from the GPu document reference path
        if(documentRefPath3!=null) {
            Log.d("LLLL2",documentRefPath3);
            String[] pathComponents3 = documentRefPath3.split("/");
            if (pathComponents3.length >= 3) {
                String pathcomponents3 = pathComponents3[3] + " " + pathComponents3[5];
                Log.d("LLLL4",pathcomponents3);
                return pathcomponents3;
            } else {
                return "";
            }
        }
        return "";
    }
    private String getSubdocumentId4(String documentRefPath4) {
        // Extract the subdocumentId from the GPu document reference path
        if(documentRefPath4!=null) {
            Log.d("DOCREFP99", documentRefPath4);
            String[] pathComponents4 = documentRefPath4.split("/");
            if (pathComponents4.length >= 3) {
                String pathcomponents4 = pathComponents4[3] + " " + pathComponents4[5];
                return pathcomponents4;
            } else {
                return "";
            }
        }
        return "";
    }
    private String getSubdocumentId5(String documentRefPath5) {
        // Extract the subdocumentId from the GPu document reference path
        if(documentRefPath5!=null) {
            Log.d("DOCREFP99", documentRefPath5);
            String[] pathComponents5 = documentRefPath5.split("/");
            if (pathComponents5.length >= 3) {
                String pathcomponents5 = pathComponents5[3] + " " + pathComponents5[5];
                return pathcomponents5;
            } else {
                return "";
            }
        }
        return "";
    }

    private String getSubdocumentId6(String documentRefPath6) {
        // Extract the subdocumentId from the GPu document reference path
        if(documentRefPath6!=null) {
            Log.d("DOCREFP99", documentRefPath6);
            String[] pathComponents6 = documentRefPath6.split("/");
            if (pathComponents6.length >= 3) {
                String pathcomponents6 = pathComponents6[3] + " " + pathComponents6[5];
                return pathcomponents6;
            } else {
                return "";
            }
        }
        return "";
    }
}
