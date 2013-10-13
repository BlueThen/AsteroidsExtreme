package com.jared.asteroids.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jared.asteroids.Asteroids;
import com.jared.asteroids.Renderer;
import com.jared.asteroids.math.JMath;
import com.jared.asteroids.math.JVector;

import android.graphics.Canvas;

public class PointMass {
	public JVector pos, lastPos, pinPos;
	public float m;
	public boolean pinned = false;
	public List<Link> links;
	Map<Link, Float[]> temporaryLinks;
	public boolean attachedToMouse;
	public JVector offset;
	public PointMass(float tx, float ty) {
		this(tx,ty,1);
	}
	public PointMass(float tx, float ty, float tm) {
		pos = new JVector(tx,ty);
		lastPos = pos.get();
		m = tm;
		links = new ArrayList<Link>();
		temporaryLinks = new HashMap<Link,Float[]>();
	}
	
	float totalTime = 0;
	public void integrate(float deltaTime) {
		if (!pinned) {
			totalTime += deltaTime;
			JVector vel = JVector.sub(pos, lastPos);
		//		vel.y += 98 * deltaTime;
			
//			vel.y += 10f * (float)Math.cos(totalTime * 3.5f) * deltaTime;
			vel.mult(0.96f);
			lastPos = pos.get();
			pos.add(vel);
		}
		for (int i = 0; i < links.size(); i++) {
			Link link = links.get(i);
			if (temporaryLinks.containsKey(link)) {
				if (totalTime - temporaryLinks.get(link)[0] > temporaryLinks.get(link)[1]) {
					// remove that link from EVERYWHERE
					links.remove(link);
					
				}
			}
		}
	}
	public void solveConstraints() {
		for (Link link : links)
			link.solve();
		
//		if (pos.x > Renderer.width-60)
//			pos.x = Renderer.width-60;
//		if (pos.x < 60)
//			pos.x = 60;
		if (pos.y > Renderer.height-60)
			pos.y = Renderer.height-60;
		if (pos.y < 60)
			pos.y = 60;
		
		if (pinned)
			pos = pinPos.get();
		
		if (attachedToMouse) {
			pos = JVector.add(Asteroids.cursor, offset);
		}
	}
	public void draw() {
//		System.out.println(pos.x + " " + pos.y);
		//Renderer.canvas.drawCircle(pos.x, pos.y, 2, Renderer.paint);
		for (Link link : links)
			link.draw();
	}
	public void pinTo(float x, float y) {
		pinPos = new JVector(x,y);
		pinned = true;
	}
	public void attachTo(PointMass b, float restingDistance, float stiffness, boolean drawable, float lifespan) {
		links.add(new Link(this, b, restingDistance, 1, drawable));
		temporaryLinks.put(links.get(links.size()-1), new Float[]{totalTime, lifespan});
	}
	public void attachTo(PointMass b, float restingDistance, float stiffness, boolean drawable) {
		links.add(new Link(this, b, restingDistance, 1, drawable));
	}
	public void attachTo(PointMass b, float restingDistance, float stiffness) {
		attachTo(b, restingDistance, stiffness, true);
	}
	public void attachTo(PointMass b, float restingDistance) {
		attachTo(b, restingDistance, 1, true);
	}
	void updateInteractions(float mouseInfluenceScalar) {
		// this is where our interaction comes in.
//		if (Asteroids.cursorDown) {
//			float distanceSquared = JMath.distPointToSegmentSquared(Asteroids.lastCursor.x,Asteroids.lastCursor.y,Asteroids.cursor.x,Asteroids.cursor.y,pos.x,pos.y);
//			if (distanceSquared != Float.NaN && distanceSquared < 25 * 25) {
//				lastPos = JVector.sub(pos, JVector.mult(JVector.sub(Asteroids.cursor, Asteroids.lastCursor), mouseInfluenceScalar));
//			}
//
//		}
	}
}
