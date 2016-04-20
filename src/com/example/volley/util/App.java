package com.example.volley.util;

import android.app.Application;
import android.content.Context;

public class App extends Application{
	private static Context sContext;//”¶∏√ «super Context

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sContext = getApplicationContext();		
	}
	
	public static Context getContext() {
		return sContext;
	}
		
}
