import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Huffman {
    
    public static TreeMap<Character, Integer> sortedTreeMap(String s) {
        HashMap<Character, Integer> charCountMap = new HashMap<>();
        TreeMap<Character, Integer> huffmanMap = new TreeMap<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (charCountMap.get(c) == null) {
                charCountMap.put(Character.valueOf(c), 1);
            } else {
                charCountMap.put(Character.valueOf(c), charCountMap.get(c) + 1);
            }
        }

        for (Map.Entry<Character, Integer> entry: charCountMap.entrySet()) {
            huffmanMap.put(entry.getKey(), entry.getValue());
        }

        System.out.println(huffmanMap);
        return huffmanMap;
    }

    public static void main(String[] args) {
        String s = "brbcbaaaaa";
        sortedTreeMap(s);
    }
}
