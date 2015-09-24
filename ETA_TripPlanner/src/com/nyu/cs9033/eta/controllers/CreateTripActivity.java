package com.nyu.cs9033.eta.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.provider.ContactsContract;

public class CreateTripActivity extends Activity {
	EditText trip_name;
	String tname = "";
	EditText t_location;
	String location = "";
	Button OK;
	EditText Date;
	String date = " ";
	String check = "OK";
	String time = "";
	TimePicker timepicker;
	Button Select_Friends;
	Button Select_Location;
	String number = "";
	Spinner friends;
	ArrayList<String> s_friends = new ArrayList<String>();
	String type = "";
	EditText Location;
	String Trip_location = "";
	TripDatabaseHelper dbHelper = new TripDatabaseHelper(this);
	InputStream inputStream = null;
	String result = null;
	int secondsDelayed = 2;
	String locationData = "";
	
	private static final String TAG = "CreateTripActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip);
		OK = (Button) findViewById(R.id.button1);
		trip_name = (EditText) findViewById(R.id.tripname);
		
		Log.v("inCreateTrip", tname);
		t_location = (EditText) findViewById(R.id.location);
		
		Date = (EditText) findViewById(R.id.date);
		Log.v("inCreateTrip", location);
		timepicker = (TimePicker) findViewById(R.id.timePicker);
		timepicker.setIs24HourView(true);
		Select_Friends = (Button) findViewById(R.id.friendsbutton);
		friends = (Spinner) findViewById(R.id.spinner1);
		Select_Location = (Button) findViewById(R.id.locationbutton);
		// s_friends = (EditText) findViewById(R.id.friendsedit);
		
	
		Select_Friends.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
											
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
				startActivityForResult(intent, 1);
			}
		});

	Select_Location.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				t_location = (EditText) findViewById(R.id.location);
//				Trip_location = t_location.getText().toString();
//			System.out.println("In Trip_location"+Trip_location);
			String text = "nyu, new york::indian";
				Uri u = Uri.parse("location://com.example.nyu.hw3api");
				Intent test = new Intent(Intent.ACTION_VIEW, u);
				test.putExtra("searchVal", text);
				startActivityForResult(test, 2);
				
			}
		});
		OK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tname = trip_name.getText().toString();
				location = trip_name.getText().toString();
				date = Date.getText().toString();
				time = timepicker.getCurrentHour() + ":"
						+ timepicker.getCurrentMinute();
//				Trip trip = new Trip(trip_name.getText().toString(), t_location
//						.getText().toString(), Date.getText().toString(), time,
//						check);
				new HttpAsyncTask().execute("http://cs9033-homework.appspot.com");
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {
						System.out.println("result>>>>>>>>>>>"+result);
						String[] resultData = result.split(":");
						String ID = resultData[2].substring(0,resultData[2].length()-1);
						dbHelper.createTrip(location, tname, s_friends, date, time, ID);
						Intent i = new Intent(getBaseContext(), MainActivity.class);
						

						startActivity(i);
						
					}
				}, secondsDelayed*1000);
				

			}
		});
		// TODO - fill in here
	}

	/**
	 * This method should be used to instantiate a Trip model object.
	 * 
	 * @return The Trip as represented by the View.
	 */
	public Trip createTrip(View v) {

		// Intent intent = new Intent(this, MainActivity.class);
		// Bundle b = new Bundle();
		// b.putString("tripName",tname);
		// intent.putExtras(b);
		// startActivity(intent);
		
		

		return null;
	}
	private class HttpAsyncTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			// TODO Auto-generated method stub
			return POST(urls[0]);
		}
		protected void onPostExecute(String result){
			Toast.makeText(getBaseContext(), "Data sent succesfully", Toast.LENGTH_LONG).show();
		}
	}
	
	public String POST(String url){
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.accumulate("command", "CREATE_TRIP");
			jsonObject.accumulate("location", locationData);
			jsonObject.accumulate("datetime",date + "," + time);
			jsonObject.accumulate("people", s_friends);
			
			String json = jsonObject.toString();
			StringEntity entity = new StringEntity (json);
			httpPost.setEntity(entity);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			HttpResponse httpResponse = httpClient.execute(httpPost);
			inputStream = httpResponse.getEntity().getContent();
			
			if (inputStream!=null)
			
				result = convertInputStreamtoString(inputStream);
			else result = "Unable to connect";
			
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
		Log.d("In convert method", result);
		return result;
		
	}
	/**
	 * For HW2 you should treat this method as a way of sending the Trip data
	 * back to the main Activity.
	 * 
	 * Note: If you call finish() here the Activity will end and pass an Intent
	 * back to the previous Activity using setResult().
	 * 
	 * @return whether the Trip was successfully saved.
	 */
	public boolean saveTrip(Trip trip) {

		// TODO - fill in here

		return false;
	}

	/**
	 * This method should be used when a user wants to cancel the creation of a
	 * Trip.
	 * 
	 * Note: You most likely want to call this if your activity dies during the
	 * process of a trip creation or if a cancel/back button event occurs.
	 * Should return to the previous activity without a result using finish()
	 * and setResult().
	 */
	public void cancelTrip(View v) {
		Intent I = new Intent(this, MainActivity.class);
		startActivity(I);

		// TODO - fill in here
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
	
		
		if (data != null) {
			if(requestCode==1){
			Uri uri = data.getData();

			if (uri != null) {
				Cursor c = null;
				try {
					c = getContentResolver()
							.query(uri,
									new String[] {
											ContactsContract.CommonDataKinds.Phone.NUMBER,
											ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },
									null, null, null);

					if (c != null && c.moveToFirst()) {
						number = c.getString(0);
						type = c.getString(1);
						s_friends.add(type);
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(
								this, android.R.layout.simple_spinner_item,
								s_friends); // find other layout parameters
						friends.setAdapter(adapter);

					}
				} finally {
					if (c != null) {
						c.close();
					}

				} 
					
				}
			}else if(requestCode==2){
			final	ArrayList<String> locDet = data.getStringArrayListExtra("retVal");
			locationData = locDet.get(0)+","+locDet.get(1)+","+ locDet.get(2)+","+locDet.get(3);
			Toast.makeText(getBaseContext(), locationData, Toast.LENGTH_LONG).show();
	        	for(String det : locDet){
	                Log.d(TAG+"-LOC-DETAILS", det);
	                
	                t_location.setText(locDet.get(0));
		}


		}

	}
}
}
