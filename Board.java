import dsa.Inversions;
import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

// A data type to represent a board in the 8-puzzle game or its generalizations.
public class Board {
    private int n; // the size of the array
    private int[][] tiles; // the board
    private int hamming; // hamming distance
    private int manhattan; // manhattan distance
    private int blankPos; // the position of the zero

    // Constructs a board from an n x n array; tiles[i][j] is the tile at row i and column j, with 0
    // denoting the blank tile.
    public Board(int[][] tiles) {
        this.n = tiles.length;
        this.tiles = tiles;
        this.hamming = 0;
        this.manhattan = 0;

        // hamming distance
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != i * n + j + 1 && this.tiles[i][j] != 0) {
                    this.hamming++;
                    int iDifference = (tiles[i][j] - 1) / n;
                    int jDifference = (tiles[i][j] - 1) - (iDifference * n);
                    this.manhattan += Math.abs(i - iDifference) + Math.abs(j - jDifference);
                    if (tiles[i][j] == 0) {
                        blankPos = n * i + j + 1;
                    }
                }
            }
        }

//        // manhattan distance
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                if (this.tiles[i][j] != i * n + j + 1 && tiles[i][j] != 0) {
//                    int iDifference = (tiles[i][j] - 1) / n;
//                    int jDifference = (tiles[i][j] - 1) - (iDifference * n);
//                    this.manhattan += Math.abs(i - iDifference) + Math.abs(j - jDifference);
//                }
//            }
//        }
//
//        // blank position
//        for (int i = 0; i < tiles.length; i++) {
//            for (int j = 0; j < tiles[0].length; j++) {
//                if (tiles[i][j] == 0) {
//                    blankPos = n * i + j + 1;
//                }
//            }
//        }
    }

    // Returns the size of this board.
    public int size() {
        return n;
    }

    // Returns the tile at row i and column j of this board.
    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    // Returns Hamming distance between this board and the goal board.
    public int hamming() {
        return this.hamming;
    }

    // Returns the Manhattan distance between this board and the goal board.
    public int manhattan() {
        return this.manhattan;
    }

    // Returns true if this board is the goal board, and false otherwise.
    public boolean isGoal() {
        return blankPos == n * n && hamming == 0;
    }

    // Returns true if this board is solvable, and false otherwise.
    public boolean isSolvable() {
        int[] arrTiles = new int[(n * n) - 1];
        int inc = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] != 0) {
                    arrTiles[inc++] = this.tiles[i][j];
                }
            }
        }

        int inversion = (int) Inversions.count(arrTiles);
        if (n % 2 == 1) {
            if (inversion % 2 == 1) {
                return false;
            }
            return true;
        } else {
            int sum = inversion + ((blankPos - 1) / n);
            if (sum % 2 == 0) {
                return false;
            }
            return true;
        }
    }

    // Returns an iterable object containing the neighboring boards of this board.
    public Iterable<Board> neighbors() {
        LinkedQueue<Board> q = new LinkedQueue<Board>();
        int[][] clone;
        int i = (blankPos - 1) / n;
        int j = (blankPos - 1) % n;
        int x;
        if (i + 1 > 0 && i + 1 < n) {
            clone = cloneTiles();
            x = clone[i][j];
            clone[i][j] = clone[i + 1][j];
            clone[i + 1][j] = x;
            Board board = new Board(clone);
            q.enqueue(board);
        }
        if (i - 1 >= 0 && i - 1 < n - 1) {
            clone = cloneTiles();
            x = clone[i][j];
            clone[i][j] = clone[i - 1][j];
            clone[i - 1][j] = x;
            Board board = new Board(clone);
            q.enqueue(board);
        }
        if (j + 1 > 0 && j + 1 < n) {
            clone = cloneTiles();
            x = clone[i][j];
            clone[i][j] = clone[i][j + 1];
            clone[i][j + 1] = x;
            Board board = new Board(clone);
            q.enqueue(board);
        }
        if (j - 1 >= 0 && j - 1 < n - 1) {
            clone = cloneTiles();
            x = clone[i][j];
            clone[i][j] = clone[i][j - 1];
            clone[i][j - 1] = x;
            Board board = new Board(clone);
            q.enqueue(board);
        }
        return q;
    }

    // Returns true if this board is the same as other, and false otherwise.
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Board tmp = (Board) other;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (this.tiles[i][j] != tmp.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Returns a string representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2s", tiles[i][j] == 0 ? " " : tiles[i][j]));
                if (j < n - 1) {
                    s.append(" ");
                }
            }
            if (i < n - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Returns a defensive copy of tiles[][].
    private int[][] cloneTiles() {
        int[][] clone = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                clone[i][j] = tiles[i][j];
            }
        }
        return clone;
    }

    // Unit tests the data type. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        StdOut.printf("The board (%d-puzzle):\n%s\n", n, board);
        String f = "Hamming = %d, Manhattan = %d, Goal? %s, Solvable? %s\n";
        StdOut.printf(f, board.hamming(), board.manhattan(), board.isGoal(), board.isSolvable());
        StdOut.println("Neighboring boards:");
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
            StdOut.println("----------");
        }
    }
}
