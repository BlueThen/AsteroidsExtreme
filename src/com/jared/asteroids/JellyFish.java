package com.jared.asteroids;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Paint;

import com.jared.asteroids.math.JMath;
import com.jared.asteroids.math.JVector;
import com.jared.asteroids.physics.PhysicsObject;
import com.jared.asteroids.physics.PointMass;

public class JellyFish implements PhysicsObject {
	List<PointMass> tentacles;
	List<List<PointMass>> legs;

	PointMass head;
	boolean shooting = false;
	boolean rising = false;

	int legNum = 0;
	float headX;
	float headYVel = 0;
	
    float totalTime = 0;
    float lastGrab;
    
    public float lastHit = -9990;
	JellyFish(float x, float y) {
		head = new PointMass(x,y,24);
		Asteroids.addPM(head);
		headX = x;
		//head.pinTo(x, y);
		tentacles = new ArrayList<PointMass>();
		legs = new ArrayList<List<PointMass>>();
		for (int leg = 0; leg < 10; leg++) {
			legs.add(new ArrayList<PointMass>());
			PointMass main = new PointMass(head.pos.x + 0.5f, head.pos.y + 0.5f, 1);
			main.attachTo(head, 80);
			tentacles.add(main);
			legs.get(legs.size()-1).add(main);
			for (int joints = 0; joints < 10; joints++) {
				
				PointMass last = tentacles.get(tentacles.size()-1);
				PointMass point = new PointMass(last.pos.x + 5f * (float)Math.random(), last.pos.y + 5f * (float)Math.random(), 1);
				point.attachTo(last, 60);
				legs.get(legs.size()-1).add(point);
				tentacles.add(point);
//				Asteroids.audio.scream();
			}
		}
		for (PointMass point : tentacles)
			Asteroids.addPM(point);
	}
	public void update(float deltaTime) {
		totalTime += deltaTime;
		// check against rockets
		if (totalTime - lastHit > 3) {
			for (Rocket r : Asteroids.rockets) {
				JVector delta = JVector.sub(head.pos, r.pos);
				if (delta.magSq() < r.radius * r.radius) {
					Asteroids.souls -= 5;
					for (int i = 0; i < 5; i++) {
						Ragdoll body = new Ragdoll(head.pos.x, head.pos.y, 60);
						body.scatter();
						body.die();
						lastHit = totalTime;
					}
				}
			}
		}
		for (PointMass point : tentacles) {
			JVector delta = JVector.sub(point.pos, head.pos);
			
			float dist = delta.mag();
			float randAngle = (float)(Math.PI * 2f * Math.random());
			
			delta.normalize();
			delta.add(new JVector(75f * (float)Math.cos(randAngle), 75f * (float)Math.sin(randAngle)));
			
			point.lastPos.sub(JVector.mult(delta, 1f/(dist + 60f) * 10f));
			point.lastPos.x += (10f + 5f * (float)Math.cos(totalTime * 2f)) * deltaTime;
		}
		if (shooting) {
			List<PointMass> currentLeg = legs.get(legNum);
			PointMass point = currentLeg.get(currentLeg.size() - 1);
			JVector delta = JVector.sub(Asteroids.cursor, point.pos);
//			float distance = delta.mag();
//			delta.normalize();
			point.pos.add(JVector.mult(delta, 0.15f));
			if (JVector.mult(delta, 0.15f).magSq() > 100*100)
				Asteroids.audio.whip();
//			Renderer.canvas.drawCircle(point.pos.x, point.pos.y, 60, Renderer.paint);
			
			// check against other ragdolls
			for (Ragdoll r : Asteroids.ragdolls) {
				if (!r.dead) {
					JVector pos = r.getAvgPos();
					float radius = 50;
					JVector diff = JVector.sub(pos, point.pos);
					if (diff.magSq() < 55*55) {
						float dist = diff.mag();
						if (dist < radius) { // collision!
							Asteroids.souls++;
							Asteroids.audio.scream();
							
							point.attachTo(r.head, 5, 1, true, 1);
							delta.normalize();
							point.pos.sub(JVector.mult(delta, 6000f));
							shooting = false; 
							
							r.die();
							shooting = false;
						}
					}
				}
				
			}
		}
		head.pos.x = headX;
		if (rising) {
			if (headYVel < 0)
				headYVel = 0;
			if (headYVel < 300)
				headYVel += 20;
		}
		else {
			if (headYVel > 0)
				headYVel = 0;
			if (headYVel > -250)
				headYVel -= 2;
		}
		head.lastPos.y += headYVel * deltaTime;
		
	}
	public void shootTentacle() {
		// use a random leg
		if (!shooting) {
			if (totalTime - lastGrab > 0.5f) {
				lastGrab = totalTime;
				shooting = true;
				int priorLeg = legNum;
				while (legNum == priorLeg)
					legNum = (int) (Math.random() * legs.size());
			}
		}
		else {
			noShoot();
		}
	}
	public void noShoot() {
		shooting = false;
	}
	public void rise() {
		rising = true;
	}
	public void noRise() {
		rising = false;
	}
}
