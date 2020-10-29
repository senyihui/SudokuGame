package userinterface;

import javafx.scene.control.TextField;

public class SudokuTextField extends TextField {
    private final int x;
    private final int y;
    private boolean origin;

    public SudokuTextField(int x, int y, boolean origin) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void replaceText(int start, int end, String text) {
        if (text.matches("[1-9]")) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String replacement) {
        if (replacement.matches("[1-9]")) {
            super.replaceSelection(replacement);
        }
    }
}
