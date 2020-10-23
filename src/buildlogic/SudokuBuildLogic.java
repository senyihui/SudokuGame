package buildlogic;

import computionlogic.GameLogic;
import constants.GameState;
import persistence.LocalStorageImpl;
import problemdomain.IStorage;
import problemdomain.SudokuGame;
import userinterface.IUserInterfaceContract;
import userinterface.UserInterfaceImpl;
import userinterface.logic.ControlLogic;

import java.io.IOException;

public class SudokuBuildLogic {

    public static void build(IUserInterfaceContract.View userInterface) throws IOException {
        SudokuGame initialState;
        IStorage storage = new LocalStorageImpl();

        try {
            initialState = storage.getGameData();
        } catch (IOException e) {
            initialState = GameLogic.getNewGame();
            storage.updateGameData(initialState);
        }

        IUserInterfaceContract.EventListener uiLogic = new ControlLogic(storage, userInterface);
        userInterface.setListener(uiLogic);
        userInterface.updateBoard(initialState);
    }
}
