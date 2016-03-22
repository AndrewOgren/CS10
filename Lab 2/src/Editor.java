import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Client-server graphical editor
 * Dartmouth CS 10, Winter 2015
 * Andrew Ogren & Barry Yang
 */

public class Editor extends JFrame {	
	private static String serverIP = "localhost";			// IP address of sketch server
															// "localhost" for your own machine;
															// or ask a friend for their IP address

	private static final int width = 800, height = 800;		// canvas size

	// GUI components
	private JComponent canvas, gui;
	JDialog colorDialog;
	JColorChooser colorChooser;
	JLabel colorL;

	// Current settings on GUI
	private boolean drawing = true;				// adding objects vs. moving/deleting/recoloring them
	private String shape = "ellipse"; 			// type of object to add
	private Color color = Color.black;			// current drawing color

	// Drawing state
	private Point point = null;					// initial mouse press for drawing; current position for moving
	private Shape current = null;				// the object currently being drawn (if one is)
	private int selected = -1;					// index of object (if any; -1=none) has been selected for deleting/recoloring
	private boolean dragged = false;			// keep track of whether object was actually moved
	
	// The sketch and communication
	private Sketch sketch;						// holds and handles all the drawn objects
	private EditorCommunicator comm;			// communication with the sketch server

	public Editor() {
        super("Graphical Editor");

        sketch = new Sketch();
        
        // Connect to server
        comm = new EditorCommunicator(serverIP, this);
        comm.start();

        // Helpers to create the canvas and GUI (buttons, etc.)
        setupCanvas();
        setupGUI();

        // Put the buttons and canvas together into the window
        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);
        cp.add(gui, BorderLayout.NORTH);

        // Usual initialization
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

	/**
	 * Creates a panel with all the buttons, etc.
	 */
	private void setupGUI() {
		// Toggle whether drawing or editing
		JToggleButton drawingB = new JToggleButton("drawing", drawing);
		drawingB.addActionListener(new AbstractAction("drawing") {
			public void actionPerformed(ActionEvent e) {
				drawing = !drawing;
				current = null;
			}
		});

		// Select type of shape
		String[] shapes = {"ellipse", "rectangle", "segment"};
		JComboBox shapeB = new JComboBox(shapes);
		shapeB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				shape = (String)((JComboBox)e.getSource()).getSelectedItem();
			}
		});

		// Select drawing/recoloring color
		// Following Oracle example
		JButton chooseColorB = new JButton("choose color");
		colorChooser = new JColorChooser();
		colorDialog = JColorChooser.createDialog(chooseColorB,
				"Pick a Color",
				true,  //modal
				colorChooser,
				new AbstractAction() { 
			public void actionPerformed(ActionEvent e) {
				color = colorChooser.getColor();
				colorL.setBackground(color); 
			} 
		}, //OK button
		null); //no CANCEL button handler
		chooseColorB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				colorDialog.setVisible(true);
			}
		});
		colorL = new JLabel();
		colorL.setBackground(Color.black);
		colorL.setOpaque(true);
		colorL.setBorder(BorderFactory.createLineBorder(Color.black));
		colorL.setPreferredSize(new Dimension(25, 25));

		// Delete object if it is selected
		JButton deleteB = new JButton("delete");
		deleteB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (selected != -1) { // if an object is actually selected 
					comm.delete(selected); //tell the editor communicator to send the delete msg to server communicator
					selected = -1; // make sure the object that was deleted is no longer selected
				}
			}
		});

		// Recolor object if it is selected
		JButton recolorB = new JButton("recolor");
		recolorB.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (selected != -1) { //if an object is actually selected
					color = colorChooser.getColor(); // get the color that was selected in the color chooser
					comm.recolor(selected, color); // tell the editor communicator to send the recolor msg for the selected shape and color
				}
			}
		});

		// Put all the stuff into a panel
		gui = new JPanel();
		gui.setLayout(new FlowLayout());
		gui.add(shapeB);
		gui.add(chooseColorB);
		gui.add(colorL);
		gui.add(new JSeparator(SwingConstants.VERTICAL));
		gui.add(drawingB);
		gui.add(deleteB);
		gui.add(recolorB);
	}

	private void setupCanvas() {
		canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				// Display the sketch
				// Also display the object currently being drawn in this editor (not yet part of the sketch)
				sketch.draw(g, selected);
				if (current != null && drawing) {
					current.draw(g); //draws the object
					
				}
			}
		};
		
		canvas.setPreferredSize(new Dimension(width, height));

		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				point = event.getPoint();
				// In drawing mode, start a new object;
				// in editing mode, set selected according to which object contains the pointnew Shape (point.x, point.y)
				if (drawing) { 
					//determines which shape needs to be added and creates that shape object and sets it as current
					if (shape.equals("ellipse")) {
						current = new Ellipse ((int) event.getX(), (int) event.getY(), (int) event.getX(), (int) event.getY(), color);
					}
					else if (shape.equals("rectangle")) {
						current = new Rectangle ((int) event.getX(), (int) event.getY(), (int) event.getX(), (int) event.getY(), color);
					}
					else if (shape.equals("segment")) {
						current = new Segment ((int) event.getX(), (int) event.getY(), (int) event.getX(), (int) event.getY(), color);
					}
				}
				else {
					selected = -1;
					if (sketch.container(event.getX(), event.getY()) != -1 && current == null) { //makes sure the point you click is on a shape
						selected = sketch.container(event.getX(), event.getY()); //sets current to the selected shape
						}
					}
				repaint();
			}

			public void mouseReleased(MouseEvent event) {
				// Pass the update (added object or moved object) on to the server
				if (drawing) {
					comm.add(current); //tells editor communicator to tell the server to add the current shape
					current = null;
				}
				else if (selected != -1 && dragged) { //makes sure that a shape is selected and that it is being dragged
					//Tells the editor communicator to send a message to move the selected shape to the current position
					comm.moveTo(selected, sketch.get(selected).x1, sketch.get(selected).y1); 
					dragged = false; //makes sure it is no longer dragging
				}
			}
		});		

		canvas.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event) {
				dragged = true;
				// In drawing mode, update the other corner of the object; -only send current when release the mouse
				// in editing mode, move the object by the difference between the current point and the previous one
				if (drawing) {
					//draws a new ellipse with one corner at the original point, and the other which depends on where the mouse goes
					current.setCorners((int) point.getX(), (int) point.getY(), (int) event.getX(), (int) event.getY());
				}
				else if (selected != -1 && sketch.container(event.getX(), event.getY()) != -1) {
					//moves the ellipse using difference of the current point and the previous one
					sketch.get(selected).moveBy((int) event.getX() - (int) point.getX(), (int) event.getY() - (int) point.getY());
					//updates the new point
					point = event.getPoint();
				}
				repaint();
			}
		});
	}

	/**
	 * Getter for the sketch instance variable
	 * @return
	 */
	public Sketch getSketch() {
		return sketch;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Editor();
			}
		});	
	}
}
