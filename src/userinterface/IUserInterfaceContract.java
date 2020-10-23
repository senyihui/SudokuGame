package userinterface;

import problemdomain.SudokuGame;

public interface IUserInterfaceContract {
    interface EventListener {
        void onSudoKuInput(int x, int y, int input);
        void onDialogClick();
    }

    interface View {
        void setListener(IUserInterfaceContract.EventListener listener);
        // update a single square after user input
        void updateSquare(int x, int y, int input);

        // update the whole board
        void updateBoard(SudokuGame game);
        void showDialog(String message);
        void showError(String message);
    }
}
