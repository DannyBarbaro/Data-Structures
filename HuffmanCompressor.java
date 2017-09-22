import java.util.Scanner;
import java.util.Arrays;
import java.io.*;

//Class to perform the compression of a text file
public class HuffmanCompressor {

  //main method to run the compression
  public static void main(String[] args) {
    int[] freqNums = scanFile(args[0]);
    HuffmanNode[] nodeArr = createNodes(freqNums);
    nodeArr = freqSort(nodeArr);
    HuffmanNode top = createTree(nodeArr);
    String empty = "";
    String[] encodings = new String[94];
    encodings = getCodes(top, empty, false, encodings, true);
    printEncoding(encodings, freqNums);
    writeFile(args[0], args[1], encodings);
  }

  //method to read the input file and get frequencyies for letters
  public static int[] scanFile(String fileName){
    File file = new File(fileName);
    int[] freqNums = new int[94];
    //try catch in case the file doesn't exist
    try {
        Scanner scan = new Scanner(file);
        //loop to look at each word in the file
        while (scan.hasNext()) {
          String s = scan.next();
          //loop to look at each character in the word
          for(int i = 0; i < s.length(); i++){
            if((int)s.charAt(i)-32 <= 94)
              freqNums[(int)s.charAt(i)-32] = freqNums[(int)s.charAt(i)-32] + 1;
          }
          //accounts for all the spaces
          freqNums[0] = freqNums[0]+1;
        }
        scan.close();
        //gets rid of the imaginary last space
        freqNums[0] = freqNums[0]-1;
    }
    catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    return freqNums;
  }

  //method to take the frequencies and create the Nodes
  public static HuffmanNode[] createNodes(int[] freqNums){
    HuffmanNode[] nodeArr = new HuffmanNode[94];
    for(int i = 0; i < freqNums.length; i++){
      nodeArr[i] = new HuffmanNode((char)(i+32), freqNums[i], null, null);
    }
    return nodeArr;
  }

  // method to sort the node array by frequency
  public static HuffmanNode[] freqSort(HuffmanNode[] nodeArr){
    Arrays.sort(nodeArr);
    return nodeArr;
  }

  //method to combine 2 huffman Nodes
  public static HuffmanNode combineNodes(HuffmanNode left, HuffmanNode right){
    HuffmanNode combined = new HuffmanNode(null, left.getFrequency()+right.getFrequency(), left, right);
    return combined;
  }

  //method to create the Huffman tree
  public static HuffmanNode createTree(HuffmanNode[] nodeArr){
    HuffmanNode[] garbageArr = nodeArr;
    while(garbageArr.length != 1){
      HuffmanNode[] tempArr = new HuffmanNode[garbageArr.length-1];
      tempArr[0] = combineNodes(garbageArr[0], garbageArr[1]);
      for(int i = 1; i < garbageArr.length-1; i++){
        tempArr[i] = garbageArr[i+1];
      }
      garbageArr = freqSort(tempArr);
    }
    return garbageArr[0];
  }

  //method to get the encodings from the tree
  public static String[] getCodes(HuffmanNode node, String code, boolean wentLeft, String[] encodings, boolean isRoot) {
    //the corner case of just a single node
    if(isRoot && node.getLeft() == null && node.getRight() == null) {
      encodings[node.getInChar() - 32] = "0";
      return encodings;
    }
    
    //ends the recussion if the tree is empty
    if(node == null) {
      return encodings;
    }

    //adds the "binary" to the string depending on going left or right
    if(wentLeft && !isRoot) {
      code += "0";
    } else if (!wentLeft && !isRoot){
      code += "1";
    }

    //if it hits the end, it saves the encoding, else it splits to the left and right
    if(node.getLeft() == null && node.getRight() == null) {
      encodings[node.getInChar() - 32] = code;
    } else {
      getCodes(node.getLeft(),code,true,encodings,false);
      getCodes(node.getRight(),code,false,encodings,false);
    }

    return encodings;
  }


  //method to print out the encoding list and calculate the saved space
  public static void printEncoding(String[] encodings, int[] freqNums){
    int saveSpace = 0;
    //loops through all characters to print encodings and calculate savings
    for(int i = 0; i < encodings.length; i++) {
      if (freqNums[i] != 0) {
        System.out.println("'" + (char)(i+32) + "'" + ": " + freqNums[i] + ": " + encodings[i]);
        saveSpace = saveSpace + (8 - encodings[i].length())*freqNums[i];
      }
    }
    System.out.println();
    System.out.println("You will save " + saveSpace + " bits with the Huffman Compressor");
  }

  //methods to scan the input file and create the encoded output
  public static void writeFile(String inputFile, String outputFile, String[] encodings){
    File iFile = new File(inputFile);
    File oFile = new File(outputFile);
    //try catch for creating the output file
    try {
      //if it doesn't exist it creates the file. else if clears the output file
	    if (oFile.createNewFile()){
        System.out.println("File is created!");
	    } else{
        PrintWriter writer = new PrintWriter(oFile);
        writer.print("");
        writer.close();
        System.out.println("File cleared.");
	    }
    }
    catch(IOException e){
      e.printStackTrace();
    }
    //try catch to scan the input file
    try {
      Scanner scan = new Scanner(iFile);
      PrintWriter writer = new PrintWriter(oFile);
      //loop to look at each word in the file
      while (scan.hasNext()) {
        String s = scan.next();
        //loop to look at each character in the word
        for(int i = 0; i < s.length(); i++){
          if((int)s.charAt(i)-32 <= 94) {
            writer.print(encodings[(int)s.charAt(i)-32]);
          }
        }
        //gives the space between words
        writer.print(encodings[0]);
      }
      scan.close();
    }
    catch (FileNotFoundException e) {
        e.printStackTrace();
    }
  }
}
