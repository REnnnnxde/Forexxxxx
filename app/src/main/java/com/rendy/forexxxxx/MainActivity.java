package com.rendy.forexxxxx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private ProgressBar loadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout1;

    private TextView audTextView, bndTextView, cadTextView, chfTextView,
            cnyTextView, eurTextView, gbpTextView, jpyTextView,
            myrTextView, sgdTextView, thbTextView, usdTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews
        audTextView = findViewById(R.id.audTextView);
        bndTextView = findViewById(R.id.bndTextView);
        cadTextView = findViewById(R.id.cadTextView);
        chfTextView = findViewById(R.id.chfTextView);
        cnyTextView = findViewById(R.id.cnyTextView);
        eurTextView = findViewById(R.id.eurTextView);
        gbpTextView = findViewById(R.id.gbpTextView);
        jpyTextView = findViewById(R.id.jpyTextView);
        myrTextView = findViewById(R.id.myrTextView);
        sgdTextView = findViewById(R.id.sgdTextView);
        thbTextView = findViewById(R.id.thbTextView);
        usdTextView = findViewById(R.id.usdTextView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        swipeRefreshLayout1 = findViewById(R.id.swipeRefreshLayout1);

        initSwipeRefreshLayout();
        initForex();
    }

    private void initSwipeRefreshLayout() {
        swipeRefreshLayout1.setOnRefreshListener(() -> {
            initForex();
            swipeRefreshLayout1.setRefreshing(false);
        });
    }

    private void initForex() {
        loadingProgressBar.setVisibility(View.VISIBLE); // Show progress bar

        String url = "https://openexchangerates.org/api/latest.json?app_id=cc5e1edb66e143ba9e4681180a569000";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(response);
                    displayExchangeRates(jsonObject); // Update UI with exchange rates
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
                loadingProgressBar.setVisibility(View.GONE); // Hide progress bar
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                loadingProgressBar.setVisibility(View.GONE); // Hide progress bar on failure
                // Handle failure, e.g., show error message
                Toast.makeText(MainActivity.this, "Failed to fetch exchange rates", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayExchangeRates(JSONObject jsonObject) {
        try {
            // Parse exchange rates from JSON
            JSONObject rates = jsonObject.getJSONObject("rates");

            // Update TextViews with formatted exchange rates
            audTextView.setText(FormatNumber(rates.getDouble("AUD"), "0.00"));
            bndTextView.setText(FormatNumber(rates.getDouble("BND"), "0.00"));
            cadTextView.setText(FormatNumber(rates.getDouble("CAD"), "0.00"));
            chfTextView.setText(FormatNumber(rates.getDouble("CHF"), "0.00"));
            cnyTextView.setText(FormatNumber(rates.getDouble("CNY"), "0.00"));
            eurTextView.setText(FormatNumber(rates.getDouble("EUR"), "0.00"));
            gbpTextView.setText(FormatNumber(rates.getDouble("GBP"), "0.00"));
            jpyTextView.setText(FormatNumber(rates.getDouble("JPY"), "0.00"));
            myrTextView.setText(FormatNumber(rates.getDouble("MYR"), "0.00"));
            sgdTextView.setText(FormatNumber(rates.getDouble("SGD"), "0.00"));
            thbTextView.setText(FormatNumber(rates.getDouble("THB"), "0.00"));
            usdTextView.setText(FormatNumber(rates.getDouble("USD"), "0.00"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String FormatNumber(double number, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(number);
    }
}