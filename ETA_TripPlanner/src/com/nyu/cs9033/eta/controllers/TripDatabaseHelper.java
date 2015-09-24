package com.nyu.cs9033.eta.controllers;

import java.util.ArrayList;

import com.nyu.cs9033.eta.models.Trip;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TripDatabaseHelper extends SQLiteOpenHelper {

	public static String name = "TripDatbase";
	public static int version = 1;
	public static String okCheck="OK";
	public static String id = " ";
	public TripDatabaseHelper(Context context) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("Create table tripTable(Location text, Tripname text, Friends text, Date text, Time text, trip_id text, trip_status text)");
		System.out.println("In create");
	}

	public ArrayList<String> triplist() {
		ArrayList<String> List = new ArrayList<String>();
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			Cursor cur = db.rawQuery("Select Tripname from tripTable", null);
			System.out.println("i m in");
			if (cur.getCount() != 0) {
				while (cur.moveToNext()) {
					List.add(cur.getString(0));
					
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return List;

	}
	public void onDelete(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("tripTable", null, null);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}	

	public void createTrip(String Location, String Tripname,
			ArrayList<String> Friends, String Date, String Time, String ID) {
		SQLiteDatabase dataBase = this.getWritableDatabase();
		System.out.println("Insert into tripTable values('" + Location + "','"
				+ Tripname + "','" + Friends + "','" + Date + "','" + Time
				+ "','" + ID + "')");
		dataBase.execSQL("Insert into tripTable values('" + Location + "','"
				+ Tripname + "','" + Friends + "','" + Date + "','" + Time
				+ "','" + ID + "','notstarted')");
		System.out.println(" "+ Location +" "+Tripname+ " "+Friends+" "+Date+" "+Time);

	}
	public Trip tripDetail(String tripName){
		Trip T = null;
		String TripLoc="";
		String tripDate="";
		String tripTime="";
		String tripfriends="";
		ArrayList<String> friendList = new ArrayList <String> ();
		SQLiteDatabase db = this.getReadableDatabase();
		try
		{
			Cursor abc = db.rawQuery("Select Location, Tripname , Friends , Date , Time from tripTable where Tripname=?", new String []{tripName});
		System.out.println("Insert");
			if(abc.getCount()!=0)
		{
			abc.moveToFirst();
			TripLoc=abc.getString(0);
		System.out.println("in db details...."+tripName);
		System.out.println("in db details...."+abc.getString(3));
		System.out.println("in db details...."+abc.getString(4));
		System.out.println("in db details...."+abc.getString(2));
			
			tripDate=abc.getString(3);
			tripTime=abc.getString(4);
			tripfriends=abc.getString(2);
			T= new Trip(tripName, TripLoc, tripDate, tripTime,okCheck, tripfriends);	
			
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return T;
		
		}
	
	public String getID (String Name){
		try{
		SQLiteDatabase SQLdb = this.getReadableDatabase();
		Cursor ID = SQLdb.rawQuery("Select  trip_id from triptable where Tripname ='"+Name+"'",null);
		if(ID.getCount()!=0){
			ID.moveToFirst();
			id = ID.getString(0);
			SQLdb.close();
			}
				
	} catch(Exception e){
		e.printStackTrace();
	}
		return id;
}
	public int updateTripStatus (String id){
		int res =0;
		try{
		SQLiteDatabase SQLdb = this.getReadableDatabase();
		ContentValues values = new ContentValues();
		values.put("trip_status", "Started");
		
		res = SQLdb.update("triptable", values, "trip_id=?",new String[] {id});
		//Cursor ID = SQLdb.rawQuery("update triptable set trip_status ='started' where trip_id="+id,null);
		Log.d("in update function result database ", "result is>>>"+res);
		Cursor ID1 = SQLdb.rawQuery("Select  trip_status from triptable where trip_id ='"+id+"'",null);
		if(ID1.getCount()!=0){
			ID1.moveToFirst();
			String status = ID1.getString(0);
			System.out.println("status after update>>>"+status);
			}
				
	} catch(Exception e){
		e.printStackTrace();
	}
		return res;
}
}
