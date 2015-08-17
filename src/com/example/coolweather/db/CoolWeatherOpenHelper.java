package com.example.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	//table Province 
	private static final String CREATER_PROVINCE="create table Province("
			+"id integer primary key autoincrement, "
			+"province_name text, "
			+"province_code text)";
	//table City 
		private static final String CREATER_CITY="create table CITY("
				+"id integer primary key autoincrement, "
				+"city_name text, "
				+"city_code text, "
				+"province_id integer)";
		//table Country
		private static final String CREATER_COUNTRY="create table COUNTRY("
				+"id integer primary key autoincrement, "
				+"country_name text, "
				+"country_code text, "
				+"city_id integer)";
		
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATER_PROVINCE);
		db.execSQL(CREATER_CITY);
		db.execSQL(CREATER_COUNTRY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
