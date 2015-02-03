package speedr.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.event.ActionEvent;

/**
 * Speedr / Ed
 * 02/02/2015 12:13
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
