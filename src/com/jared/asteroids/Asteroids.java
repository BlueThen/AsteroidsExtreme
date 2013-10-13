package com.jared.asteroids;

import java.util.ArrayList;
import java.util.List;

import com.jared.asteroids.audio.Audio;
import com.jared.asteroids.math.JVector;
import com.jared.asteroids.physics.Physics;
import com.jared.asteroids.physics.PointMass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;

@SuppressLint("NewApi")
public class Asteroids extends Activity implements OnTouchListener {
	
	Asteroids asteroids;
	private SparseArray<JVector> activePointers;
	
	public static float worldSpeed = 70f;
	 
	public static JVector cursor = new JVector(0,0);
	public static JVector lastCursor = cursor.get();
	public static boolean cursorDown = false;
	int frame = 0;
    
    Physics physics;
    public static List<PointMass> pm = new ArrayList<PointMass>();
    public static List<Circle> circles = new ArrayList<Circle>();
    public static List<Ragdoll> ragdolls = new ArrayList<Ragdoll>();
    public static List<Rocket> rockets = new ArrayList<Rocket>();
    JellyFish jellyfish;
    
    public static int souls = 0;
    public static Audio audio;
    class RenderView extends View {
        public RenderView(Context context) {
            super(context);
            
            SoundPool soundpool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

            AssetManager assetManager = getAssets();
            
            
            audio = new Audio(soundpool, assetManager);
            
            for (int i = 0; i < 7; i++)
            	new Rocket(2000 + 1600f * (float)Math.random(), (float)Math.random() * 1200f, 60);
            
            
            activePointers = new SparseArray<JVector>();
            Renderer.paint = new Paint();
            physics = new Physics();
            jellyfish = new JellyFish(600,400);
            physics.addPO(jellyfish);
            for (int i = 0; i < 25; i++)
            	new Ragdoll(400 + 900 * (float)Math.random(), 1000f * (float)Math.random(), 60);
        }
        /* LOOP */
        protected void onDraw(Canvas canvas) {
        	Renderer.width = canvas.getWidth();
        	Renderer.height = canvas.getHeight();
        	Renderer.canvas = canvas;
        	frame++;

        	Paint paint = Renderer.paint;
        	
            canvas.drawRGB(255, 255, 255);            
           
            
            // update physics
            physics.update(pm);
            
            
            // update game logic
            while (ragdolls.size() < 40)
              ragdolls.add(new Ragdoll(Renderer.width + 40, Renderer.height * (float)Math.random(), 60f));
            while (rockets.size() < 10)
            	new Rocket(2000 + 1600f * (float)Math.random(), 50f + (float)Math.random() * 1000f, 60);
            // pointer logic
            boolean shootActive = false;
            boolean riseActive = false;
            for (int i = 0; i < activePointers.size(); i++) {
            	JVector currentPointer = activePointers.get(i);
            	if (currentPointer != null) {
	            	if (currentPointer.x < Renderer.width/4) {
	            		jellyfish.rise();
	            		riseActive = true;
//	            		jellyfish.head.pinPos = jellyfish.head.pos.get();
	            	}
	            	else {
	            		cursorDown = true;
	            		shootActive = true;
	            		lastCursor = cursor.get();
	            		cursor = currentPointer.get();
	            		if (!jellyfish.shooting)
	            			jellyfish.shootTentacle();
	            	}
            	}
            }
            if (!shootActive)
            	jellyfish.noShoot();
            if (!riseActive)
            	jellyfish.noRise();
            
            // render
            if (jellyfish.totalTime - jellyfish.lastHit < 3) {
		        Paint paint2 = new Paint(paint);
				paint2.setARGB(255 - (int) (255f/3f * (jellyfish.totalTime - jellyfish.lastHit)), 255, 0, 0);
				Renderer.canvas.drawCircle(jellyfish.head.pos.x, jellyfish.head.pos.y, 50, paint2);
            }
            for (PointMass point : pm)
            	point.draw();
            for (Circle circle : circles)
            	circle.draw();
            for (Rocket rock : rockets)
            	rock.draw();
            
            Renderer.paint.setStrokeWidth(3);
            Renderer.paint.setTextSize(50);
            canvas.drawText("Souls: " + souls, 50, 75, Renderer.paint);
            
            
            invalidate();
        }
        
    }
    public static void addPM(PointMass point) {
    	pm.add(point);
    }
   

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RenderView view = new RenderView(this);
        view.setOnTouchListener(this);
        setContentView(view);
        asteroids = this;
    }


	@Override
	public boolean onTouch(View v, MotionEvent event) {

    	// get pointer index from the event object
    	int pointerIndex = event.getActionIndex();

    	// get pointer ID
    	int pointerId = event.getPointerId(pointerIndex);

    	// get masked (not specific to a pointer) action
    	int maskedAction = event.getActionMasked();

    	switch (maskedAction) {

    	case MotionEvent.ACTION_DOWN:
    	case MotionEvent.ACTION_POINTER_DOWN: {
    		// We have a new pointer. Lets add it to the list of pointers

//    		PointF f = new PointF();
//    		f.x = event.getX(pointerIndex);
//    		f.y = event.getY(pointerIndex);
    		activePointers.put(pointerId, new JVector(event.getX(pointerIndex), event.getY(pointerIndex)));
    		break;
    	}
    	case MotionEvent.ACTION_MOVE: { // a pointer was moved
    		for (int size = event.getPointerCount(), i = 0; i < size; i++) {
    			JVector point = activePointers.get(event.getPointerId(i));
    			if (point != null) {
    				point.x = event.getX(i);
    				point.y = event.getY(i);
    			}
    		}
    		break;
    	}
    	case MotionEvent.ACTION_UP:
    	case MotionEvent.ACTION_POINTER_UP:
    	case MotionEvent.ACTION_CANCEL: {
    		activePointers.remove(pointerId);
    		break;
    	}
    	}
//    	invalidate();

    	return true;
	}
}