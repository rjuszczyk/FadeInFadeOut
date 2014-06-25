package com.example.layouttest.myviews;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class MyBackground {
	
	int width;
	int height;
	int w;
	int h;
	int size;
	
	private float lerp(float a, float b, float t) {
		return b*t + a*(1.0f-t);
	}
	
	Random random = new Random();
	private	ArrayList<Float> xs; //current rectangles positions
	private	ArrayList<Float> ys;
	
	private	ArrayList<Float> target_xs; // positions which are current destination of rectangles
	private	ArrayList<Float> target_ys;
	
	private	ArrayList<Float> speeds;
	private	int count;
	public boolean in;
	
	float animationProgress = 1000;
	Paint paint;
	public MyBackground(Context c, boolean _in, int _width, int _height, Paint _paint, int _size) {
		this(c, _in, _width, _height, _size);
		paint = _paint;
	}
	
	public MyBackground(Context c, boolean _in, int _width, int _height, Paint _paint) {
		this(c, _in, _width, _height);
		paint = _paint;
	}
	

	public MyBackground(Context c, boolean _in, int _width, int _height) {
		this(c, _in, _width,_height, _height < _width ? _width/45 : _height/45);
		
		
	}
	
	public MyBackground(Context c, boolean _in, int _width, int _height, int _size) {
		in = _in;
		width = _width;
		height = _height;
		
    	size = _size;   	
    	w = width / size;
    	if(width % size != 0)
    		w++;
    	
    	h = height / size;
    	if(height % size != 0)
    		h++;
    	
    	count = h*w;
    	
    	animationProgress=0;
		
    	xs = new ArrayList<Float>(count);
    	ys = new ArrayList<Float>(count);
    	
    	target_xs = new ArrayList<Float>(count);
    	target_ys = new ArrayList<Float>(count);
    	speeds = new ArrayList<Float>(count);
		
		if(in) {
    		for(int i = 0 ; i < count; i++) {
    			speeds.add(random.nextFloat()*400+100);

    			int yy = i / w;
    			int xx = i - yy*w;
    			target_xs.add((float) (xx*size)) ;
    			target_ys.add((float) (yy*size)) ;
    			xs.add((float) xx*size);
    			ys.add((float) yy*size);
    		}
		} else {
			for(int i = 0 ; i < count; i++) {
    			speeds.add(random.nextFloat()*400+100);
    			
    			float t = random.nextFloat();
    			t*=2*3.15;
    			float r=width;
    			if(height>width)
    				r=height;
    			target_xs.add((float) ((r+15)*Math.sin(t))+width/2) ;
    			target_ys.add((float) ((r+15)*Math.cos(t))+height/2) ;
    			xs.add((float) ((r+15)*Math.sin(t))+width/2);
    			ys.add((float) ((r+15)*Math.cos(t))+height/2);
    		}
		}
	}
	
	public void setInAnim() {
		if(in) return;
		in = true;
		for(int i = 0 ; i < count; i++) {
			int yy = i / w;
			int xx = i - yy*w;
			target_xs.set(i,(float) xx*size);
			target_ys.set(i,(float) yy*size);
		}	
		animationProgress=0;
	}
	public void setOutAnim() {
		if(!in) return;
		in = false;
		
		animationProgress=0;
		switch(random.nextInt(7)) {
		case 0:
    		for(int i = 0 ; i < count; i++) {
    			float t = random.nextFloat();
    			t*=2*3.15;
    			float r=width;
    			if(height>width)
    				r=height;
    			
    			target_xs.set(i,(float) ((r+15)*Math.sin(t))+width/2);
    			target_ys.set(i,(float) ((r+15)*Math.cos(t))+height/2);
    		}
    		break;
		case 1: 
			for(int i = 0 ; i < count; i++) {
    			target_xs.set(i,(float) (-100+random.nextInt(100)));
    			target_ys.set(i,(float) (-100+random.nextInt(50)));
    		}
			break;
		case 2:
			for(int i = 0 ; i < count; i++) {
    			target_xs.set(i,(float) (width+50+random.nextInt(100)));
    			target_ys.set(i,(float) (-100+random.nextInt(50)));
    		}
			break;
		case 3:
			for(int i = 0 ; i < count; i++) {
    			target_xs.set(i,(float) (-100+random.nextInt(100)));
    			target_ys.set(i,(float) (height+50+random.nextInt(50)));
    		}
			break;
		case 4:
			for(int i = 0 ; i < count; i++) {
    			target_xs.set(i,(float) (width+ 50+random.nextInt(100)));
    			target_ys.set(i,(float) (height+ 50+random.nextInt(50)));
    		}
			break;
			
		default:
			float start = random.nextFloat();
			float end = random.nextFloat();
			if(start > end) {
				float temp = start;
				start = end;
				end = temp;
			}
			for(int i = 0 ; i < count; i++) {
    			float t = random.nextFloat();
    			
    			t = start + (start-end)*t;
    			t*=2*3.15;
    			float r=width;
    			if(height>width)
    				r=height;
    			
    			target_xs.set(i,(float) ((r+10)*Math.sin(t))+width/2);
    			target_ys.set(i,(float) ((r+10)*Math.cos(t))+height/2);
    		}
		
		}
	}
	

	public void anim(float deltaTime) {
		if(animationProgress < 990) {
			for(int i = 0 ; i < count; i++) {
				xs.set(i, lerp(xs.get(i), target_xs.get(i), deltaTime/speeds.get(i)) );
				ys.set(i, lerp(ys.get(i), target_ys.get(i), deltaTime/speeds.get(i)) );
			}
			animationProgress = lerp(animationProgress, 1000, deltaTime/500.f);
		}
	}
	public void draw(Canvas canvas) {
		for(int i = 0 ; i < count; i++) {
			float x = xs.get(i);
			float y = ys.get(i);
            canvas.drawRect(x-size, y-size, x+size, y+size,paint);
		}
	}
}
