package com.jared.asteroids.physics;

import com.jared.asteroids.Renderer;
import com.jared.asteroids.math.JVector;

// Link Constraint
public class Link {
	float r, stiffness;
	PointMass p1, p2;
	float im1, im2;
	float scalarP1, scalarP2;
	boolean drawThis = true;
	Link (PointMass which1, PointMass which2, float restingDistance, float stiff) {
		this(which1, which2, restingDistance, stiff, true);
	}
	Link (PointMass which1, PointMass which2, float restingDistance, float stiff, boolean drawable) {
		p1 = which1; 
		p2 = which2;

		r = restingDistance;
		stiffness = stiff;

		float im1 = 1 / p1.m; // inverse mass quantities
		float im2 = 1 / p2.m;
		scalarP1 = (im1 / (im1 + im2)) * stiffness;
		scalarP2 = (im2 / (im1 + im2)) * stiffness;
		
		drawThis = drawable;
	}
	void solve() {
		JVector delta = JVector.sub(p1.pos, p2.pos);  
		float d = (float)Math.sqrt(delta.x * delta.x + delta.y * delta.y);
		if (d != 0) {
			float difference = (r - d) / d;
			if (!p1.pinned && !p2.pinned) {
				p1.pos.add(JVector.mult(delta, scalarP1 * difference));
				p2.pos.sub(JVector.mult(delta, scalarP2 * difference));
			}
			else if (p1.pinned)
				p2.pos.sub(JVector.mult(delta, stiffness * difference));
			else if (p2.pinned)
				p1.pos.add(JVector.mult(delta, stiffness * difference));
			
		}
	}

	void draw() {
		if (drawThis)
			Renderer.canvas.drawLine(p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y, Renderer.paint);
	}
}
