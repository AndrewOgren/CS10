import java.util.Map;
import java.util.PriorityQueue;
import java.util.Comparator;



public class Data {
	
	char character;
	int frequency;
	
	public Data(char character, int frequency){
		this.character = character;
		this.frequency = frequency;	
	}
	public char getCharacter(){
		return character;
	}
	public int getFrequency(){
		return frequency;
	}
	public String toString() {
		return "character is " + character + " frequency is " + frequency;
	}
	
}

