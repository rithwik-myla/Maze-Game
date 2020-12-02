package byow;

import javax.swing.*;
import java.util.*;

public class ArrayHeapMinPQ<T, D extends Number> implements ExtrinsicMinPQ<T> {
    private ArrayList<PriorityNode> minHeap;
    private HashMap<T, Integer> items;

    /*
    @ NaiveMinPQ.java
    This class is copied directly from there.
     */
    private class PriorityNode implements Comparable<PriorityNode> {
        private T item;
        private double priority;

        PriorityNode(T e, double p) {
            this.item = e;
            this.priority = p;
        }

        T getItem() {
            return item;
        }

        double getPriority() {
            return priority;
        }

        void setPriority(double priority) {
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityNode other) {
            if (other == null) {
                return -1;
            }
            return Double.compare(this.getPriority(), other.getPriority());
        }
    }

    public ArrayHeapMinPQ() {
        minHeap = new ArrayList<>();
        items = new HashMap<>();
        minHeap.add(null);
    }

    @Override
    public void add(T item, double priority) {
        if (contains(item)) {
            throw new IllegalArgumentException();
        }
        int inputIndex = minHeap.size();
        PriorityNode addition = new PriorityNode(item, priority);

        minHeap.add(addition);
        items.put(addition.getItem(), inputIndex);

        swim(inputIndex);
    }

    @Override
    public boolean contains(T item) {
        return items.containsKey(item);
    }

    @Override
    public T getSmallest() {
        if (size() <= 0) {
            throw new NoSuchElementException();
        }
        return minHeap.get(1).getItem();
    }

    @Override
    public T removeSmallest() {
        if (size() <= 0) {
            throw new NoSuchElementException();
        }
        T answer = getSmallest();
        smallSwapLastAndRemove();
        if (size() > 0) {
            sink(1);
        }
        return answer;
    }

    @Override
    public int size() {
        return minHeap.size() - 1;
    }

    @Override
    public void changePriority(T item, double priority) {
        if (!contains(item)) {
            throw new NoSuchElementException();
        }

        int itemIndex = items.get(item);
        double searchPriority = minHeap.get(itemIndex).priority;
        minHeap.get(itemIndex).priority = priority;
        if (priority > searchPriority) {
            sink(itemIndex);
        } else if (priority < searchPriority) {
            swim(itemIndex);
        }
    }




    /*
    @Lecture Slides for the following three methods.
    These were described explicitly in the slides referring to
    the Approach 3B (as described in the textbook).
    Returns the parent of the given PriorityNode for the first
    method. The second one returns the left child of the given
    PriorityNode. The third one returns the right child.
     */
    private int parent(int k) {
        return k / 2;
    }

    private int leftChild(int k) {
        if ((k * 2) >= minHeap.size()) {
            return 0;
        }
        return k * 2;
    }

    private int rightChild(int k) {
        if (((k * 2) + 1) >= minHeap.size()) {
            return 0;
        }
        return (k * 2) + 1;
    }

    /*
    @Lecture Slides
    The following method was described in the "A Deep Look
    at Approach 3" slide.
     */
    private void swim(int k) {
        if (k != 1) {
            if (minHeap.get(parent(k)).compareTo(minHeap.get(k)) > 0) {
                swap(k, parent(k));
                swim(parent(k));
            }
        }
    }

    /*
    This methods swaps the parent and the child in the arraylist.
     */
    private void swap(int child, int parent) {
        PriorityNode childNode = minHeap.get(child);
        PriorityNode parentNode = minHeap.get(parent);
        minHeap.set(child, parentNode);
        items.put(parentNode.getItem(), child);
        minHeap.set(parent, childNode);
        items.put(childNode.getItem(), parent);
    }

    private void sink(int k) {
        PriorityNode leftChild = minHeap.get(leftChild(k));
        PriorityNode rightChild = minHeap.get(rightChild(k));
        PriorityNode parent = minHeap.get(k);

        if (leftChild != null && leftChild.compareTo(rightChild) < 0) {
            if (parent.compareTo(leftChild) > 0) {
                swap(leftChild(k), k);
                sink(leftChild(k));
            }
        } else {
            if (parent.compareTo(rightChild) > 0) {
                swap(rightChild(k), k);
                sink(rightChild(k));
            }
        }
    }

    private void smallSwapLastAndRemove() {
        int lastIndex = size();
        PriorityNode firstItem = minHeap.get(1);
        items.remove(firstItem.getItem());
        PriorityNode lastItem = minHeap.get(lastIndex);
        minHeap.set(1, lastItem);
        minHeap.remove(lastIndex);
    }


}
