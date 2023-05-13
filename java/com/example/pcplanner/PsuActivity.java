package com.example.pcplanner;

import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.pcplanner.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class PsuActivity extends AppCompatActivity {

    private TextView itemNameTextView;
    private final String FIELD1 = "Processor Number";
    private final String FIELD2 = "Lithography";
    private final String FIELD3 = "Vertical Segment";

    private String processorNumber;
    private String priceNumber;
    private String generationNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psu);

        itemNameTextView = findViewById(R.id.item_name_text_view);

        descrip dw = new descrip();
        dw.execute();

    }

    public class descrip extends AsyncTask<Void, Void, String>{

        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {

            org.jsoup.nodes.Document document = null;
            try {
                document = Jsoup.connect("https://www.intel.com/content/www/us/en/products/sku/134594/intel-core-i712700k-processor-25m-cache-up-to-5-00-ghz/specifications.html?wapkw=i7").get();
            }
            catch (IOException e){
                e.printStackTrace();
            }

            Element processorrow = document.select("div.tech-label:contains(" + FIELD1 + ")").first().parent();
            processorNumber = processorrow.select("div.tech-data").first().text();
            Element pricerow = document.select("div.tech-label:contains(" + FIELD2 + ")").first().parent();
            priceNumber = pricerow.select("div.tech-data").first().text();
            Element generationrow = document.select("div.tech-label:contains(" + FIELD3 + ")").first().parent();
            generationNumber = generationrow.select("div.tech-data").first().text();

            return processorNumber;

        }

        public void onPostExecute(String result){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String displayText = "Processor Number: " + processorNumber + "\nPrice Number: " + priceNumber + "\nGeneration Number: " + generationNumber;
                    itemNameTextView.setText(displayText);
                }
            });
        }

    }
}
