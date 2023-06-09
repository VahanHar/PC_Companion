package com.vahanhar.pcplanner.MainMenu.Admin;

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

import com.vahanhar.pcplanner.R;
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
    private final String FIELD66 = "Maximum Turbo Power"; // WORKING FROM 12GEN
    private final String FIELD7 = "Launch Date";
    private final String FIELD8 = "Max Memory Size";
    private final String FIELD9 = "Memory Types";
    private final String FIELD10 = "Socket";

    //AMD
    private final String FIELD22 = "# of CPU Cores";
    private final String FIELD33 = "# of Threads";
    private final String FIELD44 = "Max. Boost Clock";
    private final String FIELD55 = "Base Frequency";
    private final String FIELD67 = "TDP";
    private final String FIELD77 = "Launch Date";
    private final String FIELD99 = "Memory Type";
    private final String FIELD100 = "CPU Socket";


    private String processorNumber;
    private String coreNumber;
    private String threadNumber;
    private String maxFrequency;
    private String baseFrequency;
    private String maxPower;
    private String max2Power;
    private String launchDate;
    private String maxRam;
    private String MemTypes;
    private String Socket;

    private String processorNumber1;
    private String coreNumber1;
    private String threadNumber1;
    private String maxFrequency1;
    private String baseFrequency1;
    private String maxPower1;
    private String max2Power1;
    private String launchDate1;
    private String MemTypes1;
    private String Socket1;

    private String maxPrice;
    private String BenchM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu3);


        Button button = findViewById(R.id.button);


        EditText editText2 = findViewById(R.id.editText_intel);

        EditText editText3 = findViewById(R.id.editText_Amazon);

        EditText editText4 = findViewById(R.id.editText_Bench);

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

                if (!TextUtils.isEmpty(editText2.getText()) && !TextUtils.isEmpty(editText3.getText())) {
                    Cpu3Admin.Intl dw = new Cpu3Admin.Intl();
                    dw.execute();
                    Cpu3Admin.Amazon dw2 = new Cpu3Admin.Amazon();
                    dw2.execute();
                }
                else{
                    Log.e("CPUADMIN","CPU field is empty");
                    Toast.makeText(getApplicationContext(), "FILL ALL LINES", Toast.LENGTH_SHORT).show();
                }

                if(!TextUtils.isEmpty(editText4.getText())){
                    Cpu3Admin.BenchMark dw3 = new Cpu3Admin.BenchMark();
                    dw3.execute();
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

            Element processorrow2 = document.select("div.tech-label:contains(" + FIELD1 + ")").first();
            if (processorrow2 != null) {
                processorrow2 = processorrow2.parent();
                processorNumber = processorrow2.select("div.tech-data").first().text();
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
            Element row44 = document.select("div.tech-label:contains(" + FIELD66 + ")").first();
            if (row44 != null) {
                row44 = row44.parent();
                max2Power = row44.select("div.tech-data").first().text();
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
            Element row7 = document.select("div.tech-label:contains(" + FIELD9 + ")").first();
            if (row7 != null) {
                row7 = row7.parent();
                MemTypes = row7.select("div.tech-data").first().text();
            }
            Element row8 = document.select("div.tech-label:contains(" + FIELD10 + ")").first();
            if (row8 != null) {
                row8 = row8.parent();
                Socket = row8.select("div.tech-data").first().text();
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

                        String MemTypes2;
                        if (MemTypes.length() <= 20) {
                            MemTypes2 = MemTypes;
                        } else {
                            MemTypes2 = MemTypes.substring(0, 20);
                        }


                        //ADDING FIELDS OF THAT GENERATION PROCESSOR
                        characteristicsDocRef.set(new HashMap<String, Object>() {{
                            put("1.Processor Number", processorNumber);
                            put("2.Launch Date", launchDate);
                            put("3.Number of Cores", coreNumber);
                            put("4.Number of Threads", threadNumber);
                            put("5.Base Frequency", baseFrequency);
                            put("6.Maximum Frequency", maxFrequency);
                            if (!(maxPower == null)) {
                                put("7.Maximum Power usage", maxPower);
                            } else {
                                put("7.Maximum Power usage", max2Power);
                            }
                            put("8.Maximum RAM", maxRam);
                            put("9.Ram Type", MemTypes2);
                            put("a.Socket", Socket);
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
                Log.d("Cpu3Admin.Amazon", "Starting doInBackground()");
                Log.d("Cpu3Admin.Amazon", "URL: " + url);
                document = Jsoup.connect(url).get();
                Log.d("CPU3ADMIN.Amazon", String.valueOf(document));
            } catch (IOException e) {
                e.printStackTrace();
            }


            Element row6 = document.select("span.a-price.aok-align-center.reinventPricePriceToPayMargin.priceToPay").first();
            if (row6 != null) {
                String priceText = row6.select("span.a-offscreen").first().text();
                maxPrice = priceText;
            }


            return processorNumber;
        }


        public void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String[] parts = null;
                    String p1 = null;
                    String p2 = null;

                    if(processorNumber!=null) {
                        parts = processorNumber.split("-");
                    }

                        p1 = parts[0];
                        p2 = parts[1];

                        CollectionReference parentCollectionRef = firestore.collection("PC Components");
                        DocumentReference cpuDocRef = parentCollectionRef.document("CPU");
                        CollectionReference intelCollectionRef = cpuDocRef.collection("INTEL");
                        DocumentReference brandDocRef = intelCollectionRef.document("Core " + p1);
                        CollectionReference subsubCollectionRef = brandDocRef.collection("sub");
                        DocumentReference modelCollectionRef = subsubCollectionRef.document(p2);
                        CollectionReference detailscoleRef = modelCollectionRef.collection("Characteristics");
                        DocumentReference characteristicsDocRef = detailscoleRef.document("Characteristics");


                        String fieldName = "b.Maximum Price";
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






    public class BenchMark extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            org.jsoup.nodes.Document document = null;
            EditText editText4 = findViewById(R.id.editText_Bench);
            try {
                String url = editText4.getText().toString().trim();
                Log.d("Cpu3Admin.Amazon", "Starting doInBackground()");
                Log.d("Cpu3Admin.Amazon", "URL: " + url);
                document = Jsoup.connect(url).get();
                Log.d("CPU3ADMIN.Amazon", String.valueOf(document));
            } catch (IOException e) {
                e.printStackTrace();
            }


            Element colElement = document.select("div.col-xs-8").first();
            if (colElement != null) {
                String benchText = colElement.select("h2.conclusion").text();
                String benchValue = benchText.replaceAll("[^0-9.]", "");
                if (benchValue.length() >= 3) {
                    benchValue = benchValue.substring(0, 3);
                }
                BenchM = benchValue;
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



                    String fieldName = "c.BenchMark";
                    int fvalue = Integer.parseInt(BenchM);


                    Map<String, Object> updateFields = new HashMap<>();
                    updateFields.put(fieldName, fvalue);

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
//
//    public class AMD extends AsyncTask<Void, Void, String> {
//
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(Void... voids) {
//            org.jsoup.nodes.Document document = null;
//            EditText editText2 = findViewById(R.id.editText_intel);
//            try {
//                String url = editText2.getText().toString().trim();
//                Log.d("Cpu3Admin.AMD", "Starting doInBackground()");
//                Log.d("Cpu3Admin.AMD", "URL: " + url);
//                document = Jsoup.connect(url).get();
//                Log.d("CPU3ADMIN.AMD", String.valueOf(document));
//                Log.d("DOCUMENTADMINCPU", String.valueOf(document));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
////            coreNumber;
////            threadNumber;
////            maxFrequency;
////            baseFrequency;
////            maxPower;
////            launchDate;
////            maxRam;
//
//            Element processorrow = document.select("h2.field.section-title").first();
//            Log.d("LLLLAAA", String.valueOf(processorrow));
//            if (processorrow != null) {
//                processorNumber1 = processorrow.text();
//                Log.d("AAALLL",processorNumber1);
//            }
//            Element corerow = document.select("div.field__label:contains(" + FIELD22 + ")").first();
//            if (corerow != null) {
//                corerow = corerow.parent();
//                coreNumber1 = corerow.select("div.field__item").first().text();
//            }
//            Element row1 = document.select("div.field__label:contains(" + FIELD33 + ")").first();
//            if (row1 != null) {
//                row1 = row1.parent();
//                threadNumber1 = row1.select("div.field__item").first().text();
//            }
//            Element row2 = document.select("div.field__label:contains(" + FIELD44 + ")").first();
//            if (row2 != null) {
//                row2 = row2.parent();
//                maxFrequency1 = row2.select("div.field__item").first().text();
//            }
//            Element row3 = document.select("div.field__label:contains(" + FIELD55 + ")").first();
//            if (row3 != null) {
//                row3 = row3.parent();
//                baseFrequency1 = row3.select("div.field__item").first().text();
//            }
//            Element row4 = document.select("div.field__label:contains(" + FIELD67 + ")").first();
//            if (row4 != null) {
//                row4 = row4.parent();
//                maxPower1 = row4.select("div.field__item").first().text();
//            }
//            Element row5 = document.select("div.field__label:contains(" + FIELD77 + ")").first();
//            if (row5 != null) {
//                row5 = row5.parent();
//                launchDate1 = row5.select("div.field__item").first().text();
//            }
//            Element row7 = document.select("div.field__label:contains(" + FIELD99 + ")").first();
//            if (row7 != null) {
//                row7 = row7.parent();
//                MemTypes1 = row7.select("div.field__item").first().text();
//            }
//            Element row8 = document.select("div.field__label:contains(" + FIELD100 + ")").first();
//            if (row8 != null) {
//                row8 = row8.parent();
//                Socket1 = row8.select("div.field__item").first().text();
//            }
//
//            return processorNumber1;
//        }
//
//
//        public void onPostExecute(String result) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    CollectionReference parentCollectionRef = firestore.collection("PC Components");
//                    DocumentReference cpuDocRef = parentCollectionRef.document("CPU");
//                    CollectionReference intelCollectionRef = cpuDocRef.collection("AMD");
//
//
//                    String p3 = processorNumber1.replace("™","");
//                    String p4 = p3.replace("AMD","").trim();
//
//                    String[] parts = p4.split(" ");
//                    String p1 = parts[0] + " " + parts[1];
//                    String p2 = parts[2];
//
//
//                    DocumentReference brandDocRef = intelCollectionRef.document(p1);
//                    CollectionReference subsubCollectionRef = brandDocRef.collection("sub");
//                    DocumentReference modelCollectionRef = subsubCollectionRef.document(p2);
//                    CollectionReference detailscoleRef = modelCollectionRef.collection("Characteristics");
//                    DocumentReference characteristicsDocRef = detailscoleRef.document("Characteristics");
//
//                    //ADDING GENERATION BY LINK
//                    brandDocRef.collection("sub").document(p2).set(new HashMap<String, Object>())
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(getApplicationContext(), "generation added successfully", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getApplicationContext(), "Error adding generation", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                    //ADDING FIELDS OF THAT GENERATION PROCESSOR
//                    characteristicsDocRef.set(new HashMap<String, Object>() {{
//                        put("1.Processor Number", processorNumber1);
//                        put("2.Launch Date", launchDate1);
//                        put("3.Number of Cores", coreNumber1);
//                        put("4.Number of Threads", threadNumber1);
//                        put("5.Base Frequency", baseFrequency1);
//                        put("6.Maximum Frequency", maxFrequency1);
//                        put("7.Maximum Power usage", maxPower1);
//                        put("9.Ram Type", MemTypes1);
//                        put("a.Socket", Socket1);
//                    }});
//
//                }
//            });
//        }
//    }

}