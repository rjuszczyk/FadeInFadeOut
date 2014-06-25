package com.example.layouttest;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends Activity {
	
	MyView ev;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    ev = (MyView) findViewById(R.id.myView);
	    ev.setFocusable(false);
	}

	public boolean onTouchEvent(MotionEvent event) {
		return ev.onTouchEvent(event);
	}	
}
