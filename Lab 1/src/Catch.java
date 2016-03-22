import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Webcam-based catch game
 * Sample solution to Lab 1, Dartmouth CS 10, Winter 2015
 * 
 * @students Barry Yang and Andrew Ogren
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 */
public class Catch extends Webcam {
    private Universe universe;			       // handles finding regions
    private static final int numFliers = 5;    // the # of fliers to create

    /**
     * Overrides Webcam method to do the region finding the flier stuff.
     */
    public void processImage() {
        if (universe != null) {
            // Set the image in universe
        	universe.setImage(image);

            // Detect regions and recolor
            universe.findRegions();
            universe.recolorRegions();

            // Move the fliers and detect catches and misses
            universe.moveFliers();
        }
    }

    /**
     * Overrides the Webcam method to also draw the flier.
     */
    public void draw(Graphics g) {
        super.draw(g);

        // Draw fliers
        if(universe != null) {
            universe.drawFliers(g);
        }
    }

    /**
     * Overrides the Webcam method to create the universe, set the track 
     * color and start the flier.
     */
    public void handleMousePress(MouseEvent event) {
        // Create universe
    	universe = new Universe(image);

        // Set tracking color
    	Color color =  new Color (image.getRGB (event.getX(), event.getY())); //sets color to color of the chosen pixel
        universe.setTrackingColor(color);

        // Create fliers and start them flying
        for (int i = 0; i < numFliers; i++) {
        	Flier f = new Flier(universe);
        	universe.addFlier(f);
        	f.toss(); //tosses the new flier to the window
        }
    }

    /**
     * Main method for the application
     * @param args      command-line arguments (ignored)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Catch();
            }
        });
    }
}
