package computionlogic;

import java.util.Random;

import static problemdomain.SudokuGame.GRID_BOUNDARY;

public class GameGenerator {
    public static int[][] getNewGameGrid() {
        return unsolvedGame(getSolvedGame());
    }

    public static int[][] getSolvedGame() {
        Random random = new Random(System.currentTimeMillis());
        int seed = random.nextInt(9);
        int[][] basic = new int[][]{
                {9, 7, 8, 3, 1, 2, 6, 4, 5},
                {3, 1, 2, 6, 4, 5, 9, 7, 8},
                {6, 4, 5, 9, 7, 8, 3, 1, 2},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {8, 9, 7, 2, 3, 1, 5, 6, 4},
                {2, 3, 1, 5, 6, 4, 8, 9, 7},
                {5, 6, 4, 8, 9, 7, 2, 3, 1}
        };

        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                basic[xIndex][yIndex] = (basic[xIndex][yIndex] + seed) % 9 + 1;
            }
        }

        return basic;
    }

    /**
     * The purpose of this function is to take a game which has already been solved (and thus proven to be solvable),
     * and randomly assign a certain number of tiles to be equal to 0. It appears that there is no straight
     * forward way to check if a puzzle is still solvable after removing the tiles, beyond using another algorithm
     * to attempt to re-solve the problem.
     *
     * 1. Copy values of solvedGame to a new Array (make into a helper)
     * 2. Remove 40 Values randomly from the new Array.
     * 3. Test the new Array for solvablility.
     * 4a. Solveable -> return new Array
     * 4b. return to step 1
     * @param solvedGame
     * @return
     */
    private static int[][] unsolvedGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        //note: not actually solvable until the algorithm below finishes!
        int[][] solvableArray = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);

        int index = 0;

        while (index < 30) {
            int xCoordinate = random.nextInt(GRID_BOUNDARY);
            int yCoordinate = random.nextInt(GRID_BOUNDARY);

            if (solvableArray[xCoordinate][yCoordinate] != 0) {
                solvableArray[xCoordinate][yCoordinate] = 0;
                index++;
            }
        }

        int[][] toBeSolved = new int[GRID_BOUNDARY][GRID_BOUNDARY];
        SudokuUtilities.copySudokuArrayValues(solvableArray, toBeSolved);

        //check if result is solvable
//      solvable = GameSolver.puzzleIsSolvable(toBeSolved);

        //TODO Delete after tests
//        System.out.println(solvable);


        return solvableArray;
    }
}
