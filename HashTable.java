import java.util.*;
import java.io.*;
//Class for creating a hash table from a text file
public class HashTable {

  //The array list for the hash nodes
  private HashNode[] hashTable;

  //size of the table
  private int tableSize;

  //constructor
  public HashTable() {
    tableSize = 16;
    hashTable = new HashNode[tableSize];
  }

  //main method that takes 2 args. input file and output file
  public static void main(String[] args) {
    HashTable ht = new HashTable();
    ht.wordCount(args[0]);
    ht.writeFile(args[1]);
  }

  //read file word by word and hash it
  public void wordCount(String inputFile){
    File file = new File(inputFile);
    //try catch in case the file doesn't exist
    try {
        Scanner scan = new Scanner(file);
        //scanner will cut new words at non alpha numeric values
        scan.useDelimiter("\\W");
        int num = 0;
        //loop to look at each word in the file and hash it
        while (scan.hasNext()) {
          String s = scan.next();
          s = s.toLowerCase();
          if (s.length() > 0) {
            hashWord(new HashNode(s));
          }
        }
    }
    catch (FileNotFoundException e) {
        e.printStackTrace();
    }
  }

  //method to hash the word
  public void hashWord(HashNode newNode) {
    //gets a hash value for the word
    int hashValue = Math.abs(newNode.getWord().hashCode()) % hashTable.length;
    //looks to insert it into the hash table or at the end of the chain
    if(hashTable[hashValue] != null) {
      HashNode nodeptr = hashTable[hashValue];
      int counter = 1;
      boolean repeat = false;
      //check to find repeat or end of chain
      if (newNode.getWord().equals(hashTable[hashValue].getWord())) {
        repeat = true;
      }
      while(nodeptr.getNext() != null && !repeat){
        nodeptr = nodeptr.getNext();
        counter++;
        if (newNode.getWord().equals(nodeptr.getWord())){
          repeat = true;
        }
      }
      //if new it places it at the end of the chain, else increment the count
      if(!repeat){
        nodeptr.setNext(newNode);
      } else {
        nodeptr.setCount(nodeptr.getCount() + newNode.getCount());
      }
      //if the chain is too long it rehashes the table
      if(counter > tableSize){
        rehash();
      }
    } else {
      hashTable[hashValue] = newNode;
    }
  }

  //rehash function to make the table bigger
  public void rehash() {
    int oldSize = tableSize;
    HashNode[] oldTable = hashTable;
    tableSize = 2 * oldSize;
    hashTable = new HashNode[tableSize];
    for(int i = 0; i < oldSize; i++) {
      if(oldTable[i] != null) {
        HashNode prev = null;
        HashNode old = oldTable[i];
        while(old.getNext() != null){
          hashWord(old);
          prev = old;
          old = old.getNext();
          prev.setNext(null);
        }
        hashWord(old);
        old.setNext(null);
      }
    }
  }

  //create an output file
  public void writeFile(String outputFile) {
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
    catch(IOException e) {
      e.printStackTrace();
    }
    //try catch to scan the input file
    try {
      PrintWriter writer = new PrintWriter(oFile);
      int numEntry = 0;
      int numRows = 0;
      //loops to iterate over all values in the table
      for(int i = 0; i < tableSize; i++){
        if(hashTable[i] != null) {
          numRows++;
          HashNode nodeptr = hashTable[i];
          //write the word and count to the file and calculate chain length
          while(nodeptr.getNext() != null){
            writer.print("(" + nodeptr.getWord() + " " + nodeptr.getCount() + ")");
            numEntry++;
            nodeptr = nodeptr.getNext();
          }
          writer.println("(" + nodeptr.getWord() + " " + nodeptr.getCount() + ")");
          numEntry++;
        }
      }
      //print the length calculation
      writer.println("Average length of collisions = " + (double)(numEntry/numRows));
      writer.close();
    }
    catch (FileNotFoundException e) {
        e.printStackTrace();
    }
  }

  //inner class for Hash node values
  public class HashNode {

    //word in the node
    private String word;

    //count for the word
    private int count;

    //next node in linked list
    private HashNode next;

    //new word constructor
    public HashNode(String newWord){
      word = newWord;
      count = 1;
      next = null;
    }

    //old word constructor for rehash
    public HashNode(String newWord, int num, HashNode node){
      word = newWord;
      count = num;
      next = node;
    }

    //getter for the word
    public String getWord(){
      return word;
    }

    //getter for the count
    public int getCount(){
      return count;
    }

    //increment the count
    public void increment(){
      count++;
    }

    //set the count
    public void setCount(int newCount){
      count = newCount;
    }

    //getter for next
    public HashNode getNext(){
      return next;
    }

    //setter for the next
    public void setNext(HashNode nextNode){
      next = nextNode;
    }

  }
}
