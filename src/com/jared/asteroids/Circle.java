package com.jared.asteroids;

import com.jared.asteroids.physics.PointMass;

public class Circle {
	PointMass attached;
	float r;
	Circle(PointMass point, float radius) {
		attached = point;
		r = radius;
		Asteroids.circles.add(this);
	}
	void draw() {
		Renderer.canvas.drawCircle(attached.pos.x, attached.pos.y, r, Renderer.paint);
	}
}
