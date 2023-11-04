# Huffman for Image Compression/Decompression
Huffman encoding for lossless image compression.

## Current Problem
1. [25 Oct, 8am] Compression only works for 10404007.png. The rest becomes larger :(

### Please Note
1. In `App.java`, line 24 is changed to only read one file for testing purposes.
2. Navigate to `project` > `Reference` > `HuffmanCoding.java` for the original Huffman Encoding/Decoding methods (that reads in a string). Might be helpful to understand how it works. In `Utility.java`, these methods are then modified to read in integer arrays (pixel data) instead of just a string.

### Run the file
1. `cd project`
2. `javac -d classes/ App.java`
3. `java -cp classes/ App`

Alternatively, you can run `make compile` followed by `make run` if you have `make` installed in your shell.

### Useful References
1. https://courses.cs.washington.edu/courses/cse143x/15au/lectures/huffman/huffman.pdf
2. https://www.lavivienpost.com/971157d5-f94b-4817-9e14-7306ee3670ea (Original Huffman Code as seen in `HuffmanCoding.java`)
3. https://www.geeksforgeeks.org/image-compression-using-huffman-coding/ (`input.png` taken from here)
4. https://courses.cs.washington.edu/courses/csep590a/07au/ (Explores various lossless/lossy compression methods, gives pseudocode and time complexity)
5. https://opendsa-server.cs.vt.edu/ODSA/Books/CS3/html/index.html (There is quadtree and more here!)
6. https://courses.cs.washington.edu/courses/cse490g/06wi/project2/index.html (sample assignment with some code, not answer but can use as guide)