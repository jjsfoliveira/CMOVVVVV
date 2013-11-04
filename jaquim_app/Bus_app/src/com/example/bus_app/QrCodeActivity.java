package com.example.bus_app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.provider.Settings.Secure;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class QrCodeActivity extends Activity {
	TextView bus_name;
	TextView state;
	TextView user;
	TextView type;
	
	Button scanBtn;
	RelativeLayout layoutResult;
	RelativeLayout progress;
	
	int spot;
	String[] r;
	String u = "Erro";
	
	
	//String host = "http://10.0.2.2:55611/BusWebService.svc";
	String host = "http://192.168.1.69:55611/BusWebService.svc";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code);
		
		MainActivity.progress.setVisibility(View.GONE);
		
		scanBtn = (Button)findViewById(R.id.scan);
		bus_name = (TextView) findViewById(R.id.bus_name);
		layoutResult = (RelativeLayout) findViewById(R.id.l_result);
		state = (TextView) findViewById(R.id.state);
		user = (TextView) findViewById(R.id.name_user);
		type = (TextView) findViewById(R.id.type);
		progress = (RelativeLayout) findViewById(R.id.progress_layout);
		
		progress.setVisibility(View.GONE);
		
		layoutResult.setVisibility(View.GONE);
		
		Intent i = getIntent();
		
		String name = i.getExtras().getString("bus_name");
		spot = Integer.parseInt(i.getExtras().getString("spot_id"));
		
		bus_name.setText(name);
		
		scanBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

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
		
	}
	
	
	//In the same activity you’ll need the following to retrieve the results:
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {

			if (resultCode == RESULT_OK) {
				
				/*tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
				tvResult.setText(intent.getStringExtra("SCAN_RESULT"));*/
				r = intent.getStringExtra("SCAN_RESULT").split("\\-");

				class GetValidateRunnable implements Runnable {
				  	  public void run() {
				  		//progress.setVisibility(View.VISIBLE);
				  		HttpURLConnection con = null;
				  		String load = "Error";
				  		
						try {
							URL url = new URL(host + "/activateTicket?user="+r[0]+"&tickettype="+r[1]+"&spot="+spot);
			
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
							load = reader.readLine();
							reader.close();

							u = load;
						} catch (IOException e) {
						} finally {
							if (con != null)
								con.disconnect();
						}
						
				        runOnUiThread(new Runnable() {
				          public void run() {
				        	  progress.setVisibility(View.GONE);
				        	  layoutResult.setVisibility(View.VISIBLE);
				        	  u = u.substring(1, u.length()-1);
				        	  if(!u.equals("erro")){
				        		  state.setText("Validated");
				        		  type.setText("T"+r[1]);
				        		  user.setText(u);
				        		  layoutResult.setBackgroundColor(Color.GREEN);
				        	  }else{
				        		  state.setText("Not validated");
				        		  type.setText("");
				        		  user.setText("");
				        		  layoutResult.setBackgroundColor(Color.RED);
				        	  }
				          }
				        });
				  	  }
				  	}
					new Thread(new GetValidateRunnable()).start();
				
				Log.i("msg","ok" );
			} else if (resultCode == RESULT_CANCELED) {
				/*tvStatus.setText("Press a button to start a scan.");
				tvResult.setText("Scan cancelled.");*/
				layoutResult.setVisibility(View.GONE);
			}
		}
	}

	
	

}
