package com.example.pcplanner.MainMenu.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.pcplanner.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Cpu3Admin extends AppCompatActivity {

    private TextView mProductTitleTextView;
    private ImageView mLinkImageAmazonImageView;
    private TextView mDiscountPriceTextView;
    private TextView mCores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu3);



        Button button = findViewById(R.id.button);

        EditText editText = findViewById(R.id.editTextTextPersonName);

        EditText editText2 = findViewById(R.id.editText_intel);

        mProductTitleTextView = findViewById(R.id.product_title_text_view);
        mLinkImageAmazonImageView = findViewById(R.id.link_image_amazon_image_view);
        mDiscountPriceTextView = findViewById(R.id.discount_price_text_view);
        mCores = findViewById(R.id.cores_text_view);


        ActivityCompat.requestPermissions(Cpu3Admin.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setFocusable(false);
                editText2.setFocusable(false);
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar hello = Snackbar.make(view, "Connection to Amazon. This operation may take some time.", 25000);
                        hello.show();
                        String line;
                        String ProductTitle = "";
                        String LinkImageAmazon = "";
                        String DiscountPrice = "";
                        String AdvisedPrice = "";
                        String PriceNormal = "";




                        //INTEL
//                        String ProductTitle2 = "";
//
//
//                        try {
//                            // Gets HTML of the page
//                            URL url = new URL(editText2.getText().toString());
//                            CookieManager cookieManager = new CookieManager();
//                            CookieHandler.setDefault(cookieManager);
//                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                            conn.setReadTimeout(10000);//this is in milliseconds
//                            conn.setConnectTimeout(15000);//this is in milliseconds
//                            conn.setRequestProperty("Cookie", "PHPSESSID=str_from_server");
//                            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0");
//                            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//                            conn.connect();
//                            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                            while ((line = reader.readLine()) != null) {
//
//                                // INTEL Mobile Page Scraping
//
//                                if (line.contains("<div class=\"col-xs-6 col-lg-6 tech-data\">")) {
//
//                                    Log.d("CHKA","LINE FOUND");
//
//                                    ProductTitle2 = line.substring(line.indexOf("<div class=\"col-xs-6 col-lg-6 tech-data\">"));
//
//                                    ProductTitle2 = ProductTitle2.substring(ProductTitle2.indexOf("<span>") + 6, ProductTitle2.indexOf("</span>"));
//
//                                    Log.d("CHKA",ProductTitle2);
//                                }
//                                else{
//                                    Log.e("CHKA","LINE NOT FOUND");
//                                }
//
//
//                            }
//                        } catch (MalformedURLException e) {
//
//                        } catch (IOException e) {
//
//                        }





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
                        if(!ProductTitle.isEmpty()) {
                            title1 = ProductTitle.substring(0, 22);
                        }
                        else{
                            title1 = "error";
                        }


                        String finalPriceString = priceString;

                        //intel
//                        String finalProductTitle = ProductTitle2;


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProductTitleTextView.setText(title1);
                                Picasso.get().load(imlink).into(mLinkImageAmazonImageView);
                                mDiscountPriceTextView.setText(finalPriceString);
//                                mCores.setText(finalProductTitle);
                            }
                        });

                    }
                });
                thread.start();
            }
        });
    }
}