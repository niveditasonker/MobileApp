package com.nyu.cs9033.eta.controllers;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;


import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.R.integer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";
	Button create, view;
	int request_Code = 1;
	String check = "";
	String date = "", location = "", time = "", name = "";
	String current_trip = "";
	InputStream inputStream = null;
	String result = null;
	int secondsDelayed = 3;
	String id = "";
	TripDatabaseHelper tdh = new TripDatabaseHelper(this);
	TextView tv ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {	//Creates
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		create = (Button) findViewById(R.id.createbutton);
		view = (Button) findViewById(R.id.viewbutton);
		Trip trip = getIntent().getParcelableExtra("trip");

		if (trip != null) {
			date = trip.date;
			location = trip.location;
			name = trip.name;
			time = trip.time;
			check = trip.check;
			Log.d("", check);
			Log.d("", name);

		}

		create.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startCreateTripActivity();

			}
		});

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int request_code = 0;
				Intent viewTrip = new Intent (getBaseContext(), ViewTripActivity.class );
			startActivityForResult(viewTrip, request_code);
				
			}
		});
		

		}
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			return POST(urls[0]);
		}
		protected void onPostExecute(String param){
			Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
		}
	}
	
	public String POST(String url){
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("command", "TRIP_STATUS");
			jsonObject.accumulate("trip_id", id);			
			String json = jsonObject.toString();
			StringEntity entity = new StringEntity (json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			HttpResponse httpResponse = httpClient.execute(httpPost);
			inputStream = httpResponse.getEntity().getContent();
			
			if (inputStream!=null)
			{
				result = convertInputStreamtoString(inputStream);
				
			}
			else 
				{result = "Unable to get trip_status";
				}
			Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
			}
		catch(Exception e){
			e.printStackTrace();
		}
		return result;
		
		
	}
	
	private static String convertInputStreamtoString(InputStream inputStream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while((line = br.readLine())!= null)
		result += line;
		inputStream.close();
		Log.d("In tripstatus method", result);
		return result;

	}

	/**
	 * This method should start the Activity responsible for creating a Trip.
	 */
	public void startCreateTripActivity() {			//Starts create trip activity
		Intent goToCreate = new Intent(this, CreateTripActivity.class);
		startActivity(goToCreate);
		// TODO - fill in here
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		 
		if (requestCode==0){
			if(resultCode==RESULT_OK){
			current_trip = data.getDataString();
			Log.d(current_trip, current_trip);
			id = tdh.getID(current_trip);
				
				Toast.makeText(getBaseContext(), id, Toast.LENGTH_LONG).show();
				new HttpAsyncTask().execute("http://cs9033-homework.appspot.com");
				new Handler().postDelayed(new Runnable() {
					
					//updateTripStatus
					
					@Override
					public void run() {
						
						int res =tdh.updateTripStatus(id);
						Toast.makeText(getBaseContext(), "status updated "+res, Toast.LENGTH_LONG).show();
						
					}
				}, secondsDelayed*1000);
				

			}
		
				
			}
		}
		
	}

	/**
	 * This method should start the Activity responsible for viewing a Trip.
	 */

	

	

