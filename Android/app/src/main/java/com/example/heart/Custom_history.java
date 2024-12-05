//package com.example.heart;
//
//import android.app.Activity;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
////import com.squareup.picasso.Picasso;
//
//public class Custom_history extends ArrayAdapter<String> {
//
//    private Activity context;       //for to get current activity context
//    SharedPreferences sh;
//    private String[] file_path, result, date_time;
//
//
//    public Custom_history(Activity context, String[] file_path, String[] result, String[] date_time) {
//        super(context, R.layout.custom_history, file_path);
//        this.context = context;
//        this.file_path = file_path;
//        this.result = result;
//        this.date_time = date_time;
//
//
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        //override getView() method
//
//        LayoutInflater inflater = context.getLayoutInflater();
//        View listViewItem = inflater.inflate(R.layout.custom_history, null, true);
//        //cust_list_view is xml file of layout created in step no.2
//
//        sh= PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
//
////        ImageView im = (ImageView) listViewItem.findViewById(R.id.imageView);
//        ImageView im = listViewItem.findViewById(R.id.imageView);
//        TextView t2=(TextView)listViewItem.findViewById(R.id.textView40);
//        TextView t3=(TextView)listViewItem.findViewById(R.id.textView41);
//
//        t2.setText(result[position]);
//        t3.setText(date_time[position]);
//
//
////
//        sh= PreferenceManager.getDefaultSharedPreferences(getContext());
//
//        String pth = "http://"+sh.getString("ip", "")+"/"+file_path[position];
//        pth = pth.replace("~", "");
////	       Toast.makeText(context, pth, Toast.LENGTH_LONG).show();
//
//        Log.d("-------------", pth);
////        Picasso.with(context)
//                .load(pth)
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background).into(im);
//
//        return  listViewItem;
//    }
//
//
//
//
//
//    private TextView setText(String string) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//}



package com.example.heart;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso; // Ensure Picasso is imported

public class Custom_history extends ArrayAdapter<String> {

    private Activity context;       // For getting current activity context
    SharedPreferences sh;
    private String[] file_path, result, date_time;

    // Constructor for the custom adapter
    public Custom_history(Activity context, String[] file_path, String[] result, String[] date_time) {
        super(context, R.layout.custom_history, file_path);
        this.context = context;
        this.file_path = file_path;
        this.result = result;
        this.date_time = date_time;
    }

    // Override the getView() method to populate each list item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate a new view
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.custom_history, parent, false);
        }

        // Get references to the views in the list item
        ImageView im = convertView.findViewById(R.id.imageView);
        TextView t2 = convertView.findViewById(R.id.textView40);
        TextView t3 = convertView.findViewById(R.id.textView41);

        // Set the text views
        t2.setText(result[position]);
        t3.setText(date_time[position]);

        // Get the shared preferences
        sh = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Create the URL path for the image
        String pth = "http://" + sh.getString("ip", "") + "/" + file_path[position];
        pth = pth.replace("~", "");

        // Log the image URL (for debugging purposes)
        Log.d("Image Path", pth);

        // Load the image using Picasso
        Picasso.get()
                .load(pth)
                .placeholder(R.drawable.ic_launcher_background) // Placeholder image
                .error(R.drawable.ic_launcher_background) // Error image
                .into(im);

        return convertView;
    }
}
