package com.example.wuziqi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button btnshow;
	private Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainframe);
		btnshow = (Button) findViewById(R.id.bt_restart);    
        btnshow.setOnClickListener(new OnClickListener() {      
            public void onClick(View v) {      
		 Intent intent = new Intent();
         intent.setClass(MainActivity.this,MainFrame.class);
         MainActivity.this.startActivity(intent);
         }                              
        });
        btn = (Button) findViewById(R.id.btn1);    
        btn.setOnClickListener(new OnClickListener() {      
            public void onClick(View v) {      
            	System.exit(0);
         }                              
        });
	}
}
