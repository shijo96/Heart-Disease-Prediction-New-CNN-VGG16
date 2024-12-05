package com.example.heart;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

public class User_view_bookings extends AppCompatActivity implements JsonResponse, AdapterView.OnItemClickListener{

    ListView l1;


    String[] login_id,doctor_id,first_name,house_name,gender,place,landmark,qualification,phone,email,cdate,cday,cstart,cend,bstatus,pstatus;

    public static String login_ids,doctor_ids,genders,first_names,qualifications,cdates,cdays,cstarts,cends,bstatuss,pstatuss;
    SharedPreferences sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_view_bookings);
        // Set the status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.home));
        }




        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        l1=(ListView) findViewById(R.id.vdoctors);
        l1.setOnItemClickListener(this);

        JsonReq JR= new JsonReq(User_view_bookings.this);
        JR.json_response=(JsonResponse)User_view_bookings.this;
        String q="/User_view_bookings?user_id="+sh.getString("uid","");
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
            if (method.equalsIgnoreCase("User_view_bookings")) {
                String status = jo.getString("status");
                Log.d("pearl", status);
                if (status.equalsIgnoreCase("success")) {
                    JSONArray ja1 = (JSONArray) jo.getJSONArray("data");
                    login_id = new String[ja1.length()];
                    doctor_id = new String[ja1.length()];
                    first_name = new String[ja1.length()];
                    house_name = new String[ja1.length()];
                    gender = new String[ja1.length()];
                    place = new String[ja1.length()];
                    landmark = new String[ja1.length()];
                    qualification = new String[ja1.length()];
                    phone = new String[ja1.length()];
                    email = new String[ja1.length()];
                    cdate = new String[ja1.length()];
                    cday = new String[ja1.length()];
                    cstart = new String[ja1.length()];
                    cend = new String[ja1.length()];
                    bstatus = new String[ja1.length()];
                    pstatus = new String[ja1.length()];

                    for (int i = 0; i < ja1.length(); i++) {
                        login_id[i] = ja1.getJSONObject(i).getString("login_id");
                        doctor_id[i] = ja1.getJSONObject(i).getString("doctor_id");
                        first_name[i] = ja1.getJSONObject(i).getString("first_name")+" "+ja1.getJSONObject(i).getString("last_name");
                        house_name[i] = ja1.getJSONObject(i).getString("house_name");
                        gender[i] = ja1.getJSONObject(i).getString("gender");
                        place[i] = ja1.getJSONObject(i).getString("place");
                        landmark[i] = ja1.getJSONObject(i).getString("landmark");
                        qualification[i] = ja1.getJSONObject(i).getString("qualification");
                        phone[i] = ja1.getJSONObject(i).getString("phone");
                        email[i] = ja1.getJSONObject(i).getString("email");
                        cdate[i] = ja1.getJSONObject(i).getString("cdate");
                        cday[i] = ja1.getJSONObject(i).getString("cday");
                        cstart[i] = ja1.getJSONObject(i).getString("cstart");
                        cend[i] = ja1.getJSONObject(i).getString("cend");
                        bstatus[i] = ja1.getJSONObject(i).getString("bstatus");
                        pstatus[i] = ja1.getJSONObject(i).getString("pstatus");


                    }
//                    ArrayAdapter<String> ar = new ArrayAdapter<String>(getApplicationContext(), R.layout.custtext, val);
                    Custom_bookings ar=new Custom_bookings(User_view_bookings.this,first_name,gender,qualification,email,cdate,cday,cstart,cend,bstatus,pstatus);
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

        login_ids=login_id[arg2];
        doctor_ids=doctor_id[arg2];
        genders=gender[arg2];
        first_names=first_name[arg2];
        qualifications=qualification[arg2];

        SharedPreferences.Editor ed = sh.edit();
        ed.putString("login_ids", login_ids);
        ed.putString("doctor_ids", doctor_ids);
        ed.putString("genders", genders);
        ed.putString("first_names", first_names);
        ed.putString("qualifications", qualifications);
        ed.apply(); // Save the IP value asynchronously

//
        final CharSequence[] items = {"View Schedule","Review","Chat","Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(User_view_bookings.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("View Schedule"))
                {
                    startActivity(new Intent(getApplicationContext(),User_view_doctor_schedule.class));
                }
                if (items[item].equals("Review"))
                {
                    startActivity(new Intent(getApplicationContext(),User_add_review.class));
                }
                if (items[item].equals("Chat"))
                {
                    startActivity(new Intent(getApplicationContext(),User_chat.class));
                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();
//	Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(i, GALLERY_CODE);
    }


}

