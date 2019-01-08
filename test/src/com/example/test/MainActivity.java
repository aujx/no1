package com.example.test;

import android.app.Activity;    
import android.os.Bundle;    
import android.view.View;    
import android.widget.Toast;   
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void myclick1(View source)    
    {    
        Toast.makeText(getApplicationContext(), "µÇÂ¼ÖÐ", Toast.LENGTH_SHORT).show();    
    }   
    public void myclick2(View source)    
    {    
        Toast.makeText(getApplicationContext(), "sorry£¬³ö´íÁË£¡£¡", Toast.LENGTH_SHORT).show();    
    }   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
