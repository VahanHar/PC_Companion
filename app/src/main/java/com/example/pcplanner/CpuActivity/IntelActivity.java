package com.example.pcplanner.CpuActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcplanner.MainMenu.MyListAdapter;
import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class IntelActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ListView listView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intel);

        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.listView);
        sharedPreferences = getSharedPreferences("PC_Components2", Context.MODE_PRIVATE);

        // Try to retrieve the saved data from SharedPreferences
        String savedData = sharedPreferences.getString("DATA", null);
        if (savedData != null) {
            // If data is found, parse the JSON string and display it
            ArrayList<String> collections = new Gson().fromJson(savedData, ArrayList.class);
            MyListAdapter adapter = new MyListAdapter(IntelActivity.this, R.layout.list_item, collections);
            listView.setAdapter(adapter);
        } else {
            // If data is not found, retrieve it from Firebase Firestore and save it to SharedPreferences
            CollectionReference componentsCollectionRef = db.collection("PC Components");
            componentsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Task<QuerySnapshot>> tasks = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tasks.add(document.getReference().collection("collections").get());
                        }
                        Task<List<QuerySnapshot>> combinedTask = Tasks.whenAllSuccess(tasks);
                        combinedTask.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
                            @Override
                            public void onSuccess(List<QuerySnapshot> snapshots) {
                                HashSet<String> collectionSet = new HashSet<>();
                                for (QuerySnapshot snapshot : snapshots) {
                                    for (QueryDocumentSnapshot document : snapshot) {
                                        collectionSet.add(document.getId());
                                    }
                                }
                                ArrayList<String> collections = new ArrayList<>(collectionSet);
                                MyListAdapter adapter = new MyListAdapter(IntelActivity.this, R.layout.list_item, collections);
                                listView.setAdapter(adapter);

                                // Save the data to SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("DATA", new Gson().toJson(collections));
                                editor.apply();
                            }
                        });
                        combinedTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(IntelActivity.this, "Error getting collections", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(IntelActivity.this, "Error getting components", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        // Set click listener for collection items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String collectionName = ((TextView) view.findViewById(R.id.textView)).getText().toString();

                // Check if there is cached data for this collection
                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                String cachedData = prefs.getString(collectionName, null);

                if (cachedData != null) {
                    // If there is cached data, display it in the list view
                    ArrayList<String> documents = new ArrayList<>(Arrays.asList(cachedData.split(",")));
                    MyListAdapter adapter = new MyListAdapter(IntelActivity.this, R.layout.list_item, documents);
                    listView.setAdapter(adapter);
                } else {
                    // If there is no cached data, fetch data from Firestore
                    db.collection("PC Components").document(collectionName)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            ArrayList<String> collections = (ArrayList<String>) document.get("collections");
                                            MyListAdapter adapter = new MyListAdapter(IntelActivity.this, R.layout.list_item, collections);
                                            listView.setAdapter(adapter);

                                            // Save the fetched data to local storage for future use
                                            SharedPreferences.Editor editor = prefs.edit();
                                            editor.putString(collectionName, TextUtils.join(",", collections));
                                            editor.apply();
                                        } else {
                                            Toast.makeText(IntelActivity.this, "Collection not found", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(IntelActivity.this, "Error getting document", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }

        });
    }
}
