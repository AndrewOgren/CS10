import java.io.*;
import java.util.*;

import javax.swing.*;

public class frequencyTable {
	int initialCapacity;
	static TreeComparator comparator;
	static PriorityQueue<BinaryTree<Data>> queue;
	static String filePathName;
	
	public static Map<Character, Integer> findFrequencies(){
		int i = 0;
		BufferedReader input;
		filePathName = getFilePath();
		Map<Character, Integer> charFreq = new TreeMap<Character, Integer>();
		try {
			input = new BufferedReader(new FileReader(filePathName));
		
			while((i= input.read()) != -1){
				char Char = (char)i;
				if (charFreq.containsKey(Char)){
					charFreq.put(Char, (charFreq.get(Char))+1);
					
				}
				else{
					charFreq.put(Char, 1);
				}	
			}
			input.close();
			System.out.println(charFreq);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return charFreq;
	}
	
	
	
	public static void buildQueue(Map<Character, Integer> charFreq) {
		comparator = new TreeComparator();
		queue = new PriorityQueue<BinaryTree<Data>>(charFreq.size(), comparator);
		for(Character character: charFreq.keySet()){
			Data d = new Data(character, charFreq.get(character).intValue());
			BinaryTree<Data> Node = new BinaryTree<Data>(d);
			queue.add(Node);}	
		}
	
	public static BinaryTree<Data> buildTree() {
		while (queue.size() > 1) {
			BinaryTree<Data> tree1 = queue.poll();
			BinaryTree<Data> tree2 = queue.poll();
			int sum = tree1.getData().getFrequency() + tree2.getData().getFrequency();
			Data dummy = new Data('0', sum);
			BinaryTree<Data> parent = new BinaryTree<Data>(dummy, tree1, tree2);
			queue.add(parent);
		}
		BinaryTree<Data> tree = queue.poll();
		System.out.println(tree);
		return tree;
		
	}
	public static Map<Character, String> codeTree(BinaryTree<Data> tree){
		Map<Character, String> codeMap = new TreeMap<Character, String>();
		String code = "";
		traverseTree(tree, codeMap, code);
		return codeMap;
		
	}	
	public static void traverseTree(BinaryTree<Data> tree, Map<Character, String> codeMap, String code){ 
		if (tree.isLeaf()) {
			codeMap.put(tree.getData().getCharacter(), code);
			//code = code.substring(0, code.length()-1);
		}
		
		else{
			if (tree.hasLeft()) {
			traverseTree(tree.getLeft(), codeMap, code + 0);
			}
			if (tree.hasRight()){
			traverseTree(tree.getRight(), codeMap, code + 1);
			}
		}
		
	}
	public static void compression(Map <Character, String> codeMap){
		BufferedReader input;
		BufferedBitWriter bitOutput;
		int i = 0;
		try {
			input = new BufferedReader(new FileReader(filePathName));
			bitOutput = new BufferedBitWriter(filePathName.substring(0, filePathName.length()-4)+"_compressed.txt");
			try {
				while((i= input.read()) != -1){
					char Char = (char)i;
					String codeWord = codeMap.get(Char);
					
					for(int j= 0; j<codeWord.length(); j++){
						
						bitOutput.writeBit(Character.getNumericValue(codeWord.charAt(j)));
					}	
				}
			input.close();
			bitOutput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			}
}
	public static void decompression(BinaryTree<Data> tree){
		BufferedWriter output;
		BufferedBitReader bitInput;
		int i = 0;
		BinaryTree<Data> currentTree = tree;
		try {
			output = new BufferedWriter(new FileWriter(filePathName.substring(0, filePathName.length()-4)+"_decompressed.txt"));
			bitInput = new BufferedBitReader(filePathName.substring(0, filePathName.length()-4)+"_compressed.txt");
			while ((i= bitInput.readBit()) != -1){
				int bit = i;
				if (currentTree.hasLeft() && bit == 0){
					currentTree = currentTree.getLeft();	
				}
				if (currentTree.hasRight() && bit == 1){
					currentTree = currentTree.getRight();
				}
				if (currentTree.isLeaf()){
					output.write(currentTree.getData().getCharacter());
					currentTree = tree;
				}
			}
			output.close();
			bitInput.close();
		}
		catch (IOException e) {
			System.out.println("Failure");
			e.printStackTrace();
		}
	}
	
	
	public static String getFilePath() {
	    JFileChooser fc = new JFileChooser("."); // start at current directory

	    int returnVal = fc.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        String pathName = file.getAbsolutePath();
	        return pathName;
	    }
	    else {
	        return "";  
	    }
	}
	
	
	public static void main(String[] args) {
		Map<Character, Integer> frequencyMap = findFrequencies();
		buildQueue(frequencyMap);
		BinaryTree<Data> Tree = buildTree();
		Map<Character, String> codeMap = codeTree(Tree);
		System.out.println(codeMap);
		compression(codeMap);
		decompression(Tree);
		
		
	}
}
