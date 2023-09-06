import java.util.Arrays;
import edu.princeton.cs.algs4.Stack;

// i * N + j - expression for translation 1D array to 2D array
public class Board {

    private int[] arrayOfBoard;
    private int dimension;
    public Board(int[][] tiles) {
        this.dimension = tiles.length;
        arrayOfBoard = new int[dimension * dimension];
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                arrayOfBoard[i * dimension + j] = tiles[i][j];
            }
        }
    }

    private Board(Board board) {
        this.dimension = board.dimension;
        arrayOfBoard = Arrays.copyOf(board.arrayOfBoard, dimension * dimension);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();

        builder.append(dimension);
        builder.append("\n ");

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                builder.append(arrayOfBoard[i * dimension + j]);
                if (j != dimension - 1) builder.append("  ");
            }
            if (i != dimension - 1) builder.append("\n ");
        }

        return builder.toString();
    }

    public int dimension() {
        return dimension;
    }

    public int hamming() {
        int hamming = 0;

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (arrayOfBoard[i * dimension + j] == 0) continue;
                if (arrayOfBoard[i * dimension + j] != i * dimension + j + 1) hamming++;
            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattanForTile = 0;
        int manhattanSummary = 0;
        int currentTileValue = 0;

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                currentTileValue = arrayOfBoard[i * dimension + j];

                if (currentTileValue == 0) continue;

                if (currentTileValue != i * dimension + j + 1) {
                    manhattanForTile = Math.abs((j) - (currentTileValue - 1) % dimension);
                    manhattanForTile += Math.abs((i + 1) - Math.ceil((double) currentTileValue / dimension));
                    manhattanSummary += manhattanForTile;
                }
            }
        }
        return manhattanSummary;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Board board = (Board) other;

        if (dimension != board.dimension) return false;
        return Arrays.equals(arrayOfBoard, board.arrayOfBoard);
    }

    public Iterable<Board> neighbors() {
        var neighbors = new Stack<Board>();
        Board copy;
        int xOfZero = 0;
        int yOfZero = 0;

        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                if (arrayOfBoard[i * dimension + j] == 0) {
                    xOfZero = j;
                    yOfZero = i;
                }
            }
        }

        if (yOfZero != 0) {
            copy = new Board(this);
            copy.swap(xOfZero, yOfZero, xOfZero, yOfZero - 1);
            neighbors.push(copy);
        }
        if (yOfZero < dimension - 1) {
            copy = new Board(this);
            copy.swap(xOfZero, yOfZero, xOfZero, yOfZero + 1);
            neighbors.push(copy);
        }
        if (xOfZero != 0) {
            copy = new Board(this);
            copy.swap(xOfZero, yOfZero, xOfZero - 1, yOfZero);
            neighbors.push(copy);
        }
        if (xOfZero != dimension - 1) {
            copy = new Board(this);
            copy.swap(xOfZero, yOfZero, xOfZero + 1, yOfZero);
            neighbors.push(copy);
        }

        return neighbors;
    }

    public Board twin() {
        var twin = new Board(this);
        int tmp = 0;

        if (twin.arrayOfBoard[0] != 0) {
            if (twin.arrayOfBoard[1] != 0) {
                twin.swap(0, 0, 1, 0);
            } else {
                twin.swap(0, 0, 0, 1);
            }
        } else {
            twin.swap(1, 0, 0, 1);
        }

        return twin;
    }

    private void swap(int fromX, int fromY, int toX, int toY) {
        int tmp = arrayOfBoard[fromY * dimension + fromX];
        arrayOfBoard[fromY * dimension + fromX] = arrayOfBoard[toY * dimension + toX];
        arrayOfBoard[toY * dimension + toX] = tmp;
    }

    public static void main(String[] args) {
        var firstBoard = new Board(new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});
        System.out.println("Board:\n" + firstBoard);
        System.out.println("Hamming: " + firstBoard.hamming());
        System.out.println("Manhattan: : " + firstBoard.manhattan());

        var secondBoard = new Board(new int[][]{{1, 0, 3}, {4, 2, 5}, {7, 8, 6}});
        System.out.println("\nBoard:\n" + secondBoard);
        Iterable<Board> neighbors = secondBoard.neighbors();
        System.out.println("\nNeighbors:");
        for (var e : neighbors) {
            System.out.println(e);
        }

        System.out.println("\nTwin:\n" + secondBoard.twin());

        System.out.println("\nFalse equals: " + firstBoard.equals(secondBoard));

        var thirdBoard = new Board(new int[][]{{8, 1, 3}, {4, 0, 2}, {7, 6, 5}});

        System.out.println("True equals: " + firstBoard.equals(thirdBoard));
        System.out.println("Self equals: " + firstBoard.equals(firstBoard));
        System.out.println("Dimension: " + secondBoard.dimension());

        System.out.println("False isGoal: " + secondBoard.isGoal());
        var fourthBoard = new Board(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}});
        System.out.println("True isGoal: " + fourthBoard.isGoal());


    }

}
