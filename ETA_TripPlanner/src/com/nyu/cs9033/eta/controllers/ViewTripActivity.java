package com.nyu.cs9033.eta.controllers;

import java.util.ArrayList;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewTripActivity extends Activity {
	ListView triplist;

	ArrayList<String> list = new ArrayList<String>();
//	TripDatabaseHelper dbhelper = new TripDatabaseHelper(this);
	TextView Location, Date, Time, Name, Friends;
	String check, name, location="", time, date, friends;
	Spinner triphistory;
	Button current_trip;
	TripDatabaseHelper dbHelper = new TripDatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_history);
		
		Name=(TextView)findViewById(R.id.t_name);
		Location=(TextView)findViewById(R.id.t_location);
		Date=(TextView)findViewById(R.id.t_date);
		Time=(TextView)findViewById(R.id.t_time);
		Friends=(TextView)findViewById(R.id.t_friends);
		triphistory=(Spinner)findViewById(R.id.triplist);
		current_trip=(Button)findViewById(R.id.currenttrip);
		
		
		ArrayList<String> tripListed= new ArrayList<String>();
		tripListed=dbHelper.triplist();
		if(tripListed!=null)
		{
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tripListed);
		triphistory.setAdapter(adapter);
		}
		
		triphistory.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Trip newTrip = dbHelper.tripDetail(triphistory.getItemAtPosition(
					       position).toString());
					     System.out.println("called"+triphistory.getItemAtPosition(
					       position).toString());
					     ArrayList<String> newFriendList = new ArrayList<String>();

					     Name = (TextView)findViewById(R.id.t_name);
					     Name.setText(newTrip.getName());
					     Location = (TextView)findViewById(R.id.t_location);
					     Location.setText(newTrip.getLocation());
					     Date = (TextView)findViewById(R.id.t_date);
					     Date.setText(newTrip.getDate());
					     Time = (TextView)findViewById(R.id.t_time);
					     Time.setText(newTrip.getTime());
					     Friends = (TextView)findViewById(R.id.t_friends);
					     Friends.setText(newTrip.getFriends());
					     System.out.println("Friends coming"+newTrip.getFriends());
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		current_trip = (Button) findViewById(R.id.currenttrip);
		
		current_trip.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			Intent data = new Intent();
			data.setData(Uri.parse(Name.getText().toString()));
			setResult(RESULT_OK, data);
			finish();
			}
		});
	}
	public Trip getTrip(Intent i) {

		try {
			System.out.println("in get trip");
			Bundle b = new Bundle();
			Trip recentTrip = (Trip) i.getParcelableExtra("newlyCreatedTrip");
			// b= i.getExtras();

			
			return recentTrip;
		} catch (Exception e) {
		
			e.printStackTrace();
		}

		return null;
	}


}

