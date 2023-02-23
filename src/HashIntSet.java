


/**
 * A HashIntSet represents a set of integers using a hash table as the internal data structure.
 * The hashtable uses separate chaining (a linked list in each hash bucket index) to resolve collisions.
 * @author Marty Stepp
 * @version 2019/09/19
 * Additional methods added by Taylor Merwin
 * As seen on UW Practice-It!
 */
public class HashIntSet {
  private static final double MAX_LOAD = 0.5;   // load factor on which to rehash

  private Node[] elementData;
  private int size;

  // Constructs a new empty set of integers.
  public HashIntSet() {
    elementData = (Node[]) new HashIntSet.Node[10];
    size = 0;
  }

  // Constructs a new set of integers.
  @SuppressWarnings("unchecked")
  public HashIntSet(int... values) {
    this();
    for (int value : values) {
      add(value);
    }
  }

  // Adds the given value to this set,
  // if it was not already contained in the set.
  public void add(int value) {
    // linear probing to find proper index
    if (!contains(value)) {
      int h = hash(value);
      Node newNode = new Node(value);
      newNode.next = elementData[h];
      elementData[h] = newNode;
      size++;
    }

    // resize if necessary
    if (loadFactor() > MAX_LOAD) {
      rehash();
    }
  }

  // Returns true if o refers to another HashIntSet with the same elementData as this set.
  public boolean equals(Object o) {
    return equalsIgnoringSize(o) && size == ((HashIntSet) o).size();
  }

  // Returns true if o refers to another HashIntSet with the same elementData as this set,
  // ignoring the value of the size field.
  public boolean equalsIgnoringSize(Object o) {
    if (o instanceof HashIntSet) {
      HashIntSet other = (HashIntSet) o;
      for (Node front : elementData) {
        Node current = front;
        while (current != null) {
          if (!other.contains(current.data)) {
            return false;
          }
          current = current.next;
        }
      }
      for (Node front : other.elementData) {
        Node current = front;
        while (current != null) {
          if (!this.contains(current.data)) {
            return false;
          }
          current = current.next;
        }
      }
      return true;
    } else {
      return false;
    }
  }



  // Returns whether the given value is found in this set.
  public boolean contains(int value) {
    // linear probing to find proper index
    int h = hash(value);
    Node current = elementData[h];
    while (current != null) {
      if (current.data == value) {
        return true;
      }
      current = current.next;
    }
    return false;
  }

  // Returns true if there are no elementData in this set.
  public boolean isEmpty() {
    return size == 0;
  }

  // Returns the hash table's "load factor", its ratio of size to capacity.
  public double loadFactor() {
    return (double) size / elementData.length;
  }

  // Removes the given element value from this set,
  // if it was found in the set.
  public void remove(int value) {
    // linear probing to find proper index
    int h = hash(value);

    if (elementData[h] != null) {
      // front case
      if (elementData[h].data == value) {
        elementData[h] = elementData[h].next;
        size--;
      } else {
        // non-front case
        Node current = elementData[h];
        while (current.next != null &&
            current.next.data != value) {
          current = current.next;
        }

        // current.next == null
        // || current.next.data == value
        if (current.next != null) {
          current.next = current.next.next;
          size--;
        }
      }
    }
  }

  // Returns the number of elementData in this set.
  public int size() {
    return size;
  }

  // Returns a text representation of this set.
  public String toString() {
    String result = "[";
    boolean first = true;
    for (Node node : elementData) {
      Node current = node;
      while (current != null) {
        if (!first) {
          result += ", ";
        }
        result += current.data;
        first = false;
        current = current.next;
      }
    }
    result += "]";
    return result;
  }

  // Debugging helper that prints the inner hash table.
  public void debug() {
    System.out.println("debug() output:");
    System.out.printf("index   data\n");
    for (int i = 0; i < elementData.length; i++) {
      System.out.printf("%5d   ", i);
      Node node = elementData[i];
      if (node == null) {
        System.out.printf("%6s\n", "null");
      } else {
        boolean first = true;
        while (node != null) {
          if (!first) {
            System.out.print("  --> ");
          }
          System.out.printf("%8s", node.data);
          node = node.next;
          first = false;
        }
        System.out.println();
      }
    }
    System.out.printf("size   %d\n\n", size);
  }

  // hash function for mapping values to indexes
  private int hash(int value) {
    return Math.abs(value) % elementData.length;
  }

  // Resizes the hash table to twice its original capacity.
  @SuppressWarnings("unchecked")
  private void rehash() {
    Node[] newelementData = (Node[]) new HashIntSet.Node[2 * elementData.length];
    Node[] old = elementData;
    elementData = newelementData;
    size = 0;
    for (Node node : old) {
      while (node != null) {
        add(node.data);
        node = node.next;
      }
    }
  }


  private class Node {
    public int data;
    public Node next;

    public Node(int data) {
      this.data = data;
    }
  }

// YOUR CODE GOES HERE

  /**
   *  This method accepts another HashIntSet as a parameter and adds all elements from that set
   *  into the current set, if they are not already present.
   *  For example, if a set s1 contains [1, 2, 3] and another set s2 contains [1, 7, 3, 9],
   *  the call of s1.addAll(s2); would change s1 to store [1, 2, 3, 7, 9] in some order.
   */
  public void addAll (HashIntSet s2) {
    //do not modify the set that's passed in
    //must be O(n)

    for (int i = 0; i < s2.elementData.length; i++) {
      Node node = s2.elementData[i];

      while(node != null) {
        this.add(node.data);
        node = node.next;
      }
    }

  }

  public boolean containsAll (HashIntSet s2){

    //iterate through the array
    for (int i = 0; i < s2.elementData.length; i++) {
      Node node = s2.elementData[i];

      while(node != null) {
        if (! this.contains(node.data)) {
          return false;
        }
        node = node.next;
      }
    }
    return true;
  }

  public boolean equals (HashIntSet s2) {
    return this.equalsIgnoringSize(s2);
  }

  /**
   * Ensure this hashSet does not contain any of the elements from the other set
   */
  public void removeAll (HashIntSet s2) {

    for (int i = 0; i < s2.elementData.length; i++) {
      Node node = s2.elementData[i];

      while(node != null) {
        if (this.contains(node.data)) {
          this.remove(node.data);
        }
        node = node.next;
      }
    }
  }

  /**
   * removes all elements from this set that are not contained in the other set
   */
  public void retainAll (HashIntSet s2) {
    for (int i = 0; i < this.elementData.length; i++) {
      Node node = this.elementData[i];

      while (node != null) {
        if (! s2.contains(node.data)) {
          this.remove(node.data);
        }
        node = node.next;
      }
    }
  }


}