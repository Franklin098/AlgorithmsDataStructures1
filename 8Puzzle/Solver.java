/* *****************************************************************************
 *  Name:              Franklin Velasquez
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private int noMoves;
    // to save items in reverse order wisely use a stack
    private Stack<Board> solutionBoards;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.noMoves = -1;
        this.solutionBoards = null;
        searchAStar(initial);
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int moves;
        private SearchNode previousNode;
        private int manhattan;

        SearchNode(Board board, int moves, SearchNode previousNode) {
            this.board = board;
            this.moves = moves;
            this.previousNode = previousNode;
            this.manhattan = this.board.manhattan();
        }

        public int compareTo(SearchNode that) {
            int currentPriority = this.manhattan + this.moves;
            int thatPriority = that.manhattan + that.getMoves();
            if (currentPriority > thatPriority) {
                return 1;
            }
            else if (currentPriority < thatPriority) {
                return -1;
            }
            return 0;
        }

        public SearchNode getPreviousNode() {
            return this.previousNode;
        }

        public Board getBoard() {
            return this.board;
        }

        public int getMoves() {
            return this.moves;
        }

    }

    private void searchAStar(Board initial) {
        // main
        MinPQ<SearchNode> mainPQ = new MinPQ<>();
        SearchNode root = new SearchNode(initial, 0, null);
        mainPQ.insert(root);
        boolean isMainSolved = false;
        // twin
        MinPQ<SearchNode> twinPQ = new MinPQ<>();
        twinPQ.insert(new SearchNode(initial.twin(), 0, null));
        boolean isTwinSolved = false;

        while (!isMainSolved && !isTwinSolved) {
            // main
            SearchNode mainNode = mainPQ.delMin();
            if (mainNode.getBoard().isGoal()) {
                isMainSolved = true;
                this.solutionBoards = new Stack<>();
                this.noMoves = mainNode.getMoves();
                SearchNode temp = mainNode;
                while (temp != null) {
                    // to save items in reverse order wisely use a stack
                    this.solutionBoards.push(temp.getBoard());
                    temp = temp.getPreviousNode();
                }
                break;
            }
            Iterable<Board> mainNeighbors = mainNode.getBoard().neighbors();
            for (Board neighbor : mainNeighbors) {
                if (mainNode.getPreviousNode() != null && neighbor
                        .equals(mainNode.getPreviousNode().getBoard())) {
                    continue;
                }
                SearchNode neighborNode = new SearchNode(neighbor, mainNode.getMoves() + 1,
                                                         mainNode);
                mainPQ.insert(neighborNode);
            }

            // twin
            SearchNode twinNode = twinPQ.delMin();
            if (twinNode.getBoard().isGoal()) {
                isTwinSolved = true;
            }
            Iterable<Board> twinNeighbors = twinNode.getBoard().neighbors();
            for (Board twinNeighbor : twinNeighbors) {
                if (twinNode.getPreviousNode() != null && twinNeighbor
                        .equals(twinNode.getPreviousNode().getBoard())) {
                    continue;
                }
                SearchNode twinSearchNode = new SearchNode(twinNeighbor, twinNode.getMoves() + 1,
                                                           twinNode);
                twinPQ.insert(twinSearchNode);
            }

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return this.noMoves != -1;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return this.noMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return this.solutionBoards;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
