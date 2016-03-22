import java.io.*;
import java.net.Socket;

/**
 * Handles communication between the server and one client, for SketchServer
 * Dartmouth CS 10, Winter 2015
 * Andrew Ogren & Barry Yang
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);
			
			//Runs through the shapes in the sketch in the server
			for (int i = 0; i < server.getSketch().size(); i++) {
				Shape s =  server.getSketch().get(i); //gets the shape at the index
				if (s != null){ //makes sure this shape isn't null(hasn't been deleted)
				send("add " + s.toString()); //adds all of the shapes (that aren't null) to the server
			}
			}
			
			
			// Keep getting and handling messages from the client
			String line;
			while ((line = in.readLine()) != null) { //while messages are still being sent (not null)
				System.out.println(line);
				Message m = new Message(line); //receives the message and sets it as m
				m.update(server.getSketch()); //updates given the message
				server.broadcast(m.toString()); //broadcasts this message to all of the editors
				} 
				

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
