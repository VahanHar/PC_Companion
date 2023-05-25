package com.example.pcplanner.MainMenu.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Cpu3Admin extends AppCompatActivity {

    private ImageView mLinkImageAmazonImageView;
    FirebaseFirestore firestore;


    //INTEL
    private final String FIELD1 = "Processor Number";
    private final String FIELD2 = "Cores";
    private final String FIELD3 = "Threads";
    private final String FIELD4 = "Max Turbo Frequency";
    private final String FIELD5 = "Base Frequency";
    private final String FIELD6 = "TDP"; // WORKING UP TO 11GEN
    private final String FIELD7 = "Launch Date";
    private final String FIELD8 = "Max Memory Size";

    private String processorNumber;
    private String coreNumber;
    private String threadNumber;
    private String maxFrequency;
    private String baseFrequency;
    private String maxPower;
    private String launchDate;
    private String maxRam;

    private String maxPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu3);


        Button button = findViewById(R.id.button);


        EditText editText2 = findViewById(R.id.editText_intel);

        EditText editText3 = findViewById(R.id.editText_Amazon);

        firestore = FirebaseFirestore.getInstance();

        mLinkImageAmazonImageView = findViewById(R.id.link_image_amazon_image_view);



        ActivityCompat.requestPermissions(Cpu3Admin.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String line;
                        String ProductTitle = "";
                        String LinkImageAmazon = "";

                        //Amazon

                        try {
                            // Gets HTML of the page
                            URL url = new URL(editText3.getText().toString());
                            CookieManager cookieManager = new CookieManager();
                            CookieHandler.setDefault(cookieManager);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setReadTimeout(10000);//this is in milliseconds
                            conn.setConnectTimeout(15000);//this is in milliseconds
                            conn.setRequestProperty("Cookie", "PHPSESSID=str_from_server");
                            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0");
                            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                            conn.connect();
                            int response = conn.getResponseCode();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            int SkipThis = 0;
                            while ((line = reader.readLine()) != null) {

                                // Amazon Mobile Page Scraping
                                if (line.contains("data-a-dynamic-image=\"{&quot;")) {
                                    if (SkipThis == 0) {
                                        LinkImageAmazon = line.substring(line.indexOf("data-a-dynamic-image=\"{&quot;"));
                                        LinkImageAmazon = LinkImageAmazon.replace("data-a-dynamic-image=\"{&quot;", "");
                                        LinkImageAmazon = LinkImageAmazon.substring(0, LinkImageAmazon.indexOf("&quot;"));
                                        if (LinkImageAmazon.contains("_AC_")) {
                                            LinkImageAmazon = LinkImageAmazon.substring(0, LinkImageAmazon.lastIndexOf("."));
                                            LinkImageAmazon = LinkImageAmazon.substring(0, LinkImageAmazon.length() - 1);
                                            LinkImageAmazon = LinkImageAmazon + ".jpg";
                                        }
                                        SkipThis = 1;
                                    }
                                }
                                if (line.contains("<span id=\"productTitle\" class=\"a-size-large product-title-word-break\">        ")) {
                                    ProductTitle = line.substring(line.indexOf("<span id=\"productTitle\" class=\"a-size-large product-title-word-break\">        "));
                                    ProductTitle = ProductTitle.replace("<span id=\"productTitle\" class=\"a-size-large product-title-word-break\">        ", "");
                                    // ProductTitle = Html.fromHtml(ProductTitle).toString();
                                    ProductTitle = ProductTitle.substring(0, ProductTitle.indexOf("</span>"));
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        //image
                        String imlink = LinkImageAmazon;
                        String finalProductTitle = ProductTitle;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Picasso.get().load(imlink).into(mLinkImageAmazonImageView);

                                    // Get a reference to the Firebase Storage
                                    FirebaseStorage storage = FirebaseStorage.getInstance();

                                    // Create a storage reference with the desired name for the image
                                    if(!finalProductTitle.isEmpty()) {
                                        String imageName = finalProductTitle + ".jpg";
                                        StorageReference storageRef = storage.getReference().child("INTEL/" + imageName);

                                        // Download the image from the provided URL (imlink)
                                        Picasso.get()
                                                .load(imlink)
                                                .into(new Target() {
                                                    @Override
                                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                        // Convert the loaded bitmap to a byte array
                                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                                        byte[] imageData = baos.toByteArray();

                                                        // Upload the byte array to Firebase Storage
                                                        UploadTask uploadTask = storageRef.putBytes(imageData);
                                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                        // Image uploaded successfully
                                                                        // Get the download URL of the uploaded image
                                                                        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri downloadUri) {
                                                                                // The download URL of the image
                                                                                String imageUrl = downloadUri.toString();
                                                                                // You can use this imageUrl as needed
                                                                            }
                                                                        });
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        // Image upload failed
                                                                        e.printStackTrace();
                                                                    }
                                                                });
                                                    }

                                                    @Override
                                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                                        // Failed to load the image from the provided URL
                                                        e.printStackTrace();
                                                    }

                                                    @Override
                                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                                        // Image is being prepared for loading
                                                    }
                                                });
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "ERROR ADDING IMAGE, CLICK GET TEXT", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "FILL ALL LINES", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });



                thread.start();

                if (!TextUtils.isEmpty(editText2.getText())) {
                    Cpu3Admin.Intl dw = new Cpu3Admin.Intl();
                    dw.execute();
                }
                else{
                }

                if (!TextUtils.isEmpty(editText3.getText())) {
                    Cpu3Admin.Amazon dw = new Cpu3Admin.Amazon();
                    dw.execute();
                }

            }

        });
    }


    public class Intl extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            org.jsoup.nodes.Document document = null;
            EditText editText2 = findViewById(R.id.editText_intel);
            try {
                String url = editText2.getText().toString().trim();
                Log.d("Cpu3Admin.Intl", "Starting doInBackground()");
                Log.d("Cpu3Admin.Intl", "URL: " + url);
                document = Jsoup.connect(url).get();
                Log.d("CPU3ADMIN.INTL", String.valueOf(document));
            } catch (IOException e) {
                e.printStackTrace();
            }

//            coreNumber;
//            threadNumber;
//            maxFrequency;
//            baseFrequency;
//            maxPower;
//            launchDate;
//            maxRam;

            Element processorrow = document.select("div.tech-label:contains(" + FIELD1 + ")").first();
            if (processorrow != null) {
                processorrow = processorrow.parent();
                processorNumber = processorrow.select("div.tech-data").first().text();
            }
            Element corerow = document.select("div.tech-label:contains(" + FIELD2 + ")").first();
            if (corerow != null) {
                corerow = corerow.parent();
                coreNumber = corerow.select("div.tech-data").first().text();
            }
            Element row1 = document.select("div.tech-label:contains(" + FIELD3 + ")").first();
            if (row1 != null) {
                row1 = row1.parent();
                threadNumber = row1.select("div.tech-data").first().text();
            }
            Element row2 = document.select("div.tech-label:contains(" + FIELD4 + ")").first();
            if (row2 != null) {
                row2 = row2.parent();
                maxFrequency = row2.select("div.tech-data").first().text();
            }
            Element row3 = document.select("div.tech-label:contains(" + FIELD5 + ")").first();
            if (row3 != null) {
                row3 = row3.parent();
                baseFrequency = row3.select("div.tech-data").first().text();
            }
            Element row4 = document.select("div.tech-label:contains(" + FIELD6 + ")").first();
            if (row4 != null) {
                row4 = row4.parent();
                maxPower = row4.select("div.tech-data").first().text();
            }
            Element row5 = document.select("div.tech-label:contains(" + FIELD7 + ")").first();
            if (row5 != null) {
                row5 = row5.parent();
                launchDate = row5.select("div.tech-data").first().text();
            }
            Element row6 = document.select("div.tech-label:contains(" + FIELD8 + ")").first();
            if (row6 != null) {
                row6 = row6.parent();
                maxRam = row6.select("div.tech-data").first().text();
            }

            return processorNumber;
        }


        public void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    CollectionReference parentCollectionRef = firestore.collection("PC Components");
                    DocumentReference cpuDocRef = parentCollectionRef.document("CPU");
                    CollectionReference intelCollectionRef = cpuDocRef.collection("INTEL");

                    String[] parts = new String[0];
                    if (processorNumber != null) {
                        parts = processorNumber.split("-");
                    } else {
                        Toast.makeText(getApplicationContext(), "Error with intel link", Toast.LENGTH_SHORT).show();
                    }

                    String p1 = parts[0];
                    String p2 = parts[1];
                    DocumentReference brandDocRef = intelCollectionRef.document("Core " + p1);
                    CollectionReference subsubCollectionRef = brandDocRef.collection("sub");
                    DocumentReference modelCollectionRef = subsubCollectionRef.document(p2);
                    CollectionReference detailscoleRef = modelCollectionRef.collection("Characteristics");
                    DocumentReference characteristicsDocRef = detailscoleRef.document("Characteristics");

                    //ADDING GENERATION BY LINK
                    brandDocRef.collection("sub").document(p2).set(new HashMap<String, Object>())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "generation added successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error adding generation", Toast.LENGTH_SHORT).show();
                                }
                            });

                    //ADDING FIELDS OF THAT GENERATION PROCESSOR
                    characteristicsDocRef.set(new HashMap<String, Object>() {{
                        put("1.Processor Number", processorNumber);
                        put("2.Launch Date", launchDate);
                        put("3.Number of Cores", coreNumber);
                        put("4.Number of Threads", threadNumber);
                        put("5.Base Frequency", baseFrequency);
                        put("6.Maximum Frequency", maxFrequency);
                        put("7.Maximum Power usage", maxPower);
                        put("8.Maximum RAM", maxRam);
                    }});

                }
            });
        }
    }



    public class Amazon extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            org.jsoup.nodes.Document document = null;
            EditText editText3 = findViewById(R.id.editText_Amazon);
            try {
                String url = editText3.getText().toString().trim();
                Log.d("Cpu33Admin.Intl", "Starting doInBackground()");
                Log.d("Cpu33Admin.Intl", "URL: " + url);
                document = Jsoup.connect(url).get();
                Log.d("CPU33ADMIN.INTL", String.valueOf(document));
            } catch (IOException e) {
                e.printStackTrace();
            }


            Element row6 = document.select("span.a-price.aok-align-center.reinventPricePriceToPayMargin.priceToPay").first();
            Log.d("CPU333ADMIN.INTL", String.valueOf(row6));
            if (row6 != null) {
                String priceText = row6.select("span.a-offscreen").first().text();
                Log.d("CPU333ADMIN.INTL", String.valueOf(priceText));
                // Further processing if needed
                maxPrice = priceText;
                Log.d("CPU333ADMIN.INTL", String.valueOf(maxPrice));
            }


            return processorNumber;
        }


        public void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    CollectionReference parentCollectionRef = firestore.collection("PC Components");
                    DocumentReference cpuDocRef = parentCollectionRef.document("CPU");
                    CollectionReference intelCollectionRef = cpuDocRef.collection("INTEL");
                    String[] parts = processorNumber.split("-");
                    String p1 = parts[0];
                    String p2 = parts[1];
                    DocumentReference brandDocRef = intelCollectionRef.document("Core "+p1);
                    CollectionReference subsubCollectionRef = brandDocRef.collection("sub");
                    DocumentReference modelCollectionRef = subsubCollectionRef.document(p2);
                    CollectionReference detailscoleRef = modelCollectionRef.collection("Characteristics");
                    DocumentReference characteristicsDocRef = detailscoleRef.document("Characteristics") ;



                    String fieldName = "9.Maximum Price";
                    String fieldValue = maxPrice;

                    Map<String, Object> updateFields = new HashMap<>();
                    updateFields.put(fieldName, fieldValue);

                    characteristicsDocRef.set(updateFields, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Field added/updated successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error adding/updating field", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
        }
    }

}