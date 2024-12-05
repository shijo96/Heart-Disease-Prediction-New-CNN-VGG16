package com.example.heart;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

public class Custom_bookings extends ArrayAdapter<String> {

    private Activity context;       //for to get current activity context
    SharedPreferences sh;
    private String[] first_name,gender,qualification,email,cdate,cday,cstart,cend,bstatus,pstatus;


    public Custom_bookings(Activity context, String[] first_name, String[] gender, String[] qualification, String[] email, String[] cdate,
    String[] cday, String[] cstart, String[] cend, String[] bstatus, String[] pstatus) {
        super(context, R.layout.custom_bookings, first_name);
        this.context = context;
        this.first_name = first_name;
        this.gender = gender;
        this.qualification = qualification;
        this.email=email;
        this.cdate=cdate;
        this.cday=cday;
        this.cstart=cstart;
        this.cend=cend;
        this.bstatus=bstatus;
        this.pstatus=pstatus;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //override getView() method

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custom_bookings, null, true);
        //cust_list_view is xml file of layout created in step no.2

//        ImageView im = (ImageView) listViewItem.findViewById(R.id.imageView);
        ImageView im = listViewItem.findViewById(R.id.imageView);
        TextView t=(TextView)listViewItem.findViewById(R.id.textView7);
        TextView t1=(TextView)listViewItem.findViewById(R.id.textView25);
        TextView t2=(TextView)listViewItem.findViewById(R.id.textView39);
        TextView t3=(TextView)listViewItem.findViewById(R.id.textView40);
        TextView t4=(TextView)listViewItem.findViewById(R.id.textView41);
        TextView t5=(TextView)listViewItem.findViewById(R.id.textView42);
        TextView t6=(TextView)listViewItem.findViewById(R.id.textView43);


        t.setText(first_name[position]);
        t1.setText(qualification[position]);
        t2.setText(email[position]);
        t3.setText("Date : "+cdate[position]);
        t4.setText("Session : "+cday[position] + "\nTime : "+cstart[position]+"-"+cend[position]);
        t5.setText("Booking Status : "+bstatus[position]);
        t6.setText("Payment Status : "+pstatus[position]);

        // Check gender and set the appropriate background
        if (gender[position].equalsIgnoreCase("Male")) {
            im.setBackgroundResource(R.drawable.d1); // Set image for Male
        } else if (gender[position].equalsIgnoreCase("Female")) {
            im.setBackgroundResource(R.drawable.d2); // Set image for Female
        }


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