/* *****************************************************************************
 *  Name:              Franklin Velasquez
 *  Last modified:     March, 2022
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        while (!StdIn.isEmpty()) {
            String nextWord = StdIn.readString();
            if (randomizedQueue.size() <= k) {
                randomizedQueue.enqueue(nextWord);
            }
            else {
                randomizedQueue.dequeue();
            }
        }

        for (String value : randomizedQueue) {
            StdOut.println(value);
        }
    }
}
