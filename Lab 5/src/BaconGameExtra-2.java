/**
 * A class to compute the bacon number of an actor
 *  finds how many "acquaintance links" apart an actor is
 * @author Andrew Ogren
 * @author Barry Yang
 * @param <V>
 * @param <E>
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.io.*;

import javax.swing.JFileChooser;

import net.datastructures.Edge;
import net.datastructures.Vertex;

public class BaconGameExtra<V, E> {
	
	//memoization implementation
	private static Map<String, ArrayList<String>> memoizedStatements = new HashMap<String, ArrayList<String>>();
	private static Map<String, Integer> memoizedActors = new HashMap<String, Integer>();
	
	public static <V, E> DirectedAdjListMap<V, E> bfs(AdjacencyListGraphMap<V, E> graph, V root) throws Exception {
    	
    	DirectedAdjListMap<V, E> directedGraph = new DirectedAdjListMap<V, E>(); //creates an empty directed graph
    	SLLQueue<Vertex<V>> queue = new SLLQueue<Vertex<V>>(); //creates an empty queue
    	
    	queue.enqueue(graph.getVertex(root)); //inserts the root into the empty queue
    	directedGraph.insertVertex(root); //inserts the root vertex into the directed graph
    	
    	while(!queue.isEmpty()) { //while there are still vertices in the queue
    		
    		Vertex<V> v = queue.dequeue(); //return the next vertex in the queue
    		for(Edge<E> e : graph.incidentEdges(v)) { //for all of the incident edges of this vertex
    			Vertex<V> adjacent = graph.opposite(v, e); //get the adjacent vertex given that edge 
    			if(!directedGraph.vertexInGraph(adjacent.element())) { //if this vertex is not in the graph
    				directedGraph.insertVertex(adjacent.element()); //add it to the graph
    				directedGraph.insertDirectedEdge(adjacent.element(), v.element(), e.element()); //insert a directed edge between the two vertices
    				queue.enqueue(adjacent); //add the adjacent vertex to the queue
    			}
    		}
    	}
    	return directedGraph; //returns the directed graph once the queue is empty
    }
	
	public static Map<String, String> createIDMap(Reader fileName) throws IOException{
		BufferedReader input = new BufferedReader(fileName); //the input of the given filename
		Map<String, String> ID = new HashMap<String, String>(); //creates a map with strings as keys and values
		String line;
		try {
			while((line = input.readLine()) != null){ //while there are still more lines in the file
				String[] IDArray = new String[2]; //create an empty array with 2 element spaces
				IDArray = line.split("\\|"); //adds to that array with the pieces of the line in each position
				ID.put(IDArray[0], IDArray[1]); //put the key and value in the map based on the contents of the array
			}
		} finally {
			input.close(); //closes the input file
			}
		return ID; //returns the map	
	}
	
	public static Map<String, ArrayList<String>> actorMovieMap(Reader movieActors, Map<String, String> actors, Map<String, String> movies) throws IOException{
		BufferedReader input = new BufferedReader(movieActors); 
		Map<String, ArrayList<String>> movieActorMap = new HashMap<String, ArrayList<String>>(); //creates a map with a string as the key and an arraylist as the value
		String line;
		try {
			while((line = input.readLine()) != null) {
				String[] Array = new String[2];
				Array = line.split("\\|");
				if (movieActorMap.containsKey(movies.get(Array[0]))){ //if the key has already been created
					movieActorMap.get(movies.get(Array[0])).add(actors.get(Array[1])); //add another actor the the arraylist 
				}
				else {
					ArrayList<String> actorList = new ArrayList<String>(); //create an empty arraylist
					actorList.add(actors.get(Array[1])); //add the actor
					movieActorMap.put(movies.get(Array[0]), actorList); //put the arraylist in the map along with the key (movie name)
				}
			}		
		} finally {
			input.close(); //close the file
		}
		return movieActorMap; //return the map
	}
	
	@SuppressWarnings("unchecked")
	public static <V, E> AdjacencyListGraphMap<V, E> createGraph(Map<String, ArrayList<String>> movieActorMap, Map<String, String> actorMap){
		AdjacencyListGraphMap<V, E> movieActorGraph = new AdjacencyListGraphMap<V, E>(); //an undirected map
		for (String actorID: actorMap.keySet()){ //run through the actor id's (the keys) in the actor map
			movieActorGraph.insertVertex((V) actorMap.get(actorID));} //insert the actual names of the actors as vertices in the graph
		
		for (String movie : movieActorMap.keySet()){ //goes through the movies (keys in the movieActorMap)
			ArrayList<String> actorArray = movieActorMap.get(movie); //array of all the actors in a movie
			for (String actor: actorArray){
				for(int i = actorArray.size()-1; i>= 0; i--){ //go through each actor in a movie twice to compare actors and establish an edge
					if (!actor.equals(actorArray.get(i))){ //establish edge with everyone in movie except last one
						movieActorGraph.insertEdge((V) actor, (V) actorArray.get(i), (E) movie);
					}
				}
			}	
		}
		return movieActorGraph;	
	}
	
	public static <V, E> int baconNumber(DirectedAdjListMap<V, E> directedGraph, AdjacencyListGraphMap<V,E> undirectedGraph, V name, boolean shouldPrint){
		
		//boundary case where actor is in the list but not in directed graph
		if (!directedGraph.vertexInGraph(name) && undirectedGraph.vertexInGraph(name)){
			return -1;
		}
		
		//boundary case where actor is not in the list
		if(!undirectedGraph.vertexInGraph(name)){
			return -2;
		}
		
		Edge<E> e = null;
		int baconNumber = 0;
		Vertex<V> currentVertex = directedGraph.getVertex(name);
		String vertexName = (String) currentVertex.element();
		String mapKey = (String) currentVertex.element();
		ArrayList<String> statements = new ArrayList<String>();
		while (!vertexName.equals("Kevin Bacon")) { //as long as actor is not Kevin Bacon (the root)
			
			//gets the edge
			for (Edge<E> edge: directedGraph.incidentEdges(currentVertex)) {
				e = edge;
				break;
			}	
			
			String statement = vertexName + " appeared in " + e.element() + " with " + directedGraph.opposite(currentVertex, e) + ".";
			statements.add(statement);
			
			//writes out the path
			if(shouldPrint) {
				System.out.println(vertexName + " appeared in " + e.element() + " with " + directedGraph.opposite(currentVertex, e) + ".");
			}
			currentVertex = directedGraph.opposite(currentVertex, e); //gets the next vertex (other vertex at end of path)
			vertexName = (String) currentVertex.element(); //change the vertex name to the current vertex
			baconNumber++; //increments bacon number
		}
		memoizedStatements.put(mapKey, statements);
		return baconNumber;
	}
	
	public static void main(String[] args) throws Exception {
    	
		//read files and create map
		Reader actorFile = new FileReader("/Users/barryyang/Documents/cs10workspace/lab5/./files/actors.txt");
    	Reader movieFile = new FileReader("/Users/barryyang/Documents/cs10workspace/lab5/./files/movies.txt");
    	Map<String, String> actorMap = createIDMap(actorFile);
    	Map<String, String> movieMap = createIDMap(movieFile);
    	
    	Reader actorMovieFile = new FileReader("/Users/barryyang/Documents/cs10workspace/lab5/./files/movie-actors.txt");
    	Map<String, ArrayList<String>> actorMovieMap = actorMovieMap(actorMovieFile, actorMap, movieMap);
    	
    	//creates graphs from maps
    	AdjacencyListGraphMap<String, String> movieActorGraph = createGraph(actorMovieMap, actorMap);
    	DirectedAdjListMap<String, String> directedActorMovieGraph = bfs(movieActorGraph, "Kevin Bacon");
    	
    	Scanner input = new Scanner(System.in);
    	System.out.println("Enter the name of an actor: ");
    	String search = input.nextLine();
		
    	while (!search.equals("q")) { //as long as the user does not quit
    		
    		//Extra Credit -- name suggestions
    		//Gives you suggestions for name if not in the list, based on last name given
    		if (!movieActorGraph.vertexInGraph(search) && !search.equals("statistics")){ //if the given actor name is not a vertex in undirected graph
	    		String[] firstAndLast = new String[4]; //None of the actors have more than four names
	    		firstAndLast = search.split(" "); //splits the names by spaces and adds them to the array
	    		String lastName = null;
	    		for (String partsofName: firstAndLast){
    				lastName = partsofName;} //get the last name
	    		
	    		for (Vertex<String> name: movieActorGraph.vertices()) { //runs through all of the actor vertices
	    			String fullName = name.element();
	    			String[] actorParts = new String[4];
	    			actorParts = fullName.split(" "); //splits the names by spaces and adds them to the array
	    			
	    			String actorlastName= null;
	 
	    			for (String partsofName: actorParts) {
	    				actorlastName = partsofName; //gets the last name of each of the actors
	    				}
	    			if (lastName.equals(actorlastName)) { //for each actor out of all of the actors with the same last name as the search
	    				System.out.print("Did you mean ");
	    				int i= 0;
	    				while(i < actorParts.length){
	    					System.out.print(actorParts[i] +" ");//prints out the actor's full name
	    					i++;
	    				}
	    			System.out.println("?");
	    			}
	    		}
    		}
    		
    		int num;
    		
    		//extra credit -- statistics
    		if (search.equals("statistics")) {
    			
    	    	//largest bacon number
    	    	//finds average bacon number
    			int largestPathNum = 0;
    			int baconSum = 0;
    			int baconSize = 0;
    			Vertex<String> actor = null;
    			for(Vertex<String> vertex : directedActorMovieGraph.vertices()) { //iterate through vertices
    				int temp = baconNumber(directedActorMovieGraph, movieActorGraph, vertex.element(), false);
    				memoizedActors.put(vertex.element(), temp);
    				if(temp != -1) { //find average
    					baconSum += temp;
    					baconSize += 1;
    				}
    				if (temp > largestPathNum) { //find largest
    					largestPathNum = temp;
    					actor = vertex;
    				}
    			}
    			System.out.println(actor.element() + " has largest bacon number of " + largestPathNum + ".");
    			System.out.println("Size of list is " + baconSize + ". Average Bacon number is " + baconSum/baconSize + ".");
    			num = -3;
    		}
    		
    		else {	
    			//extra credit --memoization
	    		if (memoizedActors.containsKey(search)) { //if the search is in map
	    			num = memoizedActors.get(search); //gets the number
	    			System.out.println("Fun fact: this is a memoized entry!"); //lets you know this is a memoized entry
	    			for (String statement : memoizedStatements.get(search)) { //print out memoized statements
	    				System.out.println(statement);
	    			}
	    		}
	    		else { //if search not in map, make a new entry
	    			num = baconNumber(directedActorMovieGraph, movieActorGraph, search, true);
	    			memoizedActors.put(search, num);
	    		}
    		}
    		
    		//if statements deal with boundary cases, as explained in baconNumber method.
    		if (num == -1) {
    			System.out.println("Bacon Number is infinite. Not connected to Kevin Bacon.");
    		}
    		else if (num == -2) {
    			System.out.println("Not in the list. Cannot determine Bacon Number.");
    		}
    		else if (num == -3){
    			System.out.println("Hope you enjoyed the statistics!");
    		}
    		else {
    			System.out.println(search + "'s" +" Kevin Bacon number is " + num +".");
    		}
    		
    		//finishes with particular actor, re-prompts user for next command
    		System.out.println("Thanks for playing. Press 'q' to quit. Type 'statistics' if you are curious about statistics.\nOtherwise, enter the name of an actor: ");
    		search= input.nextLine();
    	}
    	input.close();
	}	
}