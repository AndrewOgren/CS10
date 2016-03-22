import java.util.*;
import java.awt.*;

/**
 * An animated object flying across the scene in a fixed direction
 * Sample solution to Lab 1, Dartmouth CS 10, Winter 2015
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 */
public class Flier extends Agent {

    /* 
     * TODO: YOUR CODE HERE
     * 
     * The Flier class should have AT LEAST the following attributes 
     * in addition to what we've defined for you:
     *  - speed
     *  - direction of flight
     */

    private Universe universe;         // the universe that a flier exists within
    private double dx, dy;

    public Flier(Universe universe) {
        super(0,0);
        
        this.universe = universe;
        universe.addFlier(this);
        dx = 20 * (Math.random() - 0.5);
		dy = 20 * (Math.random() - 0.5);
    }

    /**
     * Overrides Agent.move() to step by dx, dy
     */
    public void move() {
		x += dx;
		y += dy;
    }

    /**
     * Detect hitting the region (and restart)
     */
    public void checkWin() {
    	for (ArrayList<Point> region : universe.getRegions()) {
    		for (Point p : region) {
    			if ((int) p.getX() == (int) x && (int) p.getY() == (int) y) {
    				System.out.println("you win");
    				toss();
    			}
    		}
    	}
    }

    /**
     * Detect exiting the window (and restart)
     */
    public void checkLose() {
    	if (x < 0 || y < 0 || x > universe.getWidth() || y > universe.getHeight()) {
    		System.out.println("you lose");
    		toss();
    	}
    }

    /**
     * Puts the object at a random point on one of the four borders, 
     * flying in to the window at a random speed.
     */
    public void toss() {
    	double border = Math.random();
    	if (border < .25) { //top border
    		x = (universe.getWidth() * Math.random());
    		y = 0;
    		dy = Math.abs(dy);
    	}
    	else if (border > .25 && border < .5) { //left border
    		x = 0;
    		y = (universe.getHeight() * Math.random());
    		dx = Math.abs(dx);
    	}
    	else if (border > .5 && border < .75) { //bottom border
    		x = (universe.getWidth() * Math.random());
    		y = universe.getHeight();
    		dy = -Math.abs(dy);
    	}
    	else { //right border
    		x = universe.getWidth();
    		y = (universe.getHeight() * Math.random());
    		dx = -Math.abs(dx);
    	}
    }
}
