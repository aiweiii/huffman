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

    private int[][][] decode(String encoded, int width, int height) {
        int[][][] pixelData = new int[width][height][1];
        int x = 0;
        int y = 0;

	    HuffmanNode curr = root;
        for (int i = 0; i < encoded.length(); i++) {
            curr = encoded.charAt(i) == '1' ? curr.right : curr.left;
            if (curr.left == null && curr.right == null) {
                // add pixel into pixelData array
                // if (x >= width) {

                // }
                if (y >= height) {
                    x++;
                    y = 0;
                }
                if (x >= width) {
                    return pixelData;
                }
                pixelData[x][y][0] = curr.pixel;
                // System.out.println("halo at x:" + x + ", y:" + y + " = " + pixelData[x][y][0]);
                y++;
                curr = root;
            }
        }
        
        // for (int i = 0; i < width; i++) {
        //     for (int j = 0; j < height; j++) {
        //         // System.out.println("color at x: " + i + ",y: " + j+ " = " + pixelData[i][j][0]);
        //     }
        // }
        return pixelData;
	}

    // ACTUAL IMPLEMENTATION
    public void Compress(int[][][] pixels, String outputFileName) throws IOException {

        // Initialize colorsMap array
        this.colorsMap = new HashMap<>();

        int width = pixels.length;
        int height = pixels[0].length;

        // System.out.println("original width: " + width);
        // System.out.println("original height: " + height);

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
                // System.out.println("color at x: " + x + ",y: " + y + " = " + color);

                colorBinaryString = codeMap.get(color);
                // System.out.println("colorBinaryString: " + colorBinaryString);
                encodedPixelsSb.append(colorBinaryString);
            }
        }

        // write encodedPixelsSb into a bin file
        try {
            FileOutputStream fos = new FileOutputStream(outputFileName);
            DataOutputStream dos = new DataOutputStream(fos);
            // ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Write width and height into file
            dos.writeInt(width);
            dos.writeInt(height);
            
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

            fos.close();
            // oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int[][][] Decompress(String inputFileName) throws IOException, ClassNotFoundException {
        // The following is a bad implementation that we have intentionally put in the function to make App.java run, you should 
        // write code to reimplement the function without changing any of the input parameters, and making sure that it returns
        // an int [][][]
        try {
            FileInputStream fis = new FileInputStream(inputFileName);
            DataInputStream dis = new DataInputStream(fis);

            int width = dis.readInt();
            int height = dis.readInt();
            // System.out.println("check width: " + width);
            // System.out.println("check height: " + height);

            int currentByte;
            StringBuilder bitSequence = new StringBuilder();

            while ((currentByte = fis.read()) != -1) {
                for (int i = 7; i >= 0; i--) {
                    char bit = ((currentByte >> i) & 1) == 1 ? '1' : '0';
                    bitSequence.append(bit);
                }
            }
            // System.out.println("Read Bit Sequence: " + bitSequence.toString());

            decode(bitSequence.toString(), width, height);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[1][1][1];
    }
    
}

