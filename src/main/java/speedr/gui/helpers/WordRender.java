package speedr.gui.helpers;

import javafx.geometry.Pos;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.swing.*;

/**
 * Speedr / Ed
 * 07/02/2015 02:20
 */
public class WordRender {

    private static final int MAX_FOCUS_LETTER = 5;

    private static int findFocusLetter(String word)
    {
        // inspired
        // https://github.com/Miserlou/Glance-Bookmarklet/blob/master/spritz.js
        // thanks.

        int wordSiz = word.length();

        int bestLetter = 1;
        switch (wordSiz) {
            case 1:
                bestLetter = 1; // first
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                bestLetter = 2; // second
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                bestLetter = 3; // third
                break;
            case 10:
            case 11:
            case 12:
            case 13:
                bestLetter = 4; // fourth
                break;
            default:
                bestLetter = MAX_FOCUS_LETTER; // fifth
        };

        return bestLetter;
    }

    public static void clear(TextFlow currentWordLabel){
        if(currentWordLabel.getChildren().size() > 0) {
            currentWordLabel.getChildren().remove(0, currentWordLabel.getChildren().size());
        }
    }

    public static void renderWordInto(String word, TextFlow currentWordLabel) {

        clear(currentWordLabel);

        int focusLetter = findFocusLetter(word);
        int offsetSpaces = 6;

        String beforeText = word.substring(0, focusLetter-1);

        Text before = new Text(String.format("%" +  (offsetSpaces+(MAX_FOCUS_LETTER - focusLetter)) + "s%s", " ", beforeText));
        Text focus  = new Text(word.substring(focusLetter-1, focusLetter));
        Text after  = new Text(word.substring(focusLetter));

        focus.setFill(Color.RED);

        currentWordLabel.getChildren().addAll(before, focus, after);

    }
}
