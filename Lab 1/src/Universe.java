import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Universe for PS-1 Catch game.
 * Holds the fliers and the background image.
 * Also finds and holds the regions in the background image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Sample solution to Lab 1, Dartmouth CS 10, Winter 2015
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 */
public class Universe {
    private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
    private static final int minRegion = 50; 				// how many points in a region to be worth considering

    private BufferedImage image;                            // a reference to the background image for the universe
    private Color trackColor;                               // color of regions of interest (set by mouse press)

    private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
    // so the identified regions are in a list of lists of points

    private ArrayList<Flier> fliers;                        // all of the fliers

    /**
     * New universe with a background image and an empty list of fliers
     * @param image
     */
    public Universe(BufferedImage image) {
        this.image = image;		
        fliers = new ArrayList<Flier>();
    }

    /**
     * Set the image (from the webcam) that makes up the universe's background
     * @param image
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * Allow others to ask about the state of the trackColor in the universe
     * @return
     */
    public Color getTrackingColor() {
        return trackColor;
    }

    /**
     * Setting the color from an explicitly defined Color object 
     * as opposed to getting input from the player.
     * @param color
     */
    public void setTrackingColor(Color color) {
        trackColor = color;
    }

    /**
     * Allow others to ask about the size of the universe (width)
     * @return
     */
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Allow others to ask about the size of the universe (height)
     * @return
     */
    public int getHeight() {
        return image.getHeight();
    }

    /**
     * Accesses the currently-identified regions.
     * @return
     */
    public ArrayList<ArrayList<Point>> getRegions() {
        return regions;
    }

    /**
     * Set the universe's regions.
     * @return
     */
    public void setRegions(ArrayList<ArrayList<Point>> regions) {
        this.regions = regions;
    }

    /**
     * Adds the flier to the universe
     * @param f
     */
    public void addFlier(Flier f) {
        fliers.add(f);
    }

    /**
     *  Move the flier and detect catches and misses
     */
    public void moveFliers() {
    	for (Flier flier : fliers) {
        	flier.move(); //moves the flier
        	
        	//checks whether flier touches region or border
        	flier.checkWin();
        	flier.checkLose();
    	}
    }

    /**
     * Draw the fliers
     */
    public void drawFliers(Graphics g) {
        for (Flier flier : fliers) {
        	flier.draw(g);
        }
    }

    /**
     * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
     */
    public void findRegions() {
    	
    	regions = new ArrayList<ArrayList<Point>>(); 
    	boolean[][] visited = new boolean[image.getHeight()][image.getWidth()]; //keeps track of points already visited
    	Color c;
    	ArrayList<Point> frontier; //keeps track of points that need to be visited
    	Point thisPoint; //refers to point that the frontier is currently working with
    	
    	//iterates through entire image
        for (int y = 0; y < image.getHeight(); y++) {
        	for (int x = 0; x < image.getWidth(); x++) {
        		
        		c = new Color (image.getRGB (x, y)); //color at the point
        		
        		//check whether the trackColor matches the point color and whether point has been visited
        		if (colorMatch(c, trackColor) && visited[y][x] == false) {
        			visited[y][x] = true; //adds point to visited list
        			
        			//starts a new region and a new frontier, adds the first point to both ArrayLists
        			ArrayList<Point> region = new ArrayList<Point>();
            		frontier = new ArrayList<Point>();
            		region.add(new Point (x, y)); //initialize new region with the point itself
            		frontier.add(new Point(x, y));
            		
            		//while there are still pixels to be visited
            		while (frontier.size() > 0) {
            			
            			//removes the last point from the frontier, sets that to a point to be examined
            			thisPoint = frontier.remove(frontier.size() -1);
            			
            			//adds the point to the region and sets it as visited
            			region.add(thisPoint);
            			visited[thisPoint.y][thisPoint.x] = true;
            			
            			//nested for adapted from ImageProcessing.java, goes through neighbors of the pixel being examined
            			for (int ny = Math.max(0, thisPoint.y - 1); ny <= Math.min(image.getHeight() - 1, (int) thisPoint.y + 2); ny++) {
                			for (int nx = Math.max(0, thisPoint.x - 1); nx <= Math.min(image.getWidth() - 1, thisPoint.x + 2); nx++) {
                				
                				//sets color to the color of the neighboring pixel
                				c = new Color (image.getRGB (nx, ny));
                				
                				//if the neighboring pixel has not been visited and it matches the color, add to the frontier
                				if (visited[ny][nx] == false && colorMatch(c, trackColor)) {
                					frontier.add(new Point (nx, ny));
                				}
                			}
                		}
            		}
            		//if the region is worth keeping
            		if (region.size() >= minRegion) {
            			regions.add(region);
            		}
        		}
        	}
        }
    }

    /**
     * Tests whether the two colors are "similar enough" (your definition, subject to the static threshold).
     * @param c1
     * @param c2
     * @return
     */
    private static boolean colorMatch(Color c1, Color c2) {
    	//calculates Euclidean distance of the two colors
    	//adapted from track method in WebcamColorTracker
    	int red = (Math.abs(c1.getRed() - c2.getRed()));
    	int blue = (Math.abs(c1.getBlue() - c2.getBlue()));
    	int green = (Math.abs(c1.getGreen() - c2.getGreen()));
    	int d = (red + green + blue);
		if (d < maxColorDiff) { //if the difference between the colors is negligible (as defined by maxColorDiff)
			return true;
		}
		return false;
	}

    /**
     * Recolors image so that each region is a random uniform color, so we can see where they are
     */
    public void recolorRegions() {
    	//chooses a color for the regions
    	Color color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
        
    	//iterates through regions
    	for (ArrayList<Point> region : regions) {
    		//iterates through each point in the region
        	for (Point p : region) {
        		image.setRGB((int) p.getX(), (int) p.getY(), color.getRGB()); //sets the pixel of the region in the picture to the color
        	}
        }
    }
}
