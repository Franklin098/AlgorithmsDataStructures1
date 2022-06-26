/* *****************************************************************************
 *  Name:              Franklin Velasquez
 **************************************************************************** */

import java.util.ArrayList;

public final class Board {

    private final int[][] board;
    private final int N;
    private int blankRow;
    private int blankCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        // the tiles in the input are not in order, 0 means empty
        this.N = tiles.length;
        this.board = new int[N][N];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                this.board[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    this.blankRow = i;
                    this.blankCol = j;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder representation = new StringBuilder();
        representation.append(this.board.length + "\n");

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                int tileValue = this.board[i][j];
                representation.append(tileValue + (j == this.board.length - 1 ? "\n" : " "));
            }
        }
        return representation.toString();
    }

    // board dimension n
    public int dimension() {
        return this.N;
    }

    private int getGoalTileAtPosition(int row, int col) {
        // (2,0) -> 7  (0,0) -> 1  (0,2) -> 3  (2,1) -> 8  (2,2) -> 0
        if (row == N - 1 && col == N - 1) {
            return 0;
        }
        return row * this.board.length + (col + 1);
    }

    // number of tiles out of place
    public int hamming() {
        int outOfPlaceCount = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (i == N - 1 && j == N - 1) {
                    // do not eval last position (should be empty)
                    break;
                }
                int tileValue = this.board[i][j];
                int goalValue = getGoalTileAtPosition(i, j);
                if (tileValue != goalValue) {
                    outOfPlaceCount++;
                }
            }
        }
        return outOfPlaceCount;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sumManhattan = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {

                int tileValue = this.board[i][j];
                if (tileValue == 0) {
                    // do not eval empty tile
                    continue;
                }

                // get goal coordinates for tileValue
                int goalRow = (int) Math.ceil(tileValue / (double) N) - 1;
                int goalColumn = tileValue - goalRow * N - 1;

                // calculate  Manhattan Distance
                int rowDiff = Math.abs(goalRow - i);
                int colDiff = Math.abs(goalColumn - j);
                int manhattan = rowDiff + colDiff;
                sumManhattan += manhattan;
            }
        }
        return sumManhattan;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        if (N != board1.N) {
            return false;
        }

        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                int tileValue = this.board[i][j];
                int otherValue = board1.board[i][j];
                if (tileValue != otherValue) {
                    return false;
                }
            }
        }
        return true;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    private Board getNeighbor(int sourceRow, int sourceCol, int destinationRow,
                              int destinationCol) {
        int[][] copyTiles = new int[N][N];
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                copyTiles[i][j] = this.board[i][j];
            }
        }
        int temp = copyTiles[sourceRow][sourceCol];
        copyTiles[sourceRow][sourceCol] = copyTiles[destinationRow][destinationCol];
        copyTiles[destinationRow][destinationCol] = temp;
        return new Board(copyTiles);
    }

    private boolean isValidCoordinate(int row, int col) {
        if (row >= this.N || row < 0 || col >= this.N || col < 0) {
            return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<>();
        // upper position
        if (isValidCoordinate(blankRow - 1, blankCol)) {
            boards.add(getNeighbor(blankRow, blankCol, blankRow - 1, blankCol));
        }
        // bottom
        if (isValidCoordinate(blankRow + 1, blankCol)) {
            boards.add(getNeighbor(blankRow, blankCol, blankRow + 1, blankCol));
        }
        // left
        if (isValidCoordinate(blankRow, blankCol - 1)) {
            boards.add(getNeighbor(blankRow, blankCol, blankRow, blankCol - 1));
        }
        // right
        if (isValidCoordinate(blankRow, blankCol + 1)) {
            boards.add(getNeighbor(blankRow, blankCol, blankRow, blankCol + 1));
        }

        return boards;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int sourceRow = -1;
        int sourceCol = -1;
        int targetRow = 0;
        int targetCol = 0;
        outerloop:
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i != this.blankRow || j != this.blankCol) {
                    if (sourceRow != -1 && sourceCol != -1) {
                        targetRow = i;
                        targetCol = j;
                        break outerloop;
                    }
                    sourceRow = i;
                    sourceCol = j;
                }
            }
        }

        return getNeighbor(sourceRow, sourceCol, targetRow, targetCol);
    }

    public static void main(String[] args) {
        int[][] test = { { 1, 0, 3 }, { 4, 2, 5 }, { 7, 8, 6 } };
        Board board1 = new Board(test);
        System.out.println(board1.toString());
        System.out.println("board1 hamming: " + board1.hamming());
        System.out.println("board2 manhattan: " + board1.manhattan());
        System.out.println("board1 neighbors: ");
        Iterable<Board> boardIterable = board1.neighbors();
        boardIterable.forEach(board -> {
            System.out.println(board.toString());
        });

        System.out.println("======================");
        int[][] test2 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        int[][] test3 = { { 8, 1, 3 }, { 4, 0, 2 }, { 7, 6, 5 } };
        Board board2 = new Board(test2);
        Board board3 = new Board(test3);
        System.out.println(board2.toString());
        System.out.println("board2 hamming: " + board2.hamming());
        System.out.println("board2 manhattan: " + board2.manhattan());
        System.out.println("board2 == board3 ? : " + board2.equals(board3));
        System.out.println("board2 == board1 ? : " + board2.equals(board1));
        System.out.println("board2 neighbors: ");
        boardIterable = board2.neighbors();
        boardIterable.forEach(board -> {
            System.out.println(board.toString());
        });
        System.out.println("board2 twin: " + board2.twin());
    }
}
