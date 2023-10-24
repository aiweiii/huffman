# Huffman
Huffman encoding for lossless image compression.

### Please Note
1. In `App.java`, line 24 is changed to only read one file (for testing purposes).
2. Navigate to `project` > `Reference` > `HuffmanCoding.java` for the original Huffman Encoding/Decoding methods (that reads in a string). Might be helpful to understand how it works. In `Utility.java`, these methods are then modified to read in integer arrays (pixel data) instead of just a string.
3. Run `javac -d classes/ App.java` to compile
4. Run `java -cp classes/ App` to run (haha). 
5. Alternatively, you can run `make compile` and `make run` if you have `make` installed in your shell.