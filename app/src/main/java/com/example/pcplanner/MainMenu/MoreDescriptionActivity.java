package com.example.pcplanner.MainMenu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.protobuf.StringValue;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MoreDescriptionActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String collectionPath;
    private String documentId;
    private String collectionpath;
    private String subdocumentId;
    private RecyclerView fieldListRecyclerView;
    private TextView subdocumentCountTextView;
    private List<Map<String, Object>> subdocuments;

    private List<String> collectionPathList = new ArrayList<>();
    private List<String> subdocumentIdList = new ArrayList<>();
    private List<String> documentIdList = new ArrayList<>();

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moredescription);

        // Get shared preferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        firestore = FirebaseFirestore.getInstance();
        fieldListRecyclerView = findViewById(R.id.field_list_recyclerview);

        subdocumentCountTextView = findViewById(R.id.subdocument_count_tv);

        collectionpath = getIntent().getStringExtra("collectionpath");
        subdocumentId = getIntent().getStringExtra("subdocumentId");
        documentId = getIntent().getStringExtra("documentId");
        collectionPath = getIntent().getStringExtra("collectionPath");


        ImageView imageView = findViewById(R.id.btn_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Load subdocumentIdList/documentIdList from shared preferences
        Set<String> collectionPathSet = sharedPreferences.getStringSet("collectionPathList", null);
        Set<String> subdocumentIdSet = sharedPreferences.getStringSet("subdocumentIdList", null);
        Set<String> documentIdSet = sharedPreferences.getStringSet("documentIdList",null);

        if (collectionPathSet != null) {
            collectionPathList = new ArrayList<>(collectionPathSet);
        }

        if (subdocumentIdSet != null) {
            subdocumentIdList = new ArrayList<>(subdocumentIdSet);
        }
        if (documentIdSet != null) {
            documentIdList = new ArrayList<>(documentIdSet);
        }

        Button deleteListButton = findViewById(R.id.delete_list_button);
        deleteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteListButtonClick(v);
            }
        });

        // Inside onCreate method of MoreDescriptionActivity
        Button addToSubdocumentIdListButton = findViewById(R.id.add_to_list_button);
        addToSubdocumentIdListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subdocumentIdList.size() < 2) {
                    collectionPathList.add(collectionPath);
                    documentIdList.add(documentId);
                    subdocumentIdList.add(subdocumentId);

                    // Save the updated subdocumentIdList to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putStringSet("collectionPathList", new HashSet<>(collectionPathList));
                    editor.putStringSet("subdocumentIdList", new HashSet<>(subdocumentIdList));
                    editor.putStringSet("documentIdList", new HashSet<>(documentIdList));
                    editor.apply();

                    Toast.makeText(MoreDescriptionActivity.this, "Item added", Toast.LENGTH_SHORT).show();


                    // Update the subdocument count TextView
                    subdocumentCountTextView.setText("ITEMS IN THE COMPARISON LIST: " + String.valueOf(subdocumentIdList.size() + "/2"));
                }
                else{
                    if(documentIdList.size()<2){
                        collectionPathList.clear();
                        subdocumentIdList.clear();
                        documentIdList.clear();
                        // Update the subdocument count TextView
                        subdocumentCountTextView.setText("ITEMS IN THE COMPARISON LIST: " + String.valueOf(subdocumentIdList.size() + "/2"));
                    }
                    else{
                        subdocumentCountTextView.setText("THERE ARE ALREADY " + String.valueOf(subdocumentIdList.size() + "/2 ITEMS TO COMPARE!" + "\n\nCLICK 'DELETE LIST' BUTTON!"));
                    }
                }
            }
        });
        Button compareButton = findViewById(R.id.compare_button);
        compareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subdocumentIdList.size() >= 2 && documentIdList.size()>=2) {
                    String collectionPath1 = collectionPathList.get(0);
                    String collectionPath2 = collectionPathList.get(1);
                    String subdocumentId1 = subdocumentIdList.get(0);
                    String subdocumentId2 = subdocumentIdList.get(1);
                    String documentId1 = documentIdList.get(0);
                    String documentId2 = documentIdList.get(1);

                    // Create the intent
                    Intent intent = new Intent(MoreDescriptionActivity.this, CompareActivity.class);
                    intent.putExtra("subdocumentId", subdocumentId);
                    intent.putExtra("collectionpath", collectionpath);
                    intent.putExtra("collectionPath1", collectionPath1);
                    intent.putExtra("collectionPath2", collectionPath2);
                    intent.putExtra("subdocumentId1", subdocumentId1);
                    intent.putExtra("subdocumentId2", subdocumentId2);
                    intent.putExtra("documentId1", documentId1);
                    intent.putExtra("documentId2", documentId2);
                    intent.putExtra("documentId", documentId);
                    intent.putExtra("collectionPath", collectionPath);
                    startActivity(intent);
                } else {
                    Toast.makeText(MoreDescriptionActivity.this, "At least 2 elements are required for comparison", Toast.LENGTH_SHORT).show();
                    if(subdocumentIdList.size()<2) {
                        subdocumentCountTextView.setText("ITEMS IN THE COMPARISON LIST: " + String.valueOf(subdocumentIdList.size() + "/2"));
                    }
                    if(documentIdList.size()<2){
                        subdocumentCountTextView.setText("ITEMS IN THE COMPARISON LIST: " + String.valueOf(documentIdList.size() + "/2"));
                    }
                }
            }
        });




        CollectionReference documentRef = firestore.collection(collectionPath).document(documentId).collection("sub").document(subdocumentId).collection("Characteristics");

        documentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subdocuments = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    Map<String, Object> fields = document.getData();

                    // Sort the fields by name in ascending order
                    List<Map.Entry<String, Object>> sortedFields = new ArrayList<>(fields.entrySet());
                    Collections.sort(sortedFields, new Comparator<Map.Entry<String, Object>>() {
                        @Override
                        public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                            return o1.getKey().compareTo(o2.getKey());
                        }
                    });

                    // Convert the sorted List of Map.Entry objects back to a Map object
                    LinkedHashMap<String, Object> sortedFieldsMap = new LinkedHashMap<>();
                    for (Map.Entry<String, Object> entry : sortedFields) {
                        String fieldName = entry.getKey();
                        Object fieldValue = entry.getValue();
                        sortedFieldsMap.put(fieldName.substring(2), fieldValue);
                    }

                    subdocuments.add(sortedFieldsMap);
                }
                FieldListAdapter adapter = new FieldListAdapter(subdocuments);
                fieldListRecyclerView.setAdapter(adapter);
                fieldListRecyclerView.setLayoutManager(new LinearLayoutManager(MoreDescriptionActivity.this));
            } else {
                // Handle error
            }
        });

        Button addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the documentRef path
                String documentRefPath = collectionPath + "/" + documentId + "/sub/" + subdocumentId;

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
                                        Intent intent = new Intent(MoreDescriptionActivity.this, SuggestPc.class);
                                        intent.putExtra("documentRefPath", documentRefPath);
                                        intent.putExtra("pricee", pricee);
                                        Log.d("PPPP",pricee);
                                        startActivity(intent);
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
        });


    }

    //DELETE SUBCOLLECTIONLIST DATA STORED
    public void onDeleteListButtonClick(View view) {
        // Remove subdocumentIdList from SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("subdocumentIdList");
        editor.remove("documentIdList");
        editor.remove("collectionPathList");
        editor.apply();

        // Clear subdocumentIdList in memory
        subdocumentIdList.clear();
        documentIdList.clear();
        collectionPathList.clear();

        // Update the subdocument count TextView
        subdocumentCountTextView.setText("ITEMS IN THE COMPARISON LIST: "+String.valueOf(subdocumentIdList.size() + "/2"));
        // Notify the user that the list has been deleted
        Toast.makeText(this, "List deleted", Toast.LENGTH_SHORT).show();
    }


    private static class FieldListAdapter extends RecyclerView.Adapter<FieldViewHolder> {
        private final List<Map<String, Object>> subdocuments;

        public FieldListAdapter(List<Map<String, Object>> subdocuments) {
            this.subdocuments = subdocuments;
        }

        @NonNull
        @Override
        public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.field_item_layout, parent, false);
            return new FieldViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
            Map<String, Object> fields = subdocuments.get(position);
            holder.bindFields(fields);
        }

        @Override
        public int getItemCount() {
            return subdocuments.size();
        }
    }

    private static class FieldViewHolder extends RecyclerView.ViewHolder {
        private final TextView[] nameTextViews;
        private final TextView[] valueTextViews;
        private final ImageView fieldImageView;

        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextViews = new TextView[]{
                    itemView.findViewById(R.id.field_name1_textview),
                    itemView.findViewById(R.id.field_name2_textview),
                    itemView.findViewById(R.id.field_name3_textview),
                    itemView.findViewById(R.id.field_name4_textview),
                    itemView.findViewById(R.id.field_name5_textview),
                    itemView.findViewById(R.id.field_name6_textview),
                    itemView.findViewById(R.id.field_name7_textview),
                    itemView.findViewById(R.id.field_name8_textview),
                    itemView.findViewById(R.id.field_name9_textview),
                    itemView.findViewById(R.id.field_name10_textview),
                    itemView.findViewById(R.id.field_name11_textview),
                    itemView.findViewById(R.id.field_name12_textview)
            };
            valueTextViews = new TextView[]{
                    itemView.findViewById(R.id.field_value1_textview),
                    itemView.findViewById(R.id.field_value2_textview),
                    itemView.findViewById(R.id.field_value3_textview),
                    itemView.findViewById(R.id.field_value4_textview),
                    itemView.findViewById(R.id.field_value5_textview),
                    itemView.findViewById(R.id.field_value6_textview),
                    itemView.findViewById(R.id.field_value7_textview),
                    itemView.findViewById(R.id.field_value8_textview),
                    itemView.findViewById(R.id.field_value9_textview),
                    itemView.findViewById(R.id.field_value10_textview),
                    itemView.findViewById(R.id.field_value11_textview),
                    itemView.findViewById(R.id.field_value12_textview)
            };

            fieldImageView = itemView.findViewById(R.id.field_imageview);

        }


        public void bindFields(Map<String, Object> fields) {
            int i = 0;

            for (Map.Entry<String, Object> entry : fields.entrySet()) {

                if (i >= 12) {
                    break;
                }
                String name = entry.getKey();
                Object value = entry.getValue();
                nameTextViews[i].setText(name);
                valueTextViews[i].setText(String.valueOf(value));

                valueTextViews[11].setText(value + "%");


                String pricee = String.valueOf(valueTextViews[10]);

                    String filename = valueTextViews[0].getText().toString();
                    String filename1 = filename.substring(filename.indexOf("-") + 1);


                    // Create a reference to the image file in Firebase Cloud Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("INTEL");

                // List all items (files) in the root folder
                storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            String itemPath = item.getPath();
                            if (itemPath.contains(filename1)) {
                                // Found a matching file containing the substring
                                item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        Picasso.get().load(imageUrl).into(fieldImageView);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle any errors that occur during URL retrieval
                                        Log.e("MoreDescriptionActivity", "Failed to retrieve image URL", e);
                                    }
                                });
                                break; // Exit the loop after finding the first matching file
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occur during listing files
                        Log.e("MoreDescriptionActivity", "Failed to list files", e);
                    }
                });

                i++;
            }
            for (; i < 12; i++) {
                nameTextViews[i].setVisibility(View.GONE);
                valueTextViews[i].setVisibility(View.GONE);
            }
        }

    }
}
