import computionlogic.GameGenerator;
import computionlogic.GameLogic;
import constants.GameState;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;


public class SudokuGeneratorTest {

    /**
     * Generate a new puzzle based on the appropriate rules.
     */
    @Test
    public void onGenerateUnsolvedPuzzle() {
        int[][] newPuzzle = GameLogic.getNewGame().getCopyOfGridState();

        int numberOfFilledSquares = 0;

        //Traverse array
        for (int xIndex = 0; xIndex < 9; xIndex++){
            for (int yIndex = 0; yIndex < 9; yIndex++ ){
                if (newPuzzle[xIndex][yIndex] != 0) numberOfFilledSquares++;
            }
        }

        assert (!GameLogic.isSudokuValid(newPuzzle));
        assert (GameLogic.checkForCompletion(newPuzzle) == GameState.ACTIVE);
        assert (numberOfFilledSquares == 51);
    }

    /**
     * Generate a solved puzzle based on the appropriate rules.
     */
    @Test
    public void onGenerateSolvedPuzzle() throws Exception {
        GameGenerator gameGenerator = new GameGenerator();
        int[][] newPuzzle = Whitebox.invokeMethod(gameGenerator,
                "getSolvedGame");

        int numberOfFilledSquares = 0;

        //Traverse array
        for (int xIndex = 0; xIndex < 9; xIndex++){
            for (int yIndex = 0; yIndex < 9; yIndex++ ){
                if (newPuzzle[xIndex][yIndex] != 0) numberOfFilledSquares++;
            }
        }

        assert (GameLogic.isSudokuValid(newPuzzle));
        assert (GameLogic.checkForCompletion(newPuzzle) == GameState.COMPLETE);
        assert (numberOfFilledSquares == 81);
    }

}
