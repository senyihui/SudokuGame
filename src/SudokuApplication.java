import buildlogic.SudokuBuildLogic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.UserInterfaceImpl;

import java.io.IOException;

public class SudokuApplication extends Application {
    private UserInterfaceImpl uiImpl;

    @Override
    public void start(Stage primaryStage) throws IOException{
        primaryStage.setTitle("Sudoku");
        uiImpl = new UserInterfaceImpl(primaryStage);
        try {
            SudokuBuildLogic.build(uiImpl);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) {
        SudokuApplication.launch(args);
    }

}
