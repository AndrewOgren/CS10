import java.awt.*;

/**
 * Representation of a message for updating a sketch
 * Dartmouth CS 10, Winter 2015
 * Andrew Ogren & Barry Yang
 */
public class Message {
	private String msg;
	
	/**
	 * Initializes it from a string representation used for communication
	 * @param msg
	 */
	//string that 
	public Message(String msg) {
		this.msg = msg;
	}

	/**
	 * Updates the sketch according to the message
	 * This may result in a modification of the message to be passed on
	 */
	public void update(Sketch sketch) {
		// creates a string array with each element as a part of the message that is separated by a space
		String[] describe = msg.split(" ");
		
		if (describe[0].equals("delete")) { // the element in the 0th position is the string "delete"
			sketch.doDelete(Integer.parseInt(describe[1])); //uses sketch's delete method
		}
		
		if (describe[0].equals("recolor")) { // the element in the 0th position is the string "recolor"
			Color c = new Color (Integer.parseInt(describe[2])); //gets the chosen color
			sketch.doRecolor(Integer.parseInt(describe[1]), c); //uses sketch's recolor method
		}
		
		if (describe[0].equals("moveTo")) { // the element in the 0th position is the string "moveTo"
			//uses sketch's doMoveTo method at the specified points
			sketch.doMoveTo(Integer.parseInt(describe[1]), Integer.parseInt(describe[2]), Integer.parseInt(describe[3]));
		}
		
		if (describe[0].equals("add")) { //the element in the 0th position is the string "add"
			Color c = new Color (Integer.parseInt(describe[6]));
			Shape shape;
			// adds the appropriate shape
			if (describe[1].equals("rectangle")) {
				shape = new Rectangle(Integer.parseInt(describe[2]), Integer.parseInt(describe[3]), Integer.parseInt(describe[4]), Integer.parseInt(describe[5]), c);
				sketch.doAddEnd(shape);
			}
			else if (describe[1].equals("ellipse")) {
				shape = new Ellipse(Integer.parseInt(describe[2]), Integer.parseInt(describe[3]), Integer.parseInt(describe[4]), Integer.parseInt(describe[5]), c);
				sketch.doAddEnd(shape);
			}
			else if (describe[1].equals("segment")) {
				shape = new Segment(Integer.parseInt(describe[2]), Integer.parseInt(describe[3]), Integer.parseInt(describe[4]), Integer.parseInt(describe[5]), c);
				sketch.doAddEnd(shape);
			}
		}
	}

	/**
	 * Converts to a string representation for communication
	 */
	public String toString() {
		return msg; //returns the msg when called (already a string)
	}
}
