import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int size;
    private int first;
    private int last;
    private static final int INITIAL_CAPACITY = 8;

    /**
     * construct an empty randomized queue
     */
    public RandomizedQueue() {
        items = (Item[]) new Object[INITIAL_CAPACITY];
        // [first, last) contains the elements of RandomizedQueue object.
        first = 0;
        last = 0;
        size = 0;
    }

    /**
     * is the queue empty?
     *
     * @return a boolean indicates whether the queue is empty or not
     */
    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {                        // return the number of items on the queue
        return size;
    }

    /**
     * add the item
     *
     * @param item the item added to queue
     */
    public void enqueue(Item item) {
        checkArgs(item);
        if (size == items.length) {
            resize(2 * items.length);
        }
        items[last] = item;
        last = plusOne(last);
        size += 1;
    }

    /**
     * remove and return a random item
     *
     * @return the removed item
     */
    public Item dequeue() {
        checkEmpty();
        if (items.length >= 4 && size == items.length / 4) {
            resize(items.length / 2);
        }

        int idx = Math.floorMod(StdRandom.uniform(size) + first, items.length);
        Item removed = items[idx];

        last = minusOne(last);
        if (idx != last) {
            items[idx] = items[last];
        }
        items[last] = null;
        size -= 1;

        return removed;
    }

    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        int newIdx = 0;
//        for (int i = first; i != last; i = plusOne(i)) {
//            newItems[newIdx++] = items[i];
//        }
        for (int i = 0; i < size; i++) {
            newItems[i] = getItem(i);
        }
        items = newItems;
        first = 0;
        last = size;
    }

    private Item getItem(int idx) {
        return items[Math.floorMod(first + idx, items.length)];
    }

    /**
     * return (but do not remove) a random item
     *
     * @return a random sample of items
     */
    public Item sample() {
        checkEmpty();
        int idx = StdRandom.uniform(size);
        return items[idx];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        Item[] randomItems;

        int idx = 0;

        public RandomizedQueueIterator() {
            randomItems = (Item[]) new Object[size];

            int idx = 0;
            for (int i = 0; i < size; i++) {
                randomItems[i] = getItem(i);
            }

            StdRandom.shuffle(randomItems);
        }

        @Override
        public boolean hasNext() {
            return idx != size;
        }

        @Override
        public Item next() {
            if (idx == randomItems.length) {
                throw new NoSuchElementException("No more items to return.");
            }
            return randomItems[idx++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Can not call remove on RandomizedQueueIterator object.");
        }


    }

    public Iterator<Item> iterator() {         // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args) {   // unit testing (optional)
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("AA");
        queue.enqueue("BB");
        queue.enqueue("BB");
        queue.enqueue("BB");
        queue.enqueue("BB");
        queue.enqueue("BB");
        queue.enqueue("CC");
        queue.enqueue("CC");

        queue.dequeue();

        Iterator<String> iter = queue.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    private void checkEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException("RandomizedQueue is empty.");
        }
    }

    private void checkArgs(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("cannot insert null in to RandomizedQueue object.");
        }
    }

    private int plusOne(int idx) {
        idx += 1;
        if (idx == items.length) {
            idx = 0;
        }

        return idx;
    }

    private int minusOne(int idx) {
        idx -= 1;
        if (idx < 0) {
            idx = items.length - 1;
        }

        return idx;
    }
}
