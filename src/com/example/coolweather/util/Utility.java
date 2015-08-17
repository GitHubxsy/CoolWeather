package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;

public class Utility {

	public synchronized static boolean handleProvinceResponse(CoolWeatherDB coolWeatherDB,String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvince=response.split(",");
			if (allProvince!=null&&allProvince.length>0) {
				for (String p : allProvince) {
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherDB,String response,int provinceId) {
		
		if (!TextUtils.isEmpty(response)) {
			String[] allCity=response.split(",");
			if (allCity!=null&&allCity.length>0) {
				for (String c : allCity) {
					String[] array=c.split("//|");
					City city =new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					coolWeatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
public synchronized static boolean handleCountryResponse(CoolWeatherDB coolWeatherDB,String response,int cityId) {
		
		if (!TextUtils.isEmpty(response)) {
			String[] allCoutry=response.split(",");
			if (allCoutry!=null&&allCoutry.length>0) {
				for (String c : allCoutry) {
					String[] array=c.split("//|");
					Country country =new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					coolWeatherDB.saveCountry(country);
				}
				return true;
			}
		}
		return false;
	}
	
	
}
