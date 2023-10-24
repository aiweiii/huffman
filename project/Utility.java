import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class Utility {

    public class HuffmanNode {
        int pixel;
        int frequency;
        HuffmanNode left;
        HuffmanNode right;

        //constructor, Time O(1) Space O(1)
        HuffmanNode(int pixel, int frequency, HuffmanNode left, HuffmanNode right) {
            this.pixel = pixel;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        // Time O(1) Space O(1)
        @Override
        public String toString() {
            return "(" + pixel + ", " + frequency + ")";
        }
    }
    
    private HashMap<Integer, Integer> colorsMap; // change to TreeMap to test hypothesis: speed will be slightly slower than hashmap but saves memory https://www.baeldung.com/java-treemap-vs-hashmap
    private LinkedList<HuffmanNode> sortedColorsMap;
    private HuffmanNode root;
    private String colorBinaryString;
    private StringBuilder encodedPixelsSb = new StringBuilder();

    // builds the color frequency map
    public void addColorToMap(int color) {
        colorsMap.put(color, colorsMap.getOrDefault(color, 0) + 1);
    }

    // sorts the color frequency map in ascending order of frequency
    public void sortColorMapByFrequency() { // can consider using priority queue https://www.geeksforgeeks.org/implement-priorityqueue-comparator-java/
        this.sortedColorsMap = new LinkedList<>(); // a priority queue implementation
        for (Map.Entry<Integer, Integer> entry : colorsMap.entrySet()) {
            sortedColorsMap.add(new HuffmanNode(entry.getKey(), entry.getValue(), null, null));
        }
        sortedColorsMap.sort((a, b) -> a.frequency - b.frequency);
    }
    
    private HuffmanNode buildTree(Queue<HuffmanNode> nodeQueue) {
        while (nodeQueue.size() > 1) {
            HuffmanNode node1 = nodeQueue.remove();
            HuffmanNode node2 = nodeQueue.remove();
            HuffmanNode node = new HuffmanNode(' ', node1.frequency + node2.frequency, node1, node2);
            nodeQueue.add(node);
        }
        return nodeQueue.remove();
    }
    
    private Map<Integer, String> createHuffmanCode(HuffmanNode node) {
        var map = new HashMap<Integer, String>();
        createCodeRec(node, map, "");
        return map;
    }
    
    private void createCodeRec(HuffmanNode node, Map<Integer, String> map, String s) {
        if (node.left == null && node.right == null) {
            map.put(node.pixel, s);
            return;
        }
        createCodeRec(node.left, map, s + '0');
        createCodeRec(node.right, map, s + '1');
    }

    // ACTUAL IMPLEMENTATION
    public void Compress(int[][][] pixels, String outputFileName) throws IOException {

        // Initialize colorsMap array
        this.colorsMap = new HashMap<>();
            
        int width = pixels.length;
        int height = pixels[0].length;

        int count1 = 0;
        int count2 = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red = pixels[x][y][0];
                int green = pixels[x][y][1];
                int blue = pixels[x][y][2];
                int color = (red << 16) | (green << 8) | blue;
                addColorToMap(color);
            }
        }
        // System.out.println("colorsMap: " + colorsMap);

        sortColorMapByFrequency();
        // System.out.println(sortedColorsMap);

        root = buildTree(sortedColorsMap);
        // System.out.println("root: " + root);

        Map<Integer, String> codeMap = createHuffmanCode(root);

        // encode each pixel using the codeMap + combine all encoded pixels to form a STRING (encodedPixelsSb)
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red = pixels[x][y][0];
                int green = pixels[x][y][1];
                int blue = pixels[x][y][2];
                int color = (red << 16) | (green << 8) | blue;

                colorBinaryString = codeMap.get(color);
                // System.out.println("colorBinaryString: " + colorBinaryString);
                encodedPixelsSb.append(colorBinaryString);
            }
        }

        // write BOTH encodedPixelsSb and codeMap into a bin file
        try {
            FileOutputStream fos = new FileOutputStream(outputFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            /* -- Write encodedPixelsSb bit-by-bit into a bin file --
             * Idea is to combine all the bits until they reach 8-bits aka 1 byte, then pump it into the file
             * Otherwise, each character in the string is being pumped into the file as 8 bits instead of 1 bit
             */
            String sequence = encodedPixelsSb.toString();
            
            int index = 0;
            int currentByte = 0;

            for (char bit : sequence.toCharArray()) {
                if (bit == '1') {
                    currentByte = (currentByte << 1) | 1;
                } else if (bit == '0') {
                    currentByte = currentByte << 1;
                } else {
                    throw new IllegalArgumentException("Invalid character in bit sequence: " + bit);
                }

                index++;

                if (index == 8) {
                    fos.write(currentByte);
                    currentByte = 0;
                    index = 0;
                }
            }

            // Write any remaining bits (if the bit sequence's length is not a multiple of 8)
            if (index > 0) {
                currentByte <<= (8 - index);
                fos.write(currentByte);
            }

            /*
             * Write codeMap as object into the file
             */
            // oos.writeObject(codeMap);

            fos.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int[][][] Decompress(String inputFileName) throws IOException, ClassNotFoundException {
        // The following is a bad implementation that we have intentionally put in the function to make App.java run, you should 
        // write code to reimplement the function without changing any of the input parameters, and making sure that it returns
        // an int [][][]

        // FileInputStream reads image line by line 
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFileName))) {
            Object obj1 = ois.readObject();
            Object obj2 = ois.readObject();


            if ((obj1 instanceof int[][][]) && (obj2 instanceof Map)) {
                int[][][] pixelData = (int[][][]) obj1;
                Map decodedMap = (Map) obj2;

                int width = pixelData.length;
                int height = pixelData[0].length;

            } else {
                throw new IOException("Invalid object type in the input file (obj1)");
            }

        }
        return new int[1][1][1];
    }
    
}

