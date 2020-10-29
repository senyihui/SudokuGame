package computionlogic;

import constants.GameState;
import problemdomain.SudokuGame;

import static problemdomain.SudokuGame.GRID_BOUNDARY;

public class GameLogic {

    public static GameState checkForCompletion(int[][] newGridState) {
        if (!AreTilesFilled(newGridState)) {
            System.out.println("unfilled");
            return GameState.ACTIVE;
        }
        if (!isSudokuValid(newGridState)) {
            System.out.println("invalid");
            return GameState.ACTIVE;
        }


        return GameState.COMPLETE;
    }

    /**
     * traverse all tiles and determine if all are filled
     * @param grid
     * @return
     */
    private static boolean AreTilesFilled(int[][] grid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                if (grid[xIndex][yIndex] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * check if Sudoku is valid
     * ref: https://leetcode-cn.com/problems/valid-sudoku/solution/javawei-yun-suan-1ms-100-li-jie-fang-ge-suo-yin-by/
     * @param grid
     * @return
     */
    public static boolean isSudokuValid(int[][] grid) {
        int[] rows = new int[GRID_BOUNDARY];
        int[] cols = new int[GRID_BOUNDARY];
        int[] squares = new int[GRID_BOUNDARY];

        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                int boxIndex = xIndex / 3 * 3 + yIndex / 3;
                // for bit operation
                int transfer = grid[xIndex][yIndex];
                if ((rows[xIndex] >> transfer & 1) == 1
                        || (cols[yIndex] >> transfer & 1) == 1
                        || (squares[boxIndex] >> transfer & 1) == 1) {
                    return false;
                }
                rows[xIndex] = rows[xIndex] | (1 << transfer);
                cols[yIndex] = cols[yIndex] | (1 << transfer);
                squares[boxIndex] = squares[boxIndex] | (1 << transfer);
            }
        }
        return true;
    }

    public static SudokuGame getNewGame() {
        return new SudokuGame(
                GameState.NEW,
                GameGenerator.getNewGameGrid(),
                new boolean[GRID_BOUNDARY][GRID_BOUNDARY]);
    }
}
