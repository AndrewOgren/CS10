import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.io.*;

import javax.swing.JFileChooser;

import net.datastructures.Edge;
import net.datastructures.Vertex;
/**
 * A class to compute the bacon number of an actor
 *  finds how many "acquaintance links" apart an actor is
 * @author Andrew Ogren
 * @author Barry Yang
 * @param <V>
 * @param <E>
 */


public class baconGame<V, E> {
	
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
		}finally {
			input.close(); //closes the input file
			}
		
		return ID; //returns the map	
	}
	public static Map<String, ArrayList<String>> actorMovieMap(Reader movieActors, Map<String, String> actors, Map<String, String> movies) throws IOException{
		BufferedReader input = new BufferedReader(movieActors); 
		Map<String, ArrayList<String>> movieActorMap = new HashMap<String, ArrayList<String>>(); //creates a map with a string as the key and an arraylist as the value
		String line;
		try {
			while((line = input.readLine()) != null){
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
					
		}finally{
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
	public static <V, E> int baconNumber(DirectedAdjListMap<V, E> directedGraph, AdjacencyListGraphMap<V,E> undirectedGraph, V name){
		
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
		while (!vertexName.equals("Kevin Bacon")) { //as long as actor is not Kevin Bacon (the root)
			
			//gets the edge
			for (Edge<E> edge: directedGraph.incidentEdges(currentVertex)){
				e = edge;
				break;}	
			
			//writes out the path
			System.out.println(vertexName + " appeared in " + e.element() + " with " + directedGraph.opposite(currentVertex, e) + ".");
			currentVertex = directedGraph.opposite(currentVertex, e); //gets the next vertex (other vertex at end of path)
			vertexName = (String) currentVertex.element(); //change the vertex name to the current vertex
			baconNumber++; //increments bacon number
		}
		return baconNumber;
	}
	
	public static void main(String[] args) throws Exception {
    	
		//read files and create map
    	Reader actorFile = new FileReader("actorsTest.txt");
    	Reader movieFile = new FileReader("moviesTest.txt");
    	Map<String, String> actorMap = createIDMap(actorFile);
    	Map<String, String> movieMap = createIDMap(movieFile);
    	Reader actorMovieFile = new FileReader("movie-actorsTest.txt");
    	Map<String, ArrayList<String>> actorMovieMap = actorMovieMap(actorMovieFile, actorMap, movieMap);
    	
    	//creates graphs from maps
    	AdjacencyListGraphMap<String, String> movieActorGraph = createGraph(actorMovieMap, actorMap);
    	System.out.println(movieActorGraph);
    	DirectedAdjListMap<String, String> directedActorMovieGraph = bfs(movieActorGraph, "Kevin Bacon");
    	
    	/*
    	Scanner input = new Scanner(System.in);
    	System.out.println("Enter the name of an actor: ");
    	String search = input.nextLine();
    	
    	while (!search.equals("q")){ //as long as the user does not quit
    		int num = baconNumber(directedActorMovieGraph, movieActorGraph, search);
    		
    		//if statements deal with boundary cases, as explained in baconNumber method.
    		if (num == -1) {
    			System.out.println("Bacon Number is infinite. Not connected to Kevin Bacon.");
    		}
    		else if(num == -2){
    			System.out.println("Not in the list. Cannot determine Bacon Number.");
    		}
    		else {
    			System.out.println(search + "'s" +" Kevin Bacon number is " + num +".");
    		}
    		
    		//finishes with particular actor, reprompts user for next command
    		System.out.println("Thanks for playing. Press 'q' to quit. Else, enter the name of an actor: ");
    		search= input.nextLine();
    	}
    	input.close();
	}	*/
} }