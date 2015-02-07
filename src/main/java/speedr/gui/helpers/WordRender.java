package speedr.gui.helpers;

import javafx.geometry.Pos;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.paint.Color;

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

    public static HBox renderWord(String word)
    {
        int focusLetter = findFocusLetter(word);
        int offsetSpaces = 6;

        String before = word.substring(0, focusLetter-1);
        before = String.format("%" +  (offsetSpaces+(MAX_FOCUS_LETTER - focusLetter)) + "s%s", " ", before);
        String focus = word.substring(focusLetter-1, focusLetter);
        String after = word.substring(focusLetter);

        HBox box = HBoxBuilder.create()
                .spacing(0)
                .alignment(Pos.CENTER_LEFT)
                .style("-fx-font-family: monospace;")
                .children(
                        LabelBuilder.create()
                                .text(before)
                                .textFill(Color.BLACK)
                                .build(),
                        LabelBuilder.create()
                                .text(focus)
                                .textFill(Color.rgb(222,98,98))
                                .build(),
                        LabelBuilder.create()
                                .text(after)
                                .textFill(Color.BLACK)
                                .build())
                .build();


        return box;
    }


}
