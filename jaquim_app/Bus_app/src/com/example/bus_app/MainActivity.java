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
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {
	//String host = "http://10.0.2.2:55611/BusWebService.svc";
	String host = "http://192.168.1.69:55611/BusWebService.svc";
	Button ok;
	EditText bus_name;
	String name;
	static RelativeLayout progress;
	String id;
	
	boolean result;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		bus_name = (EditText)findViewById(R.id.name);
		ok = (Button)findViewById(R.id.ok_button);
		progress = (RelativeLayout)findViewById(R.id.progress_layout);
		

		progress.setVisibility(RelativeLayout.GONE);
		
		ok.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ok_onClic();
			}
		});  
	}


	private void callQrCode() {
		if(result){
			Intent intent = new Intent(this, QrCodeActivity.class);
			intent.putExtra("spot_id", id);
			intent.putExtra("bus_name", name);
			startActivity(intent);
		}else {
        	progress.setVisibility(View.GONE);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Connection failed")
			       .setCancelable(false)
			       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	private void ok_onClic(){
		if(bus_name.getText().toString().length() > 0){
			name = bus_name.getText().toString();
			
			progress.setVisibility(View.VISIBLE);
			class GetBusRunnable implements Runnable {
		  	  public void run() {
		  		HttpURLConnection con = null;
				result = true;
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
					id = reader.readLine();
					reader.close();
					Log.i("res", id);
				} catch (IOException e) {
					result = false;
				} finally {
					if (con != null)
						con.disconnect();
				}
		        runOnUiThread(new Runnable() {
		          public void run() {

		        	callQrCode();
		          }
		        });
		  	  }
		  	}
			new Thread(new GetBusRunnable()).start();
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Name can't be null!")
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
