package com.example.delivery;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText etShipmentId, etOtp;
    private Button btnConfirm, btnResendOtp;
    private TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etShipmentId = findViewById(R.id.etShipmentId);
        etOtp = findViewById(R.id.etOtp);
        etOtp = findViewById(R.id.etOtp);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnResendOtp = findViewById(R.id.btnResendOtp);
        tvStatus = findViewById(R.id.tvStatus);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shipmentId = etShipmentId.getText().toString().trim();
                String otp = etOtp.getText().toString().trim();

                if (shipmentId.isEmpty() || otp.isEmpty()) {
                    updateStatus("Please fill all fields", Color.RED);
                    return;
                }

                new ConfirmDeliveryTask().execute(shipmentId, otp);
            }
        });

        btnResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shipmentId = etShipmentId.getText().toString().trim();

                if (shipmentId.isEmpty()) {
                    updateStatus("Please enter Shipment ID to resend OTP", Color.RED);
                    return;
                }

                new RequestOtpTask().execute(shipmentId);
            }
        });
    }

    private void updateStatus(String message, int color) {
        tvStatus.setText(message);
        tvStatus.setTextColor(color);
        tvStatus.setVisibility(View.VISIBLE);
    }

    private class ConfirmDeliveryTask extends AsyncTask<String, Void, String> {
        private int responseCode;

        @Override
        protected String doInBackground(String... params) {
            String shipmentId = params[0];
            String otp = params[1];
            String agentName = "Agent John Doe"; // Hardcoded agent for demo

            try {
                URL url = new URL("http://10.0.2.2:3000/api/confirm-delivery");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("shipmentId", shipmentId);
                jsonParam.put("otp", otp);
                jsonParam.put("agentName", agentName);

                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                responseCode = conn.getResponseCode();

                BufferedReader br;
                if (responseCode >= 200 && responseCode < 300) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
                responseCode = -1;
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (responseCode == 200) {
                updateStatus(result, Color.GREEN);
            } else {
                updateStatus(result, Color.RED);
            }
        }
    }
    private class RequestOtpTask extends AsyncTask<String, Void, String> {
        private int responseCode;

        @Override
        protected String doInBackground(String... params) {
            String shipmentId = params[0];

            try {
                URL url = new URL("http://10.0.2.2:3000/api/request-otp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("shipmentId", shipmentId);

                OutputStream os = conn.getOutputStream();
                os.write(jsonParam.toString().getBytes("UTF-8"));
                os.close();

                responseCode = conn.getResponseCode();

                BufferedReader br;
                if (responseCode >= 200 && responseCode < 300) {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                }
                
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();

            } catch (Exception e) {
                e.printStackTrace();
                responseCode = -1;
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (responseCode == 200) {
                updateStatus(result, Color.BLUE); // Use Blue for info/OTP sent
            } else {
                updateStatus(result, Color.RED);
            }
        }
    }
}
