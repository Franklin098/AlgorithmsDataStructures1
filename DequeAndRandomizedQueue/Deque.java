/* *****************************************************************************
 *  Name:              Franklin Velasquez
 *  Last modified:     March, 2022
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {

    private class Node {

        public Node next;
        public Node previous;
        private Item item;

        public Node(Item item) {
            this.item = item;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    // construct and empty deque
    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node nodeToAdd = new Node(item);
        if (isEmpty()) {
            head = nodeToAdd;
            tail = nodeToAdd;
        }
        else {
            nodeToAdd.next = head;
            head.previous = nodeToAdd;
            head = nodeToAdd;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        Node nodeToAdd = new Node(item);
        if (isEmpty()) {
            head = nodeToAdd;
            tail = nodeToAdd;
        }
        else {
            tail.next = nodeToAdd;
            nodeToAdd.previous = tail;
            tail = nodeToAdd;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException();

        Node previousHead = head;
        head = previousHead.next;
        previousHead.next = null;
        if (head != null)
            head.previous = null;

        // update size
        size--;

        // to avoid a ghost reference
        if (size == 0)
            tail = null;

        return previousHead.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException();

        Node previousTail = tail;
        tail = previousTail.previous;
        previousTail.previous = null;
        if (tail != null)
            tail.next = null;

        // update size
        size--;

        // to avoid a ghost reference
        if (size == 0)
            head = null;

        return previousTail.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {

        return new Iterator<Item>() {

            Node currentNode = head;

            public boolean hasNext() {
                return currentNode != null;
            }

            public Item next() {
                if (currentNode == null)
                    throw new NoSuchElementException();
                Node aux = currentNode;
                currentNode = currentNode.next;
                return aux.item;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }


    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(4);
        deque.addLast(5);

        for (Integer value : deque) {
            System.out.print(value + " -> ");
        }

        System.out.println("\n===============REMOVING ===============");

        Integer last = deque.removeLast();

        System.out.println("\nLast removed: " + last);

        for (Integer value : deque) {
            System.out.print(value + " -> ");
        }

        Integer first = deque.removeFirst();

        System.out.println("\nFirst removed: " + first);

        for (Integer value : deque) {
            System.out.print(value + " -> ");
        }

        System.out.println("\n " + deque.size() + " remaining... removing all of them");

        deque.removeFirst();
        deque.removeLast();
        deque.removeFirst();

        System.out.println("Size after removing all of them: " + deque.size());

        for (Integer value : deque) {
            System.out.print(value + " -> ");
        }

        System.out.println("\n Add all of them again");

        deque.addFirst(3);
        deque.addFirst(2);
        deque.addFirst(1);
        deque.addLast(4);
        deque.addLast(5);

        for (Integer value : deque) {
            System.out.print(value + " -> ");
        }
    }


}

