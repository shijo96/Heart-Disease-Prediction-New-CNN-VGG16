package com.example.heart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

public class User_profile extends AppCompatActivity implements JsonResponse{

    TextView t0,t1,t2,t3,t4,t5;
    Button b1;
    SharedPreferences sh;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        sh=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        t0=findViewById(R.id.uname);
        t1=findViewById(R.id.name);
        t2=findViewById(R.id.hname);
        t3=findViewById(R.id.place);
        t4=findViewById(R.id.phone);
        t5=findViewById(R.id.email);

        b1=findViewById(R.id.buttonLogout);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        JsonReq JR= new JsonReq(User_profile.this);
        JR.json_response=(JsonResponse)User_profile.this;
        String q="/User_profile?login_id=" + sh.getString("logid", "");
        JR.execute(q);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }



    public void response(JSONObject jo) {
        // TODO Auto-generated method stub
        try {
            String status = jo.getString("status");
            Log.d("result", status);
            if (status.equalsIgnoreCase("success")) {
                JSONArray ja = (JSONArray) jo.getJSONArray("data");
//                String username = ja.getJSONObject(0).getString("username");
                t0.setText(ja.getJSONObject(0).getString("username"));
                t1.setText(ja.getJSONObject(0).getString("first_name") + " " + ja.getJSONObject(0).getString("last_name"));
                t2.setText(ja.getJSONObject(0).getString("house_name"));
                t3.setText(ja.getJSONObject(0).getString("place"));
                t4.setText(ja.getJSONObject(0).getString("phone"));
                t5.setText(ja.getJSONObject(0).getString("email"));

            }
            else {
                Toast.makeText(getApplicationContext(), "Sorry. No Data", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }


    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        super.onBackPressed();
        Intent b=new Intent(getApplicationContext(),User_home.class);
        startActivity(b);
    }


}