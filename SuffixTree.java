import java.util.ArrayList;
import java.util.HashMap;

public class SuffixTree {
	private Node head;
	
	private class Node {
		public HashMap<Character, Node> childeren;
		public boolean end;
		
		public Node (boolean end) {
			this.end = end;
			childeren = new HashMap<Character, Node>(); //new and improved with hash map
		}
		
		public void addChild(char c, Node child) {
			childeren.putIfAbsent(c, child);
		}
		
		public Node traverse(char c){
			return childeren.get(c);
		}
	}
	public SuffixTree () {
		head = new Node(false); //the head node
	}
	
	public void insert(String word) {
		Node cur = head;
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			boolean end = false;
			
			if(i == word.length() -1) {
				end = true; //its the end of the word and we know it
			}
			cur.addChild(c, new Node(end)); //and a child is born
			
			cur = cur.traverse(c); //Travel to the child
		}
	}
	
	public void insertByLevel(String word, boolean end){
		ArrayList<Node> ara = new ArrayList<Node>(head.childeren.values());
		if(ara.isEmpty()) {
			for(int i = 0; i < word.length(); i++) {
				head.childeren.put(word.charAt(i), new Node(end));
			}
		}
		for(Node child: ara) {
			insertLevel(child, word, end);
		}
	}
	
	private void insertLevel(Node cur, String word, boolean end) {
		ArrayList<Node> ara = new ArrayList<Node>(cur.childeren.values());
		if(ara.isEmpty()) {
			for(int i = 0; i < word.length(); i++) {
				cur.childeren.put(word.charAt(i), new Node(end));
			}
		}
		for(Node child: ara) {
			insertLevel(child, word, end);
		}
	}
	
	
	public void searchDictionary(SuffixTree dictionary){
		for(Character key: head.childeren.keySet()) {
			searchDictionary(dictionary, "" + key, head.childeren.get(key));
		}
	}
	
	private void searchDictionary(SuffixTree dictionary, String word, Node cur){
		if(cur.end) {
			if(dictionary.lookUp(word))
				System.out.println(word);
		}
		for(Character key: cur.childeren.keySet()) {
			searchDictionary(dictionary, word + key, cur.childeren.get(key));
		}
	}
	
	public void searchDictionaryV2(SuffixTree dictionary){
		for(Character key: head.childeren.keySet()) {
			searchDictionaryV2(dictionary, "" + key, head.childeren.get(key));
		}
	}
	
	private void searchDictionaryV2(SuffixTree dictionary, String word, Node cur){
		if(cur.end) {
			if(dictionary.lookUp(word))
				System.out.println(word);
		}
		for(Character key: cur.childeren.keySet()) {
			if(validSuffix(word+key)) {
				searchDictionary(dictionary, word + key, cur.childeren.get(key));
			}
		}
	}
	
	public void searchDictionary(HashMap<String, String> dictionary){
		for(Character key: head.childeren.keySet()) {
			searchDictionary(dictionary, "" + key, head.childeren.get(key));
		}
	}
	
	private void searchDictionary(HashMap<String, String> dictionary, String word, Node cur){
		if(cur.end) {
			if(dictionary.get(word) != null)
				System.out.println(word);
		}
		for(Character key: cur.childeren.keySet()) {
			searchDictionary(dictionary, word + key, cur.childeren.get(key));
		}
	}
	
	public boolean validSuffix(String word) {
		Node cur = head;
		if(word.length() == 0) {
			return false;
		}
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			cur = cur.traverse(c);
			if (cur == null) { //no child was found
				return false;
			}
		}
		return true;
	}

	public boolean lookUp(String word) {
		Node cur = head;
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i);
			cur = cur.traverse(c);
			if (cur == null) { //no child was found
				return false;
			}
		}
		if(cur.end) { //if end of input and dictionary word match up
			return true;
		}
		return false;
	}
	
	
}







