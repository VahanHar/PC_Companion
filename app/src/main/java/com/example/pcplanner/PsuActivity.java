package com.example.pcplanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PsuActivity extends AppCompatActivity {

    private TextView priceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psu);

        priceTextView = findViewById(R.id.priceTextView);

        new GetPriceTask().execute("https://www.amazon.de/DJI-CP-PT-000498-Mavic-Drohne-grau/dp/B01M0AVO1P/");
    }

    private class GetPriceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            Document document = null;
            try {
                document = Jsoup.connect(url).userAgent("Mozilla/49.0").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements question = document.select("#priceblock_ourprice");
            return question.html();
        }

        @Override
        protected void onPostExecute(String price) {
            priceTextView.setText("Price is " + price);
        }
    }
}

