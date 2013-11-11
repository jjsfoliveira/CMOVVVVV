package com.example.inspector_app;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;

import classJava.Validation;


import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import android.view.View;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//String host = "http://10.0.2.2:55611/BusWebService.svc";
	String host = "http://192.168.1.69:55611/BusWebService.svc";
	
	Spinner names;
	RelativeLayout progress;
	Button scan;
	RelativeLayout resultadLayout;
	TextView resultad;
	
	
	ArrayList<String> listName = new ArrayList<String>();
	ArrayList<Integer> listIdName = new ArrayList<Integer>();
	
	//ArrayList<Integer> listUsers = new ArrayList<Integer>();
	
	ArrayList<Validation> listValidation = new ArrayList<Validation>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		names = (Spinner) findViewById(R.id.names);
		progress = (RelativeLayout) findViewById(R.id.progress_layout);
		scan = (Button) findViewById(R.id.scan);
		resultadLayout = (RelativeLayout) findViewById(R.id.resultadLayout);
		resultad = (TextView) findViewById(R.id.resultad);
		
		resultadLayout.setVisibility(View.GONE);
		progress.setVisibility(10);
		scan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try {
					
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					startActivityForResult(intent, 0);
				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "ERROR:" + e, 1).show();

				}
				
			}
		});
		
		class GetNamesRunnable implements Runnable {
		  	  public void run() {
		  		HttpURLConnection con = null;
		  		String namesload = "Erro!";
				try {
					URL url = new URL(host + "/getSpots");
					con = (HttpURLConnection) url.openConnection();
					con.setReadTimeout(10000);
					con.setConnectTimeout(15000);
					con.setRequestMethod("GET");
					con.setDoInput(true);
	
					// Start the query
					con.connect();
	
					// Read results from the query
					BufferedReader reader = new BufferedReader(new InputStreamReader(
							con.getInputStream(), "UTF-8"));
					namesload = reader.readLine();
					reader.close();
					//Log.i("res", namesload);
				} catch (IOException e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
					builder.setMessage("Connection failed")
					       .setCancelable(false)
					       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				} finally {
					if (con != null)
						con.disconnect();
				}
				if (namesload != "Error")
			          try {
				            JSONArray jsonarray = new JSONArray(namesload);
				            for(int i = 0; i < jsonarray.length(); i++){
				            	//Log.i("msg", jsonarray.getString(i));
				            	listName.add(jsonarray.getJSONObject(i).getString("name"));
				            	listIdName.add(jsonarray.getJSONObject(i).getInt("Id"));
				            }
			            
			          }
			          catch (JSONException e) {
			        	  Log.i("erro", e.getLocalizedMessage());
			          }
				
		        runOnUiThread(new Runnable() {
		          public void run() {
		        	  createSpinner();
		          }
		        });
		  	  }
		  	}
			new Thread(new GetNamesRunnable()).start();
			
			
	}
	
	private void createSpinner(){
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
        (this, android.R.layout.simple_spinner_item,listName);
         
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
		names.setAdapter(dataAdapter);
		names.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				progress.setVisibility(10);
				resultadLayout.setVisibility(View.GONE);
				class GetUsersRunnable implements Runnable {
				  	  public void run() {
				  		HttpURLConnection con = null;
				  		String namesload = "Erro!";
						try {

							URL url = new URL(host + "/validation?spot="+listIdName.get(arg2));
							con = (HttpURLConnection) url.openConnection();
							con.setReadTimeout(10000);
							con.setConnectTimeout(15000);
							con.setRequestMethod("GET");
							con.setDoInput(true);
			
							// Start the query
							con.connect();
			
							// Read results from the query
							BufferedReader reader = new BufferedReader(new InputStreamReader(
									con.getInputStream(), "UTF-8"));
							namesload = reader.readLine();
							reader.close();
							//Log.i("res", namesload);
						} catch (IOException e) {
							/*AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							builder.setMessage("Connection failed")
							       .setCancelable(false)
							       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
							           public void onClick(DialogInterface dialog, int id) {
							           }
							       });
							AlertDialog alert = builder.create();
							alert.show();*/
						} finally {
							if (con != null)
								con.disconnect();
						}
						if (namesload != "Error")
					          try {
						            JSONArray jsonarray = new JSONArray(namesload);
						           // listUsers.clear();
						            listValidation.clear();
						            for(int i = 0; i < jsonarray.length(); i++){
						            	String ts = jsonarray.getJSONObject(i).getString("stamp");

					            	    String timeString = ts.substring(ts.indexOf("(") + 1, ts.indexOf(")"));
					            	    String[] timeSegments = timeString.split("\\+");
					            	    // May have to handle negative timezones
					            	    int timeZoneOffSet = Integer.valueOf(timeSegments[1]) * 36000; // (("0100" / 100) * 3600 * 1000)
					            	    long millis = Long.valueOf(timeSegments[0]);
					            	    Date date = new Date(millis + timeZoneOffSet);
					            	    Log.i("msg", date.toString());
					            	    
					            	    listValidation.add(new Validation(jsonarray.getJSONObject(i).getInt("type"), 
					            	    		jsonarray.getJSONObject(i).getInt("user"), date));
					            	    
						            }
					          }
					          catch (JSONException e) {
					        	  Log.i("erro", e.getLocalizedMessage());
					          }
						
				        runOnUiThread(new Runnable() {
				          public void run() {
				        	  progress.setVisibility(View.GONE);
				          }
				        });
				  	  }
				  	}
					new Thread(new GetUsersRunnable()).start();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
    	
		progress.setVisibility(View.GONE);

	}
	
	//In the same activity you’ll need the following to retrieve the results:
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {

			if (resultCode == RESULT_OK) {
				/*tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
				tvResult.setText(intent.getStringExtra("SCAN_RESULT"));*/
			
				String qrcUser = intent.getStringExtra("SCAN_RESULT");
				boolean find = false;
				resultadLayout.setVisibility(View.VISIBLE);
				Log.i("msg", listValidation.size()+"");
				for(int i = 0; i< listValidation.size(); i++){
					Log.i("msg", listValidation.get(i).user+"");
					if((listValidation.get(i).user+"").equals(qrcUser)){
						Date now = new Date();
						int t;
						if(listValidation.get(i).type == 1){
							t=15;
						}else if(listValidation.get(i).type == 2){
							t= 30;
						}else{
							t=60;
						}
						
						long diff = now.getTime() - listValidation.get(i).stamp.getTime();
						long diffMinutes = diff / (60 * 1000) % 60;
						
						find = true;
						if(diffMinutes <= t){
							resultadLayout.setBackgroundColor(Color.GREEN);
							resultad.setText("Validated!");
						}else{
							resultadLayout.setBackgroundColor(Color.RED);
							resultad.setText("Expired ("+(diffMinutes-t)+"min)!");
						}
					}
				}
				
				if(!find){
					resultadLayout.setBackgroundColor(Color.RED);
					resultad.setText("Not validated!");
				}
				
				Log.i("msg","ok" );
			} else if (resultCode == RESULT_CANCELED) {
				/*tvStatus.setText("Press a button to start a scan.");
				tvResult.setText("Scan cancelled.");*/
				resultadLayout.setVisibility(View.VISIBLE);
				resultadLayout.setBackgroundColor(Color.RED);
				resultad.setText("Canceled!");
				Log.i("msg","canceled" );
			}
		}
	}

}


