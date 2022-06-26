/* *****************************************************************************
 *  Name:              Franklin Velasquez
 *  Coursera
 *  Last modified:     March, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class RandomizedQueue<Item> implements Iterable<Item> {

    // Clever Idea: To avoid unused space on the array, add items in a circular way
    private Item[] queue;
    private int head;
    private int tail;
    private int itemsCount; // number of items in the list;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        head = -1;
        tail = -1;
        itemsCount = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return itemsCount <= 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return itemsCount;
    }

    private void resizeAndCopyArrayOfLength(int finalSize) {
        Item[] resizedQueue = (Item[]) new Object[finalSize];

        // iterate to the number of elements in the current queue
        for (int i = 0; i < this.itemsCount; i++) {

            // use % to handle circular behaviour
            int itemPosition = (head + i) % this.queue.length;
            // copy at the beginning of the new array
            resizedQueue[i] = this.queue[itemPosition];
        }

        this.queue = resizedQueue;
        this.head = 0;
        this.tail = itemsCount - 1;
    }

    // add the item at the end
    public void enqueue(Item item) {

        if (item == null)
            throw new IllegalArgumentException();

        if (itemsCount == queue.length) {
            resizeAndCopyArrayOfLength(queue.length * 2);
        }

        // Clever Idea: To avoid unused space on the array, add items in a circular way
        // to handle circular behaviour
        int position = (tail + 1) % queue.length;
        // add the item to the queue
        queue[position] = item;
        // update tail pointer to its new position
        tail = position;
        // update items count;
        itemsCount++;

        // handle init scenario
        if (itemsCount == 1)
            head = tail;
    }

    // remove and return a random item
    public Item dequeue() {
        // Clever Idea: to avoid holes in the queue while removing a random item,
        //              lets choose a random item and replace it with the head of the
        //              queue, then simply do a dequeue operation as always.

        if (isEmpty())
            throw new NoSuchElementException();

        // shuffle random selected item with first item
        int noElementSelected = StdRandom.uniform(itemsCount);
        int positionSelected = (head + noElementSelected) % queue.length; //handle circular behavior
        Item selectedItem = queue[positionSelected];
        queue[positionSelected] = queue[head];
        queue[head] = selectedItem;

        //Item selectedItem = queue[head]; // test normal scenario

        // dequeue with normal behavior in a queue, which is removing head
        queue[head] = null; // avoid loitering
        itemsCount--;

        if (itemsCount >= 1)
            head = (head + 1) % queue.length; // handle circular behavior
        if (isEmpty()) {
            head = -1;
            tail = -1;
        }

        if (itemsCount <= queue.length / 4) {
            // avoid queue to become a 0 length array
            int targetSize = Math.max(1, queue.length / 2);
            resizeAndCopyArrayOfLength(targetSize);
        }

        return selectedItem;
    }

    // return a random item (but not remove it)
    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();

        int noElementSelected = StdRandom.uniform(itemsCount);
        int positionSelected = (head + noElementSelected)
                % queue.length; // handle circular behavior
        Item selectedItem = queue[positionSelected];
        return selectedItem;
    }

    private void printQueue() {
        System.out.println("\nPrinting in Order - itemsCount: " + itemsCount + " queue.length : "
                                   + queue.length);

        for (int i = 0; i < itemsCount; i++) {
            int position = (head + i) % queue.length;
            System.out.print(queue[position] + " , ");
        }

        System.out.println("\nPrinting queue as it is: ");

        for (int i = 0; i < queue.length; i++) {
            System.out.print(queue[i] + " , ");
        }
        System.out.println();
    }


    // return and independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }

    private class RandomIterator implements Iterator<Item> {

        private Item[] randomOrderList;
        private int currentPosition;

        public RandomIterator() {
            this.randomOrderList = (Item[]) new Object[itemsCount];
            this.currentPosition = 0;
            for (int i = 0; i < itemsCount; i++) {
                int position = (head + i) % queue.length; // handle circular behaviour
                this.randomOrderList[i] = queue[position];
            }
            StdRandom.shuffle(randomOrderList);
        }

        public Item next() {
            if (isEmpty() || currentPosition >= this.randomOrderList.length)
                throw new NoSuchElementException();

            Item currentItem = this.randomOrderList[currentPosition];
            currentPosition++;
            return currentItem;
        }

        public boolean hasNext() {
            return currentPosition < this.randomOrderList.length;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        System.out.println("Empty randomized queue: ");

        randomizedQueue.printQueue();

        System.out.println("\nAdding 1 item");
        randomizedQueue.enqueue("a");
        randomizedQueue.printQueue();

        System.out.println("\nNow there should be 2 items");
        randomizedQueue.enqueue("b");
        randomizedQueue.printQueue();

        System.out.println("\nNow there should be 3 items");
        randomizedQueue.enqueue("c");
        randomizedQueue.printQueue();

        System.out.println("\nNow there should be 4 items");
        randomizedQueue.enqueue("d");
        randomizedQueue.printQueue();

        System.out.println("\nDequeue 1 item");
        String item = randomizedQueue.dequeue();
        System.out.println("\nDequeued item: " + item);
        randomizedQueue.printQueue();

        System.out.println("\nDequeue 1 item");
        String item2 = randomizedQueue.dequeue();
        System.out.println("\nDequeued item: " + item2);
        randomizedQueue.printQueue();


        System.out.println("\nNow there should be 3 items");
        randomizedQueue.enqueue("e");
        randomizedQueue.printQueue();

        System.out.println("\nNow there should be 4 items");
        randomizedQueue.enqueue("f");
        randomizedQueue.printQueue();

        System.out.println("\nDequeue 1 item");
        String item3 = randomizedQueue.dequeue();
        System.out.println("\nDequeued item: " + item3);
        randomizedQueue.printQueue();

        System.out.println("\nDequeue 1 item");
        String item4 = randomizedQueue.dequeue();
        System.out.println("\nDequeued item: " + item4);
        randomizedQueue.printQueue();

        System.out.println("\nDequeue 1 item");
        String item5 = randomizedQueue.dequeue();
        System.out.println("\nDequeued item: " + item5);
        randomizedQueue.printQueue();

        System.out.println("\nDequeue 1 item");
        String item6 = randomizedQueue.dequeue();
        System.out.println("\nDequeued item: " + item6);
        randomizedQueue.printQueue();

        System.out.println("\nAdding 1 item");
        randomizedQueue.enqueue("a");
        randomizedQueue.printQueue();

        System.out.println("\nNow there should be 2 items");
        randomizedQueue.enqueue("b");
        randomizedQueue.printQueue();

        System.out.println("\nNow there should be 3 items");
        randomizedQueue.enqueue("c");
        randomizedQueue.printQueue();

        System.out.println("\nNow there should be 4 items");
        randomizedQueue.enqueue("d");
        randomizedQueue.printQueue();

        System.out.println("\nPrinting in random order");
        for (String value : randomizedQueue) {
            System.out.print(value + " , ");
        }

        System.out.println("\nPrinting in random order");
        for (String value : randomizedQueue) {
            System.out.print(value + " , ");
        }
    }
}
