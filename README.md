# Huffman for Image Compression/Decompression
Huffman encoding for lossless image compression.

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

### Caveat
1. If you test with `input.png`, you will end up with a larger file. I don't know why yet, but I realised even if you throw this into websites that do compression, they can't compress any further. Maybe because it's too small, then the algo reverse psyched itself. But yea, just putting it out there in case it might be helpful.