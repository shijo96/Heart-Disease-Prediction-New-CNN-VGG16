package com.example.heart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

public class User_view_doctor_schedule extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener {

    SharedPreferences sh;
    ImageView im;
    TextView t1,t2;
    ListView l1;
    String[] consulting_id,day,start_time,end_time;
    public static String consulting_ids,days,start_times,end_times;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_doctor_schedule);

        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
         im = findViewById(R.id.imgv);

        Toast.makeText(getApplicationContext(),sh.getString("genders",""),Toast.LENGTH_LONG).show();

        t1=findViewById(R.id.dname);
        t2=findViewById(R.id.qual);

        t1.setText(sh.getString("first_names",""));
        t2.setText(sh.getString("qualifications",""));

        l1=(ListView) findViewById(R.id.vdoctors);
        l1.setOnItemClickListener(this);
//
        // Check gender and set the appropriate background
        if (sh.getString("genders","").equalsIgnoreCase("Male")) {
            im.setBackgroundResource(R.drawable.d1); // Set image for Male
        } else if (sh.getString("genders","").equalsIgnoreCase("Female")) {
            im.setBackgroundResource(R.drawable.d2); // Set image for Female
        }

        JsonReq JR= new JsonReq(User_view_doctor_schedule.this);
        JR.json_response=(JsonResponse)User_view_doctor_schedule.this;
        String q="/User_view_doctor_schedule?doctor_ids="+sh.getString("doctor_ids","");
        JR.execute(q);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void response(JSONObject jo) {
        try {
            String method = jo.getString("method");
            if (method.equalsIgnoreCase("User_view_doctor_schedule")) {
                String status = jo.getString("status");
                Log.d("pearl", status);
                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    consulting_id = new String[ja1.length()];
                    day = new String[ja1.length()];
                    start_time = new String[ja1.length()];
                    end_time = new String[ja1.length()];




                    for (int i = 0; i < ja1.length(); i++) {
                        consulting_id[i] = ja1.getJSONObject(i).getString("consulting_id");
                        day[i] = ja1.getJSONObject(i).getString("day");
                        start_time[i] = ja1.getJSONObject(i).getString("start_time");
                        end_time[i] = ja1.getJSONObject(i).getString("end_time");



                    }
//                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), R.layout.custtext, val);
                    Custom_schedule ar=new Custom_schedule(User_view_doctor_schedule.this,day,start_time,end_time);
                    l1.setAdapter(ar);

                }


                else

                {
                    Toast.makeText(getApplicationContext(), "No data!!", Toast.LENGTH_LONG).show();

                }
            }


        }catch(Exception e)
        {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }



    }



    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

        consulting_ids=consulting_id[arg2];
        days=day[arg2];
        start_times=start_time[arg2];
        end_times=end_time[arg2];


        SharedPreferences.Editor ed = sh.edit();
        ed.putString("consulting_ids", consulting_ids);
        ed.putString("days", days);
        ed.putString("start_times", start_times);
        ed.putString("end_times", end_times);
        ed.apply(); // Save the IP value asynchronously

        startActivity(new Intent(getApplicationContext(),User_book_doctor.class));

    }
}