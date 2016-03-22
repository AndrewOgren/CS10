import java.util.*;
import java.awt.*;

/**
 * An animated object flying across the scene in a fixed direction
 * Sample solution to Lab 1, Dartmouth CS 10, Winter 2015
 * @students Barry Yang and Andrew Ogren
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 */
public class FlierEC extends Agent {

    /* 
     * TODO: YOUR CODE HERE
     * 
     * The FlierEC class should have AT LEAST the following attributes 
     * in addition to what we've defined for you:
     *  - speed
     *  - direction of flight
     */

    private UniverseEC universe;         // the universe that a flier exists within
    private double dx, dy;

    public FlierEC(UniverseEC universe2) {
        super(0,0);
        
        this.universe = universe2;
        universe2.addFlierEC(this);
        
        //initial speed, 20 is chosen as a suitable constant
        dx = 20 * (Math.random());
		dy = 20 * (Math.random());
    }

    /**
     * Overrides Agent.move() to step by dx, dy
     */
    public void move() {
    	//increments the position
		x += dx;
		y += dy;
    }

    /**
     * Detect hitting the region (and restart)
     */
    public boolean checkWin() {
    	
    	//nested for loop, goes through each point in each reason
    	for (ArrayList<Point> region : universe.getRegions()) {
    		for (Point p : region) {
    			
    			//if the typecasted point on the region equals the typecasted point of the flier
    			if ((int) p.getX() == (int) x && (int) p.getY() == (int) y) { 
    				//System.out.println("you win"); //win message
    				toss();
    				return true;
    			}
    		}
    	}
    	return false;
    }

    /**
     * Detect exiting the window (and restart)
     */
    public  boolean checkLose() {
    	if (x < 0 || y < 0 || x > universe.getWidth() || y > universe.getHeight()) { //if it hits any border
    		//System.out.println("you lose"); //lose statement
    		toss();
    		return true;
    	}
    	return false;
    }

    /**
     * Puts the object at a random point on one of the four borders, 
     * flying in to the window at a random speed.
     */
    public void toss() {
    	
    	//decides which border to draw the point on, is divided by 4 though the if statement
    	double border = Math.random(); 
    	
    	if (border < .25) { //top border
    		x = (universe.getWidth() * Math.random()); //sets x to random point on the border
    		y = 0; //sets y to the top
    		dy = Math.abs(dy); //ensures that dy is positive
    	}
    	else if (border > .25 && border < .5) { //left border
    		x = 0; //sets x to left border
    		y = (universe.getHeight() * Math.random()); //sets y to random point on the border
    		dx = Math.abs(dx); //ensures that dx is positive
    	}
    	else if (border > .5 && border < .75) { //bottom border
    		x = (universe.getWidth() * Math.random()); //sets x to random spot on the border
    		y = universe.getHeight(); //sets y to bottom border
    		dy = -Math.abs(dy); //ensures that dy is negative
    	}
    	else { //right border
    		x = universe.getWidth(); //sets x to right side
    		y = (universe.getHeight() * Math.random()); //y is put to a spot on the border
    		dx = -Math.abs(dx); //ensures that dx is negative
    	}
    }
}
