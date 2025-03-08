import java.util.Scanner;

public class BattleShip {

    static final int GRID_SIZE = 10;
    static char[][] player1Grid = new char[GRID_SIZE][GRID_SIZE];
    static char[][] player2Grid = new char[GRID_SIZE][GRID_SIZE];
    static char[][] player1TrackingGrid = new char[GRID_SIZE][GRID_SIZE];
    static char[][] player2TrackingGrid = new char[GRID_SIZE][GRID_SIZE];
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initializeGrid(player1Grid);
        initializeGrid(player2Grid);
        initializeGrid(player1TrackingGrid);
        initializeGrid(player2TrackingGrid);

        placeShips(player1Grid);
        placeShips(player2Grid);

        boolean player1Turn = true;

        while (!isGameOver()) {
            clearScreen();
            if (player1Turn) {
                System.out.println("Player 1's turn:");
                printGrid(player1TrackingGrid);
                playerTurn(player2Grid, player1TrackingGrid);
            } else {
                System.out.println("Player 2's turn:");
                printGrid(player2TrackingGrid);
                playerTurn(player1Grid, player2TrackingGrid);
            }
            player1Turn = !player1Turn;
        }

        System.out.println("Game Over!");
    }

    static void initializeGrid(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = '~';
            }
        }
    }

    static void placeShips(char[][] grid) {
        int[] shSize = {2, 3, 4, 5};
        for (int size : shSize) {
            boolean placed = false;
            while (!placed) {
                int row = (int) (Math.random() * GRID_SIZE);
                int col = (int) (Math.random() * GRID_SIZE);
                boolean horizontal = Math.random() < 0.5;

                if (canPlaceShip(grid, row, col, size, horizontal)) {
                    for (int i = 0; i < size; i++) {
                        if (horizontal) {
                            grid[row][col + i] = 'S';
                        } else {
                            grid[row + i][col] = 'S';
                        }
                    }
                    placed = true;
                }
            }
        }
    }

    static boolean canPlaceShip(char[][] grid, int row, int col, int size, boolean horizontal) {
        if (horizontal) {
            if (col + size > GRID_SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] == 'S') return false;
            }
        } else {
            if (row + size > GRID_SIZE) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] == 'S') return false;
            }
        }
        return true;
    }

    static void playerTurn(char[][] opponentGrid, char[][] trackingGrid) {
        boolean validTurn = false;
        while (!validTurn) {
            System.out.println("Enter Your Target (For Example A5):");
            String input = scanner.next().toUpperCase();

            if (isValidInput(input)) {
                int row = input.charAt(1) - '0';
                int col = input.charAt(0) - 'A';

                if (trackingGrid[row][col] == 'X' || trackingGrid[row][col] == 'O') {
                    System.out.println("You already shot here! Try again.");
                } else {
                    if (opponentGrid[row][col] == 'S') {
                        System.out.println("Hit!");
                        opponentGrid[row][col] = 'X';
                        trackingGrid[row][col] = 'X';
                    } else {
                        System.out.println("Miss.");
                        opponentGrid[row][col] = 'O';
                        trackingGrid[row][col] = 'O';
                    }
                    validTurn = true;
                }
            } else {
                System.out.println("Invalid input. Please enter a letter (A-J) followed by a number (0-9).");
            }
        }
    }

    static boolean isGameOver() {
        return allShipsSunk(player1Grid) || allShipsSunk(player2Grid);
    }

    static boolean allShipsSunk(char[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] == 'S') return false;
            }
        }
        return true;
    }

    static boolean isValidInput(String input) {
        return input.length() == 2 && input.charAt(0) >= 'A' && input.charAt(0) <= 'J' && input.charAt(1) >= '0' && input.charAt(1) <= '9';
    }

    static void printGrid(char[][] grid) {
        System.out.println("  A B C D E F G H I J");
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
