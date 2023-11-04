package Reference;

import java.util.*;

// https://www.lavivienpost.com/huffman-coding-and-decoding/
class HuffmanNode {
    char ch;
    int frequency;
    HuffmanNode left;
    HuffmanNode right;

    //constructor, Time O(1) Space O(1)
    HuffmanNode(char ch, int frequency,  HuffmanNode left,  HuffmanNode right) {
        this.ch = ch;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }  
    
    // Time O(1) Space O(1)
    @Override
    public String toString() {
    	return "(" + ch + ", " + frequency + ")";
    }
}

public class HuffmanCoding {
	HuffmanNode root;
	//All steps to create huffman code
	public Map<Character, String> getCode(String input) {  
		Map<Character, Integer> freqMap = buildFrequencyMap(input); 
        LinkedList<HuffmanNode> queue = sortByFrequency(freqMap);
        System.out.println("queue: " + queue);
		root = buildTree(queue);	    
		Map<Character, String> codeMap = createHuffmanCode(root);	
		return codeMap;
	}	
	
    //Step 1: Create char frequency map from input string, Time O(s) Space O(m), 
	//s is number of chars in input string, m is number of unique chars
    private Map<Character, Integer> buildFrequencyMap(String input) {
        Map<Character, Integer> map = new HashMap<Character, Integer>();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            map.put(ch, map.getOrDefault(ch, 0)+1);
        }
        return map;
    }
    
    //Step 2: Create queue of nodes from map and sort by frequency, Time O(mlogm) Space O(m)
    private LinkedList<HuffmanNode> sortByFrequency(Map<Character, Integer> map) {
        LinkedList<HuffmanNode> pq = new LinkedList<HuffmanNode>( );
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue(), null, null));
        }
        pq.sort((a,b) -> a.frequency - b.frequency);
        return pq;
    }  
    
    //Step 3. Build frequency-sorted binary tree from sorted queue, return root
    //Time O(m) Space O(n), m is unique chars in string, n is nodes in tree n=2m-1  
    private HuffmanNode buildTree(Queue<HuffmanNode> nodeQueue) {        
        while (nodeQueue.size() > 1) { 
            HuffmanNode node1 = nodeQueue.remove();
            HuffmanNode node2 = nodeQueue.remove();
            HuffmanNode node = new HuffmanNode(' ', node1.frequency + node2.frequency, node1, node2);
            // HuffmanNode(' ', 2, (c, 1, null, null), (d, 1, null, null)) 
            nodeQueue.add(node);
        }
        return nodeQueue.remove();
    }
    
	//Step 4. Create Huffman code map by preorder of the tree, Time O(n) Space O(m+n)
    private Map<Character, String> createHuffmanCode(HuffmanNode node) {
    	Map<Character, String> map = new HashMap<Character, String>();
    	createCodeRec(node, map, "");
    	return map;
    }
    
    //Preorder of the tree using recursion, Time O(n) Space O(n), n is number of nodes in the tree
    private void createCodeRec(HuffmanNode node, Map<Character, String> map, String s) {
    	if (node.left == null && node.right == null) {
    		map.put(node.ch, s);
            return;
        }    
    	createCodeRec(node.left, map, s + '0');
    	createCodeRec(node.right, map, s + '1' );
    }
    
    //Use huffman code to encode the input string, Time O(s) Space O(o), 
    //s is input string length, o is output string length
    public String encode(Map<Character, String> codeMap, String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            sb.append(codeMap.get(input.charAt(i)));
        }
        return sb.toString();
    }
    
    //Time O(o), Space O(s), o is coded message length, s is original message input
	public String decode(String coded) {
	    StringBuilder sb = new StringBuilder();
	    HuffmanNode curr = root;
	    for (int i = 0; i < coded.length(); i++) {
	        curr = coded.charAt(i) == '1' ? curr.right : curr.left;
	        if (curr.left == null && curr.right == null) {
	            sb.append(curr.ch);
	            curr = root;
	        }
	    }
	    return sb.toString();
	}

    public static void main(String[] args) {
    	String input="ABCD";
    	HuffmanCoding huffman = new HuffmanCoding();       
        Map<Character, String> codeMap = huffman.getCode(input);
        System.out.println("code: " + codeMap);
        String encoded = huffman.encode(codeMap, input);
        System.out.println("encoding string: " + encoded);    
        
        String decode = huffman.decode(encoded);
        System.out.println("decoding string: " + decode);
    }
}
