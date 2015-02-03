package speedr.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Speedr / Ed
 * 03/02/2015 22:55
 */
public class ConfigWindowController {

    @FXML
    private Button cancelButton;

    @FXML
    public void cancelButtonClicked(ActionEvent actionEvent) {
        Stage us = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        us.close();
    }

    @FXML
    public void applyButtonClicked(ActionEvent actionEvent) {

    }
}
