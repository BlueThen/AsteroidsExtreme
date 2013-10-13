package com.jared.asteroids.math;

public class JVector {
	public float x,y;
	public JVector(float tx, float ty) {
		x = tx; 
		y = ty;
	}
	public void add(JVector b) {
		x += b.x;
		y += b.y;
	}
	public void sub(JVector b) {
		x -= b.x;
		y -= b.y;
	}
	public void mult(float b) {
		x *= b;
		y *= b;
	}
	public float magSq() {
		return x * x + y * y;
	}
	public float mag() {
		return (float)Math.sqrt(magSq());
	}
	public void normalize() {
		float dist = mag();
		x /= dist;
		y /= dist;
	}
	public static JVector add(JVector a, JVector b) {
		JVector c = a.get();
		c.add(b);
		return c;
	}
	public static JVector sub(JVector a, JVector b) {
		JVector c = a.get();
		c.sub(b);
		return c;
	}
	public static JVector mult(JVector a, float b) {
		JVector c = a.get();
		c.mult(b);
		return c;
	}
	public void set(float tx, float ty) {
		x = tx;
		y = ty;
	}
	public JVector get() { // return a copy
		return new JVector(x,y);
	}
}
