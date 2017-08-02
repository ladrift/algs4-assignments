import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    // 40N + 116 bytes (Deque)

    private class Node {   // 40 bytes (Node)
        // 16 bytes (object overhead)
        Item item; //  8 bytes (reference)
        Node prev; //  8 bytes (reference)
        Node next; //  8 bytes (reference)

        Node(Item item) {
            this.item = item;
            this.prev = null;
            this.next = null;
        }

        Node() {
            this.item = null;
            this.prev = null;
            this.next = null;
        }
    }
    // 16 bytes (object overhead)

    // 20 bytes (fields)
    private int size;       // 4 bytes (int)
    private Node sentFront; // 8 bytes (reference)
    private Node sentBack;  // 8 bytes (reference)

    // 80 bytes (sentinels)
    public Deque() {                           // construct an empty deque
        sentFront = new Node(); // 40 bytes (Node)
        sentBack = new Node();  // 40 bytes (Node)
        sentFront.next = sentBack;
        sentBack.prev = sentFront;

        this.size = 0;
    }

    public boolean isEmpty() {                // is the deque empty?
        return size == 0;
    }

    public int size() {                       // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {         // add the item to the front
        checkArgs(item);
        Node node = new Node(item);

        node.next = sentFront.next;
        node.prev = sentFront;

        sentFront.next.prev = node;
        sentFront.next = node;

        size += 1;
    }

    public void addLast(Item item) {          // add the item to the end
        checkArgs(item);
        Node node = new Node(item);

        node.next = sentBack;
        node.prev = sentBack.prev;

        sentBack.prev.next = node;
        sentBack.prev = node;

        size += 1;
    }

    public Item removeFirst() {               // remove and return the item from the front
        checkEmpty();
        Item removed = sentFront.next.item;

        sentFront.next = sentFront.next.next;
        sentFront.next.prev = sentFront;

        size -= 1;

        return removed;
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty.");
        }
    }

    public Item removeLast() {                // remove and return the item from the end
        checkEmpty();
        Item removed = sentBack.prev.item;

        sentBack.prev = sentBack.prev.prev;
        sentBack.prev.next = sentBack;

        size -= 1;

        return removed;
    }

    private class DequeIterator implements Iterator<Item> {

        Node curr = sentFront;

        @Override
        public boolean hasNext() {
            return curr.next != sentBack;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator is empty.");
            }

            curr = curr.next;
            return curr.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Can not remove on deque iterator.");
        }
    }

    public Iterator<Item> iterator() {        // return an iterator over items in order from front to end
        return new DequeIterator();
    }

    public static void main(String[] args) {  // unit testing (optional)
        Deque<String> d = new Deque<>();
        try {
            d.addFirst(null);
        } catch (IllegalArgumentException e) {
            StdOut.println("PASS: throw IllegalArgumentException when add null element to deque.");
        }

        d.addFirst("abc");
        d.addLast("xyz");

        try {
            d.removeFirst();
            d.removeLast();
            d.removeLast();
        } catch (NoSuchElementException e) {
            StdOut.println("PASS: throw NoSuchElementException when remove from empty deque.");
        }

        d.addLast("last");
        d.addFirst("middle");
        d.addFirst("first");
        Iterator<String> iterator = d.iterator();
        for (int i = 0; i < d.size(); i++) {
            StdOut.println(iterator.next());
        }

        try {
            iterator.next();
        } catch (NoSuchElementException e) {
            StdOut.println("PASS: throw NoSuchElementException when calling next() on iterator when there is no more items.");
        }

        try {
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            StdOut.println("PASS: throw UnsupportedOperationException when calling remove() on DequeIterator.");
        }
    }

    private void checkArgs(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Argument `item` can not be null.");
        }
    }
}