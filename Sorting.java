import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
//Class that contains the sorting methods
public class Sorting {

  public static void main(String[] args) {
  }

  static void shuffleArray(int[] ar)
  {
    // If running on Java 6 or older, use `new Random()` on RHS here
    Random rnd = ThreadLocalRandom.current();
    for (int i = ar.length - 1; i > 0; i--)
    {
      int index = rnd.nextInt(i + 1);
      // Simple swap
      int a = ar[index];
      ar[index] = ar[i];
      ar[i] = a;
    }
  }

  //heap Sort
  public static void heapSort(int[] arr) {
    int n = arr.length;
    //create the initial heap
    for (int i = n / 2 - 1; i >= 0; i--){
      heapify(arr, n, i);
    }
    //Take the root of the max heap and put it at the end
    for (int i=n-1; i>=0; i--) {
      // Move current root to end
      int temp = arr[0];
      arr[0] = arr[i];
      arr[i] = temp;
      //heapify the rest of the array
      heapify(arr, i, 0);
    }
  }

  //helper methods for heapSort to build the heap
  public static void heapify(int arr[], int n, int i) {
    // Initialize largest as root
    int largest = i;
    int left = 2*i + 1;
    int right = 2*i + 2;
    //If left child is larger than root
    if (left < n && arr[left] > arr[largest]) {
      largest = left;
    }
    //If right child is larger than largest so far
    if (right < n && arr[right] > arr[largest]){
      largest = right;
    }
    //If largest is not root
    if (largest != i) {
      int swap = arr[i];
      arr[i] = arr[largest];
      arr[largest] = swap;
      // Recursively heapify the affected sub-tree
      heapify(arr, n, largest);
    }
  }


  //quick Sort
  public static void quickSort(int[] arr){
    qSortHelper(arr, 0, arr.length - 1);
  }

  //helper methods for quickSort
  public static void qSortHelper(int[] arr, int low, int high) {
    int i = low;
    int j = high;
    //Set the pivot as the middle of the list
    int pivot = arr[low + (high-low)/2];
    // Divide into two lists
    while (i <= j) {
    //If the value is to the left in the array and is smaller than the pivot, move to the next element
      while (arr[i] < pivot) {
        i++;
      }
      //If the value is to the right in the array and is larger than the pivot, move to the next element
      while (arr[j] > pivot) {
        j--;
      }
      //If there is a value in the left list which is larger than the pivot and a value in the right list
      //which is smaller than the pivot, then swap the values and move i and j to the next elementa
      if (i <= j) {
        //switch numbers i and j
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        i++;
        j--;
      }
    }
    //Recursion to quick sort the top and bottom halves
    if (low < j) {
      qSortHelper(arr, low, j);
    }
    if (i < high) {
      qSortHelper(arr, i, high);
    }
  }

  //merge sort
  public static void mergeSort(int[] arr){
    int[] from = arr;
    int[] to = new int[arr.length];
    //starts with small blocks and scales up
    for (int blockSize = 1; blockSize < arr.length; blockSize = blockSize*2) {
      //chooses the start for the blocks
      for (int start = 0; start < arr.length; start = start + (2 * blockSize)) {
        merge(from, to, start, start + blockSize, start + 2 * blockSize);
      }
      //swaping with temp, to and from
      int[] temp = from;
      from = to;
      to = temp;
    }
    if (arr != from)
   // copy from temp to arr
   for (int k = 0; k < arr.length; k++)
      arr[k] = from[k];
  }

  //helper method for mergeSort
  private static void merge(int[] from, int[] to, int low, int mid, int high) {
    //adjust the mid and high values if they are too big
   if (mid > from.length) {
     mid = from.length;
   }
   if (high > from.length) {
     high = from.length;
   }
   int i = low;
   int j = mid;
   //populates the to array with values in the from array
   for (int k = low; k < high; k++) {
      if(i == mid) {
        to[k] = from[j++];
      } else if(j == high) {
        to[k] = from[i++];
      } else if(from[j] < from[i]) {
        to[k] = from[j++];
      } else {
        to[k] = from[i++];
      }
   }
}

}
