package com.jared.asteroids;

import com.jared.asteroids.math.JVector;
import com.jared.asteroids.physics.Physics;
import com.jared.asteroids.physics.PhysicsObject;
import com.jared.asteroids.physics.PointMass;

public class Rocket implements PhysicsObject {
	JVector pos;
	float radius;
	Rocket(float x, float y, float r) {
		System.out.println(x + " " + y);
		pos = new JVector(x, y);
		radius = r;
		Asteroids.rockets.add(this);
		Physics.objects.add(this);
	}

	@Override
	public void update(float fixedDeltaTimeSeconds) {
		pos.x -= 100.0f * fixedDeltaTimeSeconds;
		if (pos.x < -50)
			Asteroids.rockets.remove(this);
	}
	public void draw() {
//		System.out.println("GO GO ROCKET RENDE?R " + pos.x + " " + pos.y);
		Renderer.canvas.drawCircle(pos.x, pos.y, radius, Renderer.paint);
	}
}
