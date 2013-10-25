package com.example.bus_app;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



import android.os.Bundle;
import android.provider.Settings.Secure;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	String host = "http://10.0.2.2:13680/BusWebService.svc";
	
	Button ok;
	EditText bus_name;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bus_name = (EditText)findViewById(R.id.name);
		ok = (Button)findViewById(R.id.ok_button);
		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ok_onClic();
			}
		});  
	}


	private int getRequest(String name) {
		HttpURLConnection con = null;
		String result = "Error";
		try {

			String androidId = Secure.getString(getContentResolver(),
					Secure.ANDROID_ID);
			URL url = new URL(host + "/spots?name=" + name + "&android_id="
					+ androidId);

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
			result = reader.readLine();
			reader.close();
			Log.i("res", result);
		} catch (IOException e) {
			Log.i("erro", e.getLocalizedMessage());
		} finally {
			if (con != null)
				con.disconnect();
		}
		return Integer.parseInt(result);
	}
	
	private void ok_onClic(){
		if(bus_name.getText().toString().length() > 0){
			int id = getRequest(bus_name.getText().toString());
			Intent intent = new Intent(this, QrCodeActivity.class);
			intent.putExtra("spot_id", id);
			startActivity(intent);
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Field not null!")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	

}
