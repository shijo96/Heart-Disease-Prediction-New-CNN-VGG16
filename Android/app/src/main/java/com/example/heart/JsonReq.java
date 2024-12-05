package com.example.heart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.heart.JsonResponse;
public class JsonReq extends AsyncTask<String, Void, String> {
	public JsonResponse json_response = null;
	SharedPreferences sh;
	Context context;

	public JsonReq(Context context) {
		this.context = context;
	}

	public void setJsonResponseListener(JsonResponse listener) {
		this.json_response = listener;
	}

	@Override
	protected String doInBackground(String... arg) {
		HttpURLConnection c = null;
		sh = PreferenceManager.getDefaultSharedPreferences(context);
		String ip = sh.getString("ip", "");
		String jsonReqUrl = "http://" + ip + "/api" + arg[0];
		Log.d("pearl", jsonReqUrl);

		try {
			URL u = new URL(jsonReqUrl);
			c = (HttpURLConnection) u.openConnection();
			c.setRequestMethod("GET");
			c.setRequestProperty("Content-length", "0");
			c.setUseCaches(false);
			c.setAllowUserInteraction(false);
			c.connect();
			int status = c.getResponseCode();

			switch (status) {
				case 200:
				case 201:
					BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
					StringBuilder sb = new StringBuilder();
					String line = "";
					while ((line = br.readLine()) != null) {
						sb.append(line + "\n");
					}
					br.close();
					return sb.toString();
				default:
					return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.disconnect();
			}
		}
		return "";
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.d("pearl", result);
		try {
			JSONObject jo = new JSONObject(result);
			if (json_response != null) {
				json_response.response(jo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}