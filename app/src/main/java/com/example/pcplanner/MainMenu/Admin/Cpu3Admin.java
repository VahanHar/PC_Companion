package com.example.pcplanner.MainMenu.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import org.jsoup.nodes.Document;

import com.example.pcplanner.PsuActivity;
import com.example.pcplanner.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class Cpu3Admin extends AppCompatActivity {

    private TextView mProductTitleTextView;
    private ImageView mLinkImageAmazonImageView;
    private TextView mDiscountPriceTextView;
    FirebaseFirestore firestore;
    private TextView itemNameTextView;

    //INTEL
    private final String FIELD1 = "Processor Number";
    private final String FIELD2 = "Cores";
    private final String FIELD3 = "Threads";
    private final String FIELD4 = "Max Turbo Frequency"; // WORKING WITH ONLY TURBO BOOST MODELS
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


    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu3);


        Button button = findViewById(R.id.button);

        EditText editText = findViewById(R.id.editTextTextPersonName);

        EditText editText2 = findViewById(R.id.editText_intel);

        firestore = FirebaseFirestore.getInstance();

        //INTEL
        itemNameTextView = findViewById(R.id.item_name_text_view);
        //

        mProductTitleTextView = findViewById(R.id.product_title_text_view);
        mLinkImageAmazonImageView = findViewById(R.id.link_image_amazon_image_view);
        mDiscountPriceTextView = findViewById(R.id.discount_price_text_view);



        ActivityCompat.requestPermissions(Cpu3Admin.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                editText.setFocusable(false);
                editText2.setFocusable(false);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String line;
                        String ProductTitle = "";
                        String LinkImageAmazon = "";
                        String DiscountPrice = "";
                        String AdvisedPrice = "";
                        String PriceNormal = "";


                        //Amazon

                        try {
                            // Gets HTML of the page
                            URL url = new URL(editText.getText().toString());
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
                                if (line.contains("data-a-color=\"base\"><span class=\"a-offscreen\">")) {
                                    DiscountPrice = line.substring(line.indexOf("data-a-color=\"base\"><span class=\"a-offscreen\">"));
                                    DiscountPrice = DiscountPrice.replace("data-a-color=\"base\"><span class=\"a-offscreen\">", "");
                                    DiscountPrice = DiscountPrice.substring(0, DiscountPrice.indexOf("</span>"));
                                }
                                if (line.contains("<span class=\"a-price a-text-price a-size-base\" data-a-size=\"b\" data-a-strike=\"true\" data-a-color=\"secondary\"><span class=\"a-offscreen\">")) {
                                    AdvisedPrice = line.substring(line.indexOf("<span class=\"a-price a-text-price a-size-base\" data-a-size=\"b\" data-a-strike=\"true\" data-a-color=\"secondary\"><span class=\"a-offscreen\">"));
                                    AdvisedPrice = AdvisedPrice.replace("<span class=\"a-price a-text-price a-size-base\" data-a-size=\"b\" data-a-strike=\"true\" data-a-color=\"secondary\"><span class=\"a-offscreen\">", "");
                                    AdvisedPrice = AdvisedPrice.substring(0, AdvisedPrice.indexOf("</span>"));
                                }
                                if (line.contains("<span class=\"a-size-small a-color-secondary aok-align-center basisPrice\">Prezzo consigliato: <span class=\"a-price a-text-price\" data-a-size=\"s\" data-a-strike=\"true\" data-a-color=\"secondary\"><span class=\"a-offscreen\">")) {
                                    AdvisedPrice = line.substring(line.indexOf("<span class=\"a-size-small a-color-secondary aok-align-center basisPrice\">Prezzo consigliato: <span class=\"a-price a-text-price\" data-a-size=\"s\" data-a-strike=\"true\" data-a-color=\"secondary\"><span class=\"a-offscreen\">"));
                                    AdvisedPrice = AdvisedPrice.replace("<span class=\"a-size-small a-color-secondary aok-align-center basisPrice\">Prezzo consigliato: <span class=\"a-price a-text-price\" data-a-size=\"s\" data-a-strike=\"true\" data-a-color=\"secondary\"><span class=\"a-offscreen\">", "");
                                    AdvisedPrice = AdvisedPrice.substring(0, AdvisedPrice.indexOf("</span>"));
                                }
                                if (line.contains("Prezzo precedente:</td><td class=\"a-color-secondary a-size-base\">")) {
                                    AdvisedPrice = line.substring(line.indexOf("Prezzo precedente:</td><td class=\"a-color-secondary a-size-base\">"));
                                    AdvisedPrice = AdvisedPrice.replace("Prezzo precedente:</td><td class=\"a-color-secondary a-size-base\">", "");
                                    AdvisedPrice = AdvisedPrice.substring(0, AdvisedPrice.indexOf("</span>"));
                                    AdvisedPrice = AdvisedPrice.substring(AdvisedPrice.lastIndexOf("<span class=\"a-offscreen\">"));
                                    AdvisedPrice = AdvisedPrice.replace("<span class=\"a-offscreen\">", "");
                                }
                                if (line.contains("<span class=\"a-size-medium a-color-base price-strikethrough inline-show-experience margin-spacing aok-hidden notranslate\">")) {
                                    AdvisedPrice = line.substring(line.indexOf("<span class=\"a-size-medium a-color-base price-strikethrough inline-show-experience margin-spacing aok-hidden notranslate\">"));
                                    AdvisedPrice = AdvisedPrice.replace("<span class=\"a-size-medium a-color-base price-strikethrough inline-show-experience margin-spacing aok-hidden notranslate\">", "");
                                    AdvisedPrice = AdvisedPrice.substring(0, AdvisedPrice.indexOf("</span>"));
                                }
                                if (line.contains("\",\"priceAmount\":")) {
                                    PriceNormal = line.substring(0, line.indexOf("\",\"priceAmount\":"));
                                    PriceNormal = PriceNormal.substring(PriceNormal.indexOf("\"displayPrice\":\""));
                                    PriceNormal = PriceNormal.replace("\"displayPrice\":\"", "");
                                }

                            }
                        } catch (MalformedURLException e) {

                        } catch (IOException e) {

                        }


                        //price
                        int price = 0;
                        if (!DiscountPrice.isEmpty()) {
                            int price1 = Integer.parseInt(DiscountPrice.replaceAll("[^\\d]", ""));
                            price = price1;
                        }
                        if (!AdvisedPrice.isEmpty()) {
                            int price2 = Integer.parseInt(AdvisedPrice.replaceAll("[^\\d]", ""));
                            if (price2 > price) {
                                price = price2;
                            }
                        }
                        if (!PriceNormal.isEmpty()) {
                            int price3 = Integer.parseInt(PriceNormal.replaceAll("[^\\d]", ""));
                            if (price3 > price) {
                                price = price3;
                            }
                        }


                        String priceString = Integer.toString(price);
                        if (priceString.length() > 2) {
                            priceString = "$" + priceString.substring(0, priceString.length() - 2) + "." + priceString.substring(priceString.length() - 2);
                        }


                        //image
                        String imlink = LinkImageAmazon;

                        //title
                        String title1;
                        if (!ProductTitle.isEmpty()) {
                            title1 = ProductTitle.substring(0, 22);
                        } else {
                            title1 = "error";
                        }


                        String finalPriceString = priceString;


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProductTitleTextView.setText(title1);
                                Picasso.get().load(imlink).into(mLinkImageAmazonImageView);
                                mDiscountPriceTextView.setText(finalPriceString);
                            }
                        });

                    }
                });
                thread.start();

                if (!TextUtils.isEmpty(editText2.getText())) {
                    Cpu3Admin.Intl dw = new Cpu3Admin.Intl();
                    dw.execute();
                    Log.d("EMPTYCHI","NOa");
                }
                else{
                    Log.d("EMPTYA","Empt");
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
                    String[] parts = processorNumber.split("-");
                    String p1 = parts[0];
                    String p2 = parts[1];
                    DocumentReference brandDocRef = intelCollectionRef.document("Core "+p1);
                    CollectionReference subsubCollectionRef = brandDocRef.collection("sub");
                    DocumentReference modelCollectionRef = subsubCollectionRef.document(p2);
                    CollectionReference detailscoleRef = modelCollectionRef.collection("Characteristics");
                    DocumentReference characteristicsDocRef = detailscoleRef.document("Characteristics") ;


                    characteristicsDocRef.set(new HashMap<String, Object>() {{
                        put("Processor Number", processorNumber);
                        put("Number of Cores", coreNumber);
                        put("Number of Threads", threadNumber);
                        put("Maximum Frequency", maxFrequency);
                        put("Base Frequency", baseFrequency);
                        put("Maximum Power usage", maxPower);
                        put("Launch Date", launchDate);
                        put("Maximum RAM", maxRam);
                    }});

                    String displayText = "Processor Number: " + processorNumber + "\nNumber of Cores: " + coreNumber + "\nNumber of Threads: " + threadNumber + "\nMaximum Frequency: " + maxFrequency+ "\nBase Frequency: " + baseFrequency+ "\nMaximum Power usage: " + maxPower+ "\nLaunch Date: " + launchDate+ "\nMaximum RAM: " + maxRam;
                    itemNameTextView.setText(displayText);
                }
            });
        }
    }

}