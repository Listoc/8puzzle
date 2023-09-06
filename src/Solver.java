import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private boolean isSolvable;
    private SearchNode searchNode;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        this.searchNode = new SearchNode(initial, null);
        var twinSearchNode = new SearchNode(initial.twin(), null);
        var minPQ = new MinPQ<SearchNode>();
        var twinMinPQ = new MinPQ<SearchNode>();
        Iterable<Board> neighbors, twinNeighbors;
        this.isSolvable = true;

        neighbors = searchNode.board.neighbors();
        twinNeighbors = twinSearchNode.board.neighbors();

        for (var e : neighbors) {
            minPQ.insert(new SearchNode(e, searchNode));
        }

        for (var e : twinNeighbors) {
            twinMinPQ.insert(new SearchNode(e, twinSearchNode));
        }

        while (!searchNode.board.isGoal() && !twinSearchNode.board.isGoal()) {
            searchNode = minPQ.delMin();
            twinSearchNode = twinMinPQ.delMin();

            neighbors = searchNode.board.neighbors();
            twinNeighbors = twinSearchNode.board.neighbors();

            for (var e : neighbors) {
                if (!e.equals(searchNode.prev.board))
                    minPQ.insert(new SearchNode(e, searchNode));
            }

            for (var e : twinNeighbors) {
                if (!e.equals(searchNode.prev.board))
                    twinMinPQ.insert(new SearchNode(e, twinSearchNode));
            }
        }

        if (!searchNode.board.isGoal()) isSolvable = false;

    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        return isSolvable ? searchNode.moves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable) return null;
        var solution = new Stack<Board>();
        var currentNode = searchNode;

        while (currentNode != null) {
            solution.push(currentNode.board);
            currentNode = currentNode.prev;
        }

        return solution;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode prev;
        private int moves;
        private int priority;

        public SearchNode(Board board, SearchNode prev) {
            this.board = board;
            this.prev = prev;
            if (prev != null) this.moves = prev.moves + 1;
            else this.moves = 0;
            this.priority = moves + board.manhattan();
        }

        @Override
        public int compareTo(SearchNode other) {
            return Integer.compare(this.priority, other.priority);
        }
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
