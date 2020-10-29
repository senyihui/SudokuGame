package userinterface.logic;

import constants.GameState;
import constants.Messages;
import problemdomain.IStorage;
import problemdomain.SudokuGame;
import computionlogic.GameLogic;
import userinterface.IUserInterfaceContract;

import java.io.IOException;

public class ControlLogic implements IUserInterfaceContract.EventListener {
    private IStorage storage;

    private IUserInterfaceContract.View view;

    public ControlLogic(IStorage storage, IUserInterfaceContract.View view) {
        this.storage = storage;
        this.view = view;
    }

    /**
     * Use Case:
     * 1. Retrieve current "state" of the data from IStorage
     * 2. Update it according to the input
     * 3. Write the result to IStorage
     * @param x X coordinate of the selected input
     * @param y Y ...
     * @param input Which key was entered, One of:
     *  - Numbers 0-9
     *
     */
    @Override
    public void onSudoKuInput(int x, int y, int input) {
        try {
            SudokuGame gameData = storage.getGameData();
            int[][] newGridState = gameData.getCopyOfGridState();
            newGridState[x][y] = input;

            // update board to set the original number unchangeable
            view.updateBoard(gameData);
            boolean[][] isOrigin = gameData.getIsOrigin();

            gameData = new SudokuGame(
                    GameLogic.checkForCompletion(newGridState),
                    newGridState,
                    isOrigin);
            System.out.println(gameData.getGameState());
            storage.updateGameData(gameData);

            view.updateSquare(x, y, input);
            if (gameData.getGameState() == GameState.COMPLETE) view.showDialog(Messages.GAME_COMPLETE);
        } catch (IOException e) {
            e.printStackTrace();
            view.showDialog(Messages.ERROR);
        }
    }

    @Override
    public void onDialogClick() {
        try {
            view.updateBoard(storage.getGameData());
            storage.updateGameData(GameLogic.getNewGame());
        } catch (IOException e) {
            view.showError(Messages.ERROR);
        }

    }
}
