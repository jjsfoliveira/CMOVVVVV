package com.example.bus_app;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



public class QrCodeActivity extends Activity {
	TextView bus_name;
	TextView state;
	TextView user;
	TextView type;
	
	Button scanBtn;
	LinearLayout layoutResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code);
		
		MainActivity.progress.setVisibility(View.GONE);
		
		scanBtn = (Button)findViewById(R.id.scan);
		bus_name = (TextView) findViewById(R.id.bus_name);
		layoutResult = (LinearLayout) findViewById(R.id.layout_result);
		state = (TextView) findViewById(R.id.state);
		user = (TextView) findViewById(R.id.name_user);
		type = (TextView) findViewById(R.id.type);
		
		layoutResult.setVisibility(View.GONE);
		
		Intent i = getIntent();
		
		String name = i.getExtras().getString("bus_name");
		String id_spot = i.getExtras().getString("spot_id");
		
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
				layoutResult.setVisibility(View.VISIBLE);
				/*tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
				tvResult.setText(intent.getStringExtra("SCAN_RESULT"));*/
				Log.i("msg","ok" );
			} else if (resultCode == RESULT_CANCELED) {
				/*tvStatus.setText("Press a button to start a scan.");
				tvResult.setText("Scan cancelled.");*/
				Log.i("msg","canceled" );
			}
		}
	}

	
	

}
