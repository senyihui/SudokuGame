package problemdomain;

import computionlogic.SudokuUtilities;
import constants.GameState;

import java.io.Serializable;

public class SudokuGame implements Serializable{
    /**
     * Examples:
     * gameState -GameState.Complete
     *           -GameState.ACTIVE
     * gridState not specifically a grid, but a 3*3 square
     *           -gridState[1][1] Top left square
     */
    private final GameState gameState;
    private final int[][] gridState;

    public static final int GRID_BOUNDARY = 9;

    public SudokuGame(GameState gameState, int[][] gridState) {
        this.gameState = gameState;
        this.gridState = gridState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public int[][] getCopyOfGridState() {
        return SudokuUtilities.copyToNewArray(gridState);
    }
}
