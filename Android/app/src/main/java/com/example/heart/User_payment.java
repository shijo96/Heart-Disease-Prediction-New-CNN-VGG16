package com.example.heart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class User_payment extends AppCompatActivity implements PaymentResultListener,JsonResponse {

    Button b1;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_payment);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        ImageView imageView = findViewById(R.id.imageView3);

        Glide.with(this)
                .load(R.drawable.py)
                .transform(new CircleCrop())
                .into(imageView);


        b1=findViewById(R.id.btn_pay);
        b1.setOnClickListener(v -> {



            startPayment();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Razorpay Checkout
        Checkout.preload(getApplicationContext());

        // Start payment process
//        startPayment();
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_edrzdb8Gbx5U5M");

        // Set up payment details
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Your Company Name");
            options.put("description", "Payment for your order");
            options.put("currency", "INR");
            options.put("amount", "25000"); // Amount is in currency subunits (e.g., "100" for 1.00 INR)
            options.put("prefill.email", "test@example.com");
            options.put("prefill.contact", "9400278981");

            // Open Razorpay Checkout activity
            checkout.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in starting payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentId) {
//        Toast.makeText(this, "Payment successful: " + razorpayPaymentId, Toast.LENGTH_LONG).show();

        JsonReq JR= new JsonReq(User_payment.this);
        JR.json_response=(JsonResponse)User_payment.this;
        String q="/User_book_doctor?login_id=" + sh.getString("logid", "")+"&consulting_ids="+sh.getString("consulting_ids","")+"&date="+sh.getString("date","");
        JR.execute(q);


        // Handle successful payment here (e.g., update backend, show confirmation, etc.)
    }

    @Override
    public void onPaymentError(int code, String description) {
        Toast.makeText(this, "Payment failed: " + description, Toast.LENGTH_LONG).show();
        // Handle failed payment here (e.g., show error message, retry, etc.)
    }



    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            if (method.equalsIgnoreCase("User_book_doctor")) {
                String status = jo.getString("status");
                Log.d("pearl", status);
                if (status.equalsIgnoreCase("success")) {

//                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),User_home.class));
                }

                else

                {
                    Toast.makeText(getApplicationContext(), "Failed!!", Toast.LENGTH_LONG).show();

                }
            }


        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


}
