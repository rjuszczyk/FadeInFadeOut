package com.example.layouttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.layouttest.myviews.MyBackground;

public class MyView extends SurfaceView implements SurfaceHolder.Callback{
	private Context ctx;
	public DrawingThread thread;
	
	boolean initializationCompleted = false;
	
	int width;
	int height;
    
    public MyView(Context context) {
        super(context);
        setZOrderOnTop(true);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        ctx = context;
    }
    
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setZOrderOnTop(true);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        ctx = context;
    }

    public MyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setZOrderOnTop(true);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        ctx = context;
    }
     
    
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	initializationCompleted = false;
    	
    	this.width = width;
    	this.height = height;
    	
	    thread.init(width, height);

	    initializationCompleted = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
	    thread = new DrawingThread(holder, ctx);
	    thread.setRunning(true);
	    thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
	  	boolean retry = true;
	  	thread.setRunning(false);
	  	while (retry) {
	  		try {
	  			thread.join();
	  			retry = false;
	  	    } catch (InterruptedException e) {
	  	    }
	  	}

    }
    
    public boolean onTouchEvent(MotionEvent event) {
		if (event != null) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if(thread.myBackground.in)
					thread.myBackground.setOutAnim();
				else
					thread.myBackground.setInAnim();
				
				return true;
			}
		}	
		return super.onTouchEvent(event);
		
	}
    
    public class DrawingThread extends Thread {
    	  
    	private boolean run = false;

    	private SurfaceHolder sh;
    	private Context ctx;
    	
    	MyBackground myBackground ;
    	
    	  
    	public DrawingThread(SurfaceHolder surfaceHolder, Context context) {
    	    sh = surfaceHolder;
    	    ctx = context;
    	    //initStates();
    	}
    	  
    	public void run() {
    	    while (run) {
    	    Canvas c = null;
    	    try {
    			c = sh.lockCanvas(null);
    			synchronized (sh) {
    			    if(run)
    			    	doDraw(c);
    			}
    			} finally {
    				if (c != null) {
    					sh.unlockCanvasAndPost(c);
    			    }
    			}
    	    }
    	}
    	    
    	public void setRunning(boolean b) { 
    		synchronized (sh) {
    			Log.i("debug","stop");
    			run = b;
    		}
    	}
    	
    	void init(int width, int height) {
    		Paint BLACK_PAINT = new Paint();
    		BLACK_PAINT.setColor(Color.BLACK);
    		
	    	myBackground = new MyBackground(ctx, false, width, height, BLACK_PAINT);
    	}
    	
    	long lastTime = System.currentTimeMillis();
    	private void doDraw(Canvas canvas) {
    		float deltaTime = System.currentTimeMillis() - lastTime;
            lastTime = System.currentTimeMillis();
    		
    		if(initializationCompleted == false) return;
    		if(canvas == null) return;
    		
			canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR);
			 
			myBackground.anim(deltaTime);
			myBackground.draw(canvas);
            
    		
    	}
    }
}