import java.awt.Color;
import java.io.*;
import java.net.Socket;

/**
 * Handles communication to/from the server for the editor
 * Dartmouth CS 10, Winter 2015
 * Andrew Ogren & Barry Yang
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for
	
	//sketch is here

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			String line;
			while ((line = in.readLine()) != null) { //while there are still messages being sent
				System.out.println(line);
				Message m = new Message(line); //create a message
				m.update(editor.getSketch()); // update the message with the editor's sketch 
				editor.repaint(); //paint the current sketch on the editor side
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}	

	// Send editor requests to the server
	// methods sent as messages to the server communicator
	// must send message as strings
	public void delete(int selected) {
		send("delete " + selected);
	}
	//need to know the selected shape(index) and color to recolor
	public void recolor(int selected, Color color) {
		send("recolor " + selected + " " + color.getRGB());
	}
	//need to know the selected shape(index) and the point that it is moved to
	public void moveTo(int selected, int x, int y) {
		send("moveTo " + selected + " " + x + " " + y);
	}
	//need to know the type of shape to add
	public void add(Shape shape) {
		send("add " + shape.toString());
	}
}
