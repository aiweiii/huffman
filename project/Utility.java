import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class Utility implements Serializable {

    public void Compress(int[][][] pixels, String outputFileName) throws IOException {

        // Initialize colorsMap array
        var colorsMap = new HashMap<Integer, Integer>();

        int width = pixels.length;
        int height = pixels[0].length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red = pixels[x][y][0];
                int green = pixels[x][y][1];
                int blue = pixels[x][y][2];
                int color = (red << 16) | (green << 8) | blue;
                addColorToMap(color, colorsMap);
            }
        }

        LinkedList<HuffmanNode> sortedColorsList = sortColorMapByFrequency(colorsMap);

        HuffmanNode root = buildTree(sortedColorsList);

        Map<Integer, String> codeMap = createHuffmanCode(root);

        // encode each pixel using the codeMap + combine all encoded pixels to form a STRING (encodedPixelsSb)
        StringBuilder encodedPixelsSb = new StringBuilder();
        String colorBinaryString;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red = pixels[x][y][0];
                int green = pixels[x][y][1];
                int blue = pixels[x][y][2];
                int color = (red << 16) | (green << 8) | blue;
                // System.out.println("color at x: " + x + ",y: " + y + " = " + color);

                colorBinaryString = codeMap.get(color);
                encodedPixelsSb.append(colorBinaryString);
            }
        }

        // write encodedPixelsSb into a bin file
        try {
            FileOutputStream fos = new FileOutputStream(outputFileName);
            DataOutputStream dos = new DataOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            
            oos.writeObject(root);

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
            dos.close();
            oos.close();

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
            
            // https://mkyong.com/java/how-to-read-file-in-java-fileinputstream/#fileinputstream---better-performance claims better performance when reading in another way
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

    public class HuffmanNode implements Serializable{
        int pixel;
        int frequency;
        HuffmanNode left;
        HuffmanNode right;

        HuffmanNode(int pixel, int frequency, HuffmanNode left, HuffmanNode right) {
            this.pixel = pixel;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "(" + pixel + ", " + frequency + ")";
        }
    }
    

    // builds the color frequency map
    public void addColorToMap(int color, Map<Integer, Integer> map) {
        map.put(color, map.getOrDefault(color, 0) + 1);
    }

    // sorts the color frequency list in ascending order of frequency
    public LinkedList<HuffmanNode> sortColorMapByFrequency(Map<Integer, Integer> map) {
        LinkedList<HuffmanNode> sortedColorsList = new LinkedList<>(); // a priority queue implementation
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            sortedColorsList.add(new HuffmanNode(entry.getKey(), entry.getValue(), null, null));
        }
        sortedColorsList.sort((a, b) -> a.frequency - b.frequency);

        return sortedColorsList;
    }
    
    public HuffmanNode buildTree(Queue<HuffmanNode> nodeQueue) {
        while (nodeQueue.size() > 1) {
            HuffmanNode node1 = nodeQueue.remove();
            HuffmanNode node2 = nodeQueue.remove();
            HuffmanNode node = new HuffmanNode(' ', node1.frequency + node2.frequency, node1, node2);
            nodeQueue.add(node);
        }
        return nodeQueue.remove();
    }
    
    public Map<Integer, String> createHuffmanCode(HuffmanNode node) {
        var map = new HashMap<Integer, String>();
        createCodeRec(node, map, "");
        return map;
    }
    
    public void createCodeRec(HuffmanNode node, Map<Integer, String> map, String s) {
        if (node.left == null && node.right == null) {
            map.put(node.pixel, s);
            return;
        }
        createCodeRec(node.left, map, s + '0');
        createCodeRec(node.right, map, s + '1');
    }

    public int[][][] decode(String encoded, int width, int height, HuffmanNode root) {
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
    
}

