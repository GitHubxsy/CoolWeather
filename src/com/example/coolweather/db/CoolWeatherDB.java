package com.example.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {

	//database name
	public static final String DB_NAME="cool_weather";
	
	//databse version
	public static final int VERSION=1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	public CoolWeatherDB(Context context) {

		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}
	
	//get CoolWeather Instance
	public synchronized static CoolWeatherDB getInstance(Context context){
		if (coolWeatherDB==null) {
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	//save province
	public void saveProvince(Province province){
		if (province!=null) {
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	public List<Province> loadProvince() {
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province=new Province();
				province.setId(cursor.getColumnIndex("id"));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("Province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("Province_code")));
				list.add(province);
				
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	public void saveCity(City city) {
		if (city!=null) {
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("City_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("CITY", null, values);
			
		}
	}
	public List<City> loadCity(int provinceId) {
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("CITY", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("City_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
		
	}
	
	public void saveCountry(Country country) {
		if (country!=null) {
			ContentValues values=new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_code", country.getCountryCode());
			values.put("City_id", country.getCityId());
			db.insert("COUNTRY", null, values);
			
		}
	}
	public List<Country> loadCountry(int cityId) {
		List<Country> list=new ArrayList<Country>();
		Cursor cursor=db.query("country", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Country country=new Country();
				country.setCityId(cursor.getColumnIndex("id"));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCityId(cityId);
				list.add(country);
			} while (cursor.moveToNext());
		}
		return list;
		
	}
	
	
	
}





