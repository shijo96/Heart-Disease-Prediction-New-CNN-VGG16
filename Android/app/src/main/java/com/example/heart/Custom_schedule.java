package com.example.heart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

public class Custom_schedule  extends ArrayAdapter<String> {

    private Activity context;       //for to get current activity context
    SharedPreferences sh;
    private String[] day,start_time,end_time;


    public Custom_schedule(Activity context,String[] day,String[] start_time, String[] end_time) {
        super(context, R.layout.custom_schedule, day);
        this.context = context;
        this.day = day;
        this.start_time = start_time;
        this.end_time = end_time;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //override getView() method

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custom_schedule, null, true);
        //cust_list_view is xml file of layout created in step no.2

//        ImageView im = (ImageView) listViewItem.findViewById(R.id.imageView);
        ImageView im = listViewItem.findViewById(R.id.imageView);
        TextView t=(TextView)listViewItem.findViewById(R.id.textView7);
        TextView t1=(TextView)listViewItem.findViewById(R.id.textView25);
        TextView t2=(TextView)listViewItem.findViewById(R.id.textView39);
//        TextView t3=(TextView)listViewItem.findViewById(R.id.textView40);


        t.setText(day[position]);
        t1.setText("From : "+start_time[position]);
        t2.setText("To : "+end_time[position]);
//        t3.setText(statuss[position]);

        // Check gender and set the appropriate background
//        if (gender[position].equalsIgnoreCase("Male")) {
            im.setBackgroundResource(R.drawable.dt); // Set image for Male
//        } else if (gender[position].equalsIgnoreCase("Female")) {
//            im.setBackgroundResource(R.drawable.d2); // Set image for Female
//        }


//
//        sh= PreferenceManager.getDefaultSharedPreferences(getContext());
//
//        String pth = "http://"+sh.getString("ip", "")+"/"+crime_type_name[position];
//        pth = pth.replace("~", "");
////	       Toast.makeText(context, pth, Toast.LENGTH_LONG).show();
//
//        Log.d("-------------", pth);
////        Picasso.with(context)
//                .load(pth)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background).into(im);

        return  listViewItem;
    }





    private TextView setText(String string) {
        // TODO Auto-generated method stub
        return null;
    }
}