package com.example.coolweather.activity;


import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.R;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.Utility;
import com.example.coolweather.util.httpUtil;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChoseAreaActivity extends Activity {

	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTRY=2;
	
	private ProgressDialog progressDialog;
	private TextView titleTextView;
	private ListView listView;
	public ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	
	private List<String> datalist=new ArrayList<String>();
	
	private List<Province> provincesList;
	
	private List<City> cityList;
	private List<Country> countryList;
	
	private Province selectedProvince;
	private City selectedCity;
	//private Country selectedCountry;
	
	private int currentLevel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chose_area);
		listView=(ListView) findViewById(R.id.list_view);
		titleTextView=(TextView) findViewById(R.id.title_text);
		adapter=new ArrayAdapter<String>(this, android.R .layout.simple_expandable_list_item_1, datalist);
		
		listView.setAdapter(adapter);
		coolWeatherDB=CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {

				if (currentLevel==LEVEL_PROVINCE) {
					selectedProvince=provincesList.get(index);
					queryCities();
				}
				else if (currentLevel==LEVEL_CITY) {
					selectedCity=cityList.get(index);
					queryCities();
				}
			}
		});
		queryProvince();
	}
	
	public void queryProvince(){
		provincesList=coolWeatherDB.loadProvince();
		if (provincesList.size()>0) {
			datalist.clear();
			for (Province province : provincesList) {
				datalist.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText("China");
			currentLevel=LEVEL_PROVINCE;
			
		}else {
			queryFromServer(null,"province");
		}
	}
	
	public void queryCities(){
		cityList=coolWeatherDB.loadCity(selectedProvince.getId());
		if (cityList.size()>0) {
			datalist.clear();
			for (City city : cityList) {
				datalist.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText(selectedCity.getCityName());
			currentLevel=LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(),"city");
		}
	}
	public void quertCountries() {
		countryList=coolWeatherDB.loadCountry(selectedCity.getId());
		if (countryList.size()>0) {
			datalist.clear();
			for (Country country : countryList) {
				datalist.add(country.getCountryName());
				
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleTextView.setText(selectedCity.getCityName());
			currentLevel=LEVEL_COUNTRY;
		} else {
			queryFromServer(selectedCity.getCityCode(),"country");
		}
	}
	
	
	private void queryFromServer(final String code,final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address="http://www.weather.com.cn/data/list3/city/"+code+".xml";
		}else {
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		httpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinsh(String response) {
				// TODO Auto-generated method stub
				boolean result=false;
				if ("province".equals(type)) {
					result=Utility.handleProvinceResponse(coolWeatherDB, response);
				} else if("city".equals(type)) {
					result=Utility.handleCityResponse(coolWeatherDB, response, selectedProvince.getId());
				}else if ("country".equals(type)) {
					result=Utility.handleCountryResponse(coolWeatherDB, response, selectedCity.getId());
				}
				if (result) {
					runOnUiThread( new Runnable() {
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvince();
							}else if ("city".equals(type)) {
								queryCities();
							}
							else if ("country".equals(type)) {
								quertCountries();
							}
						}

						
					});
				}
			}
			
			@Override
			public void onError(Exception e) {

				runOnUiThread(new Runnable() {
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChoseAreaActivity.this, "Load fail", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void showProgressDialog() {

		if (progressDialog==null) {
			progressDialog =new ProgressDialog(this);
			progressDialog.setMessage("Loading");
			progressDialog.setCancelable(false);
		}
		progressDialog.show();
	}
	private void closeProgressDialog() {

		if (progressDialog!=null) {
			progressDialog.dismiss();
		}
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (currentLevel==LEVEL_COUNTRY) {
			queryCities();
		} else if(currentLevel==LEVEL_CITY) {
			queryProvince();
		}else {
			finish();
		}
		
	}
}


















