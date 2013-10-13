package com.jared.asteroids.math;

public class JMath {
	public static float distPointToSegmentSquared (float lineX1, float lineY1, float lineX2, float lineY2, float pointX, float pointY) {
    	float vx = lineX1 - pointX;
    	float vy = lineY1 - pointY;
    	float ux = lineX2 - lineX1;
    	float uy = lineY2 - lineY1;

    	float len = ux*ux + uy*uy;
    	float det = (-vx * ux) + (-vy * uy);
    	if ((det < 0) || (det > len)) {
    		ux = lineX2 - pointX;
    		uy = lineY2 - pointY;
    		return Math.min(vx*vx+vy*vy, ux*ux+uy*uy);
    	}

    	det = ux*vy - uy*vx;
    	return (det*det) / len;
    }
}
