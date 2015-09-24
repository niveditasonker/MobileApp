package com.nyu.cs9033.eta.controllers;

import java.util.ArrayList;

import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class triphistory extends Activity {
	ListView triplist;

	ArrayList<String> list = new ArrayList<String>();
	TripDatabaseHelper dbhelper = new TripDatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip_history);
		
		TripDatabaseHelper dbHelper = new TripDatabaseHelper(this);
		ArrayList<String> tripListed= new ArrayList<String>();
		tripListed=dbHelper.triplist();
		System.out.println(tripListed);
		
		Spinner newSpin= (Spinner)findViewById(R.id.spinner1);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tripListed);
	
		
				newSpin.setAdapter(adapter);

//		triplist = (ListView) findViewById(R.id.triplist);
//		triplist.setChoiceMode(0);
//
//		list = dbhelper.triplist();
//
//		triplist.setAdapter(new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_checked, list));

	}

}
