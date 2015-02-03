package speedr.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;

/**
 *
 * Controller for the Speed Reader main panel GUI.
 *
 */

public class MainWindowController {

    @FXML
    private Button goBtn;
    @FXML
    private Label lbl;

    @FXML
    public void goBtnAction(ActionEvent evt)
    {
        lbl.setText(lbl.getText()+" fart");
    }

}
