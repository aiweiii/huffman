import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.util.HashMap;

public class ImagetoPixelConverter {
    private BufferedImage image;
    private int[][][] pixelData;
    private HashMap<Integer, Integer> colorsMap;
    private HashMap<Integer, Integer> duplicatesMap;

    public HashMap<Integer, Integer> findDuplicates(int color) {
        // Find duplicated pixels
        if (colorsMap.get(color) == null) {
            colorsMap.put(color, 1);
        } else {
            colorsMap.put(color, colorsMap.get(color) + 1);
        }
        return colorsMap;
    }
    
    public void printDuplicates(HashMap<Integer, Integer> map) {
        // Initialize duplicatesMap array
        this.duplicatesMap = new HashMap<>();

        for (var entry : map.entrySet()) {
            if (entry.getValue() > 1) {
                duplicatesMap.put(entry.getKey(), entry.getValue());
            }
        }
        System.out.println("---------------------- ALL DUPLICATES --------------------------");
        System.out.println(duplicatesMap);
    }

    public ImagetoPixelConverter(String imagePath) {
        try {
            // Load the image from the specified file path
            File imageFile = new File(imagePath);
            this.image = ImageIO.read(imageFile);
            
            // Get image width and height
            int width = image.getWidth();
            int height = image.getHeight();
            
            // Initialize the pixelData array
            this.pixelData = new int[width][height][3];

            // Initialize colorsMap array
            this.colorsMap = new HashMap<>();
            
            // Convert the image into pixelData
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int color = image.getRGB(x, y);
                    findDuplicates(color);
                    // System.out.println("-------------------color: "+color);
                    int red = (color >> 16) & 0xFF;
                    int green = (color >> 8) & 0xFF;
                    int blue = color & 0xFF;
                    pixelData[x][y][0] = red;
                    // System.out.println("-------------------red: " + pixelData[x][y][0]);
                    pixelData[x][y][1] = green;
                    // System.out.println("-------------------green: "+ pixelData[x][y][1]);
                    pixelData[x][y][2] = blue;
                    // System.out.println("-------------------blue: " + pixelData[x][y][2]);
                }
            }
            // System.out.println("---------------------- DUPLICATESMAP --------------------------");
            // printDuplicates(colorsMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int[][][] getPixelData() {
        return pixelData;
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }
    
}

