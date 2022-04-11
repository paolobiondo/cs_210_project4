import dsa.LinkedStack;
import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;

// A data type that implements the A* algorithm for solving the 8-puzzle and its generalizations.
public class Solver {
    private int moves; // Minimum number of moves needed to solve the initial board
    private LinkedStack<Board> solution; // Sequence of boards from the initial board

    // Finds a solution to the initial board using the A* algorithm.
    public Solver(Board board) {
        if (board == null) {
            throw new NullPointerException("board is null");
        }
        if (!board.isSolvable()) {
            throw new IllegalArgumentException("board is unsolvable");
        }

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(board, 0, null));
        int incMoves = 0;
        while (!pq.isEmpty()) {
            SearchNode node = pq.delMin();
            if (node.board.isGoal()) {
                moves = node.moves;
                solution = new LinkedStack<>();
                for (SearchNode x = node; x != null; x = x.previous) {
                    if (x.previous != null) {
                        Board temp = x.board;
                        solution.push(temp);
                    }
                }
                break;
            } else {
                incMoves++;
                Iterable<Board> neighbors = node.board.neighbors();
                for (Board boardNeighbor : neighbors) {
                    if (node.previous == null) {
                        pq.insert(new SearchNode(boardNeighbor, incMoves, node));
                    } else if (!boardNeighbor.equals(node.previous.board)) {
                        pq.insert(new SearchNode(boardNeighbor, incMoves, node));
                    }
                }
            }
        }

    }

    // Returns the minimum number of moves needed to solve the initial board.
    public int moves() {
        return moves;
    }

    // Returns a sequence of boards in a shortest solution of the initial board.
    public Iterable<Board> solution() {
        return solution;
    }

    // A data type that represents a search node in the grame tree. Each node includes a
    // reference to a board, the number of moves to the node from the initial node, and a
    // reference to the previous node.
    private class SearchNode implements Comparable<SearchNode> {
        private Board board; // board
        private int moves; // Number of moves it took to get to this node from the initial node
        private SearchNode previous; // previous node

        // Constructs a new search node.
        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        // Returns a comparison of this node and other based on the following sum:
        //   Manhattan distance of the board in the node + the # of moves to the node
        public int compareTo(SearchNode other) {
            if (this.board.manhattan() + moves > other.board.manhattan() + other.moves) {
                return 1;
            } else if (this.board.manhattan() + moves < other.board.manhattan() + other.moves) {
                return -1;
            }
            return 0;
        }
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
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.printf("Solution (%d moves):\n", solver.moves());
            StdOut.println(initial);
            StdOut.println("----------");
            for (Board board : solver.solution()) {
                StdOut.println(board);
                StdOut.println("----------");
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
