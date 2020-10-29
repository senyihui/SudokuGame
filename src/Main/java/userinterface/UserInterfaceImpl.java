package userinterface;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import problemdomain.Coordinates;
import problemdomain.SudokuGame;
import constants.GameState;

import java.util.HashMap;

/**
 * manage the window and display notifications
 */
public class UserInterfaceImpl implements IUserInterfaceContract.View,
        EventHandler<KeyEvent> {

    /**
     * Stage and Group are JavaFX specific classes for modifying the UI.
     * Think of them as containers of various UI components.
     */
    private final Stage stage;
    private final Group root;

    /**
     * A HashMap is a data structure which stores key/value pairs. Rather than creating a member variable for every
     * SudokuTextField object (all 81 of them), I instead store these references within a HashMap, and I retrieve
     * them by using their X and Y Coordinates as a "key" (a unique value used to look something up).
     */
    HashMap<Coordinates, SudokuTextField> textFieldCoordinates;

    private IUserInterfaceContract.EventListener listener;

    /**
     * Variables
     */
    // size of the window
    private static final double WIDTH = 636;
    private static final double HEIGHT = 676;

    // padding
    private static final double BOARD_PADDING = 30;

    private static final double BOARD_LENGTH = 576;
    private static final Color WINDOW_BACKGROUND_COLOR = Color.rgb(0, 150, 136);
    private static final Color BOARD_BACKGROUND_COLOR = Color.rgb(61, 210, 206);
    private static final String Sudoku = "Sudoku";

    public UserInterfaceImpl(Stage stage) {
        this.stage = stage;
        this.root = new Group();
        textFieldCoordinates = new HashMap<>();
        initializeUserInterface();
    }

    private void initializeUserInterface() {
        drawBackground(root);
        drawTitle(root);
        drawSudokuBoard(root);
        drawTextFields(root);
        drawGridLines(root);
        stage.show();
    }

    /**
     * background of primary window
     * @param root
     */
    private void drawBackground(Group root) {
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setFill(WINDOW_BACKGROUND_COLOR);
        stage.setScene(scene);
    }

    private void drawTitle(Group root) {
        Text title = new Text(235, 650, Sudoku);
        title.setFill(Color.WHITE);
        Font titleFont = new Font(43);
        title.setFont(titleFont);
        root.getChildren().add(title);
    }

    /**
     * Background of the actual sudoku board, offset from the window by BOARD_PADDING
     * @param root
     */
    private void drawSudokuBoard(Group root) {
        Rectangle board = new Rectangle();
        board.setHeight(BOARD_LENGTH);
        board.setWidth(BOARD_LENGTH);
        board.setX(BOARD_PADDING);
        board.setY(BOARD_PADDING);
        board.setFill(BOARD_BACKGROUND_COLOR);
        root.getChildren().add(board);
    }

    /**
     * 1. Draw each TextField based on x and y values.
     * 2. As each TextField is drawn, add it's coordinates (x, y) based on it's Hash Value to
     * to the HashMap.
     *
     * @param root
     */
    private void drawTextFields(Group root) {
        // where to start drawing
        final int xOrigin = 30;
        final int yOrigin = 30;

        final int delta = 64;

        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                int x = xOrigin + delta * xIndex;
                int y = yOrigin + delta * yIndex;

                SudokuTextField tile = new SudokuTextField(xIndex, yIndex, true);
                StyleSudokuTile(tile, x, y);

                //Note: Note that UserInterfaceImpl implements EventHandler<ActionEvent> in the class declaration.
                //By passing "this" (which means the current instance of UserInterfaceImpl), when an action occurs,
                //it will jump straight to "handle(ActionEvent actionEvent)" down below.
                tile.setOnKeyPressed(this);
                textFieldCoordinates.put(new Coordinates(xIndex, yIndex), tile);

                root.getChildren().add(tile);
            }
        }

    }

    /**
     * helper for text field
     * A tile means a part of space, not simply a number
     * @param tile
     * @param x
     * @param y
     */
    private void StyleSudokuTile(SudokuTextField tile, int x, int y) {
        Font numberFont = new Font(32);
        tile.setFont(numberFont);
        tile.setAlignment(Pos.CENTER);

        tile.setLayoutX(x);
        tile.setLayoutY(y);
        tile.setPrefHeight(64);
        tile.setPrefWidth(64);

        tile.setBackground(Background.EMPTY);
    }

    private void drawGridLines(Group root) {
        int XandY = 64;
        int index = 0;
        while (index <= 9) {
            int thickness = 0;
            if (index == 3 || index == 6) {
                thickness = 3;
            } else {
                thickness = 2;
            }

            Rectangle verticalLine = getLine(BOARD_PADDING + XandY * index,
                                            BOARD_PADDING, thickness, BOARD_LENGTH);

            Rectangle horizontalLine = getLine(BOARD_PADDING, BOARD_PADDING + XandY * index,
                                            BOARD_LENGTH, thickness);

            root.getChildren().addAll(verticalLine, horizontalLine);

            index++;
        }
    }

    /**
     * use rect to replace line
     * @param x origin X point
     * @param y origin Y point
     * @param width it depends, for example, when it comes to vertical line, it refers to thickness.
     * @param height the same as width
     * @return
     */
    private Rectangle getLine(double x, double y, double width, double height) {
        Rectangle line = new Rectangle();
        line.setX(x);
        line.setY(y);
        line.setWidth(width);
        line.setHeight(height);
        line.setFill(Color.BLACK);
        return line;
    }


    @Override
    public void setListener(IUserInterfaceContract.EventListener listener) {
        this.listener = listener;
    }

    @Override
    public void updateSquare(int xIndex, int yIndex, int input) {
        SudokuTextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));
        String value = Integer.toString(input);
        if (value.equals("0")) value = "";

        tile.textProperty().setValue(value);
    }

    @Override
    public void updateBoard(SudokuGame game) {
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            for (int yIndex = 0; yIndex < 9; yIndex++) {
                SudokuTextField tile = textFieldCoordinates.get(new Coordinates(xIndex, yIndex));

                String value = Integer.toString(
                        game.getCopyOfGridState()[xIndex][yIndex]
                );

                if (value.equals("0")) value = "";

                tile.setText(value);
                //If a given tile has a non-zero value and the state of the game is GameState.NEW, then mark
                //the tile as read only. Otherwise, ensure that it is NOT read only.
                if (game.getGameState() == GameState.NEW) {
                    if (value.equals("")) {
                        tile.setStyle("-fx-opacity: 1;");
                        tile.setDisable(false);
                        game.setIsOrigin(xIndex, yIndex, false);
                    } else {
                        tile.setStyle("-fx-opacity: 0.8;");
                        tile.setDisable(true);
                        game.setIsOrigin(xIndex, yIndex, true);
                    }
                } else if (game.getGameState() == GameState.ACTIVE &&
                        game.getIsOrigin(xIndex, yIndex)) {
                    tile.setStyle("-fx-opacity: 0.8;");
                    tile.setDisable(true);
                }
            }
        }
    }

    @Override
    public void showDialog(String message) {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK);
        dialog.showAndWait();

        if (dialog.getResult() == ButtonType.OK) listener.onDialogClick();
    }

    @Override
    public void showError(String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        dialog.showAndWait();
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            if (event.getText().matches("[1-9]")) {
                int value = Integer.parseInt(event.getText());
                handleInput(value, event.getSource());
            } else if (event.getCode() == KeyCode.BACK_SPACE) {
                handleInput(0, event.getSource());
            } else {
                ((TextField)event.getSource()).setText("");
            }
        }

        event.consume();
    }

    /**
     * @param value  expected to be an integer from 0-9, inclusive
     * @param source the TextField object that was clicked.
     */
    private void handleInput(int value, Object source) {
        listener.onSudoKuInput(((SudokuTextField) source).getX(),
                                ((SudokuTextField) source).getY(),
                                value
        );
    }
}
