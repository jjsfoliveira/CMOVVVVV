package com.example.bus_app;

import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class QrCodeActivity extends Activity {
	TextView tvStatus;
	TextView tvResult;
	Button scanBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qr_code);

		tvStatus = (TextView) findViewById(R.id.tvStatus);
		tvResult = (TextView) findViewById(R.id.tvResult);
		
		scanBtn = (Button) findViewById(R.id.btnScan);
		scanBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					
					Intent intent = new Intent(
							"com.google.zxing.client.android.SCAN");
					intent.putExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
					Log.i("msg","poin1" );
					startActivityForResult(intent, 0);
					Log.i("msg","point2" );
				
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
				tvStatus.setText(intent.getStringExtra("SCAN_RESULT_FORMAT"));
				tvResult.setText(intent.getStringExtra("SCAN_RESULT"));
				Log.i("msg","ok" );
			} else if (resultCode == RESULT_CANCELED) {
				tvStatus.setText("Press a button to start a scan.");
				tvResult.setText("Scan cancelled.");
				Log.i("msg","canceled" );
			}
		}
	}

	
	

}
