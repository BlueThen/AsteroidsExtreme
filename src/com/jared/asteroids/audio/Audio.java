package com.jared.asteroids.audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jared.asteroids.Asteroids;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

public class Audio {
	SoundPool soundPool;
	AssetManager assetManager;
	List<Integer> screams;
	List<Integer> whips;
	public Audio(SoundPool sound, AssetManager am) {
		soundPool = sound;
		assetManager = am;
		screams = new ArrayList<Integer>();
		whips = new ArrayList<Integer>();
	    try { // whip2.wav whip1.mp3 whip.wav whip3.wav whip4.mp3 whip5.wav WHIPCRAK.wav WhipCrack.mp3
	    	AssetFileDescriptor descriptor = assetManager
	    			.openFd("whip2.wav");
	    	whips.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("whip1.mp3");
	    	whips.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("whip.wav");
	    	whips.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("whip3.wav");
	    	whips.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("whip4.mp3");
	    	whips.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("whip5.wav");
	    	whips.add(soundPool.load(descriptor, 1));
//	    	descriptor = assetManager
//	    			.openFd("WHIPCRAK.wav");
//	    	whips.add(soundPool.load(descriptor, 1));
//	    	descriptor = assetManager
//	    			.openFd("WhipCrack.wav");
//	    	whips.add(soundPool.load(descriptor, 1));
	    	
	    	
	    	
	    	descriptor = assetManager
	    			.openFd("ascream.wav");
	    	screams.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("scream5.wav");
	    	long time = System.currentTimeMillis();
	    	while (System.currentTimeMillis() - time > 100) { }
	    	screams.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("aaa.wav");
	    	time = System.currentTimeMillis();
	    	while (System.currentTimeMillis() - time > 100) { }
	    	screams.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("CultScream2.wav");
	    	time = System.currentTimeMillis();
	    	while (System.currentTimeMillis() - time > 100) { }
	    	screams.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("fight.mp3");
	    	time = System.currentTimeMillis();
	    	while (System.currentTimeMillis() - time > 100) { }
	    	screams.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("ahhh.wav");
	    	time = System.currentTimeMillis();
	    	while (System.currentTimeMillis() - time > 100) { }
	    	screams.add(soundPool.load(descriptor, 1));
	    	descriptor = assetManager
	    			.openFd("2scream.wav");
	    	time = System.currentTimeMillis();
	    	while (System.currentTimeMillis() - time > 100) { }
	    	screams.add(soundPool.load(descriptor, 1));
//            screamID = soundPool.load(descriptor, 1);
        } catch (IOException e) {
            System.out.println("Couldn't load sound effect from asset, "
                    + e.getMessage());
        }
	}
	public void scream() {
		int scream = screams.get((int)(Math.random() * screams.size()));
		if (scream != -1)
			soundPool.play(scream, 0.45f, 0.45f, 0, 0, 1);
	}
	public void whip() {
		int whip = whips.get((int)(Math.random() * whips.size()));
		if (whip != -1)
			soundPool.play(whip, 0.5f, 0.5f, 0, 0, 1);
	}
}
