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
    public static final int GRID_BOUNDARY = 9;

    private final GameState gameState;
    private final int[][] gridState;
    private final boolean[][] isOrigin;

    public SudokuGame(GameState gameState, int[][] gridState, boolean[][] isOrigin) {
        this.gameState = gameState;
        this.gridState = gridState;
        this.isOrigin = isOrigin;
    }

    public void setIsOrigin(int x, int y, boolean bool) {
        this.isOrigin[x][y] = bool;
    }

    public boolean getIsOrigin(int x, int y) {
        return this.isOrigin[x][y];
    }

    public boolean[][] getIsOrigin() {
        return this.isOrigin;
    }


    public GameState getGameState() {
        return gameState;
    }

    public int[][] getCopyOfGridState() {
        return SudokuUtilities.copyToNewArray(gridState);
    }
}
