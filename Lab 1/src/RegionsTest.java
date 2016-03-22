import java.awt.*;
import javax.swing.*;

/**
 * Testing code for region finding in Lab 1.
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Winter 2014
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 */
public class RegionsTest extends DrawingFrame {

    /**
     * Test your RegionFinder by passing an image filename and a color to find.
     * @param filename
     * @param color
     */
    public RegionsTest(String filename, Color color) {
        super("Region finding test : "+filename, filename);

        // Do the region finding in the universe and recolor the image.
        Universe universe = new Universe(image); // image loaded by super constructor
        universe.setTrackingColor(color);
        universe.findRegions();
        universe.recolorRegions();
    }

    /**
     * Main method for the application
     * @param args		command-line arguments (ignored)
     */
    public static void main(String[] args) { 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegionsTest("pictures/smiley.png", new Color(0, 0, 0));
                new RegionsTest("pictures/baker.jpg", new Color(130, 100, 100));
            }
        });
    }

}
