//Node Class for the Huffman Encoder

public class HuffmanNode implements Comparable<HuffmanNode> {

  //left child
  private HuffmanNode left;

  //right child
  private HuffmanNode right;

  //frequency of the character or the total of the children
  private int frequency;

  //character stored in the node
  private Character inChar;

  //Constructor for the huffman node
  public HuffmanNode(Character inChar, int frequency, HuffmanNode left, HuffmanNode right){
    this.inChar = inChar;
    this.frequency = frequency;
    this.left = left;
    this.right = right;
  }

  //getter for the character
  public Character getInChar(){
    return inChar;
  }

  //getter for the frequency
  public int getFrequency(){
    return frequency;
  }

  //getter for the left child
  public HuffmanNode getLeft(){
    return left;
  }

  //getter for the right child
  public HuffmanNode getRight(){
    return right;
  }

  //setter for the left node
  public void setLeft(HuffmanNode node){
    left = node;
  }

  //setter for the right node
  public void setRight(HuffmanNode node){
    right = node;
  }

  //method to compare nodes based on frequencies
  public int compareTo(HuffmanNode node) {
    //this compares in ascending order
		int compareFreq = ((HuffmanNode) node).getFrequency();
		return this.frequency - compareFreq;
	}
}
