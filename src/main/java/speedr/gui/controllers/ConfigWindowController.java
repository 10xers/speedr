package speedr.gui.controllers;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import speedr.core.config.ConfigurationRepository;
import speedr.core.config.CorruptedConfigException;
import speedr.core.entities.config.Configuration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI controller for the config window.
 */
public class ConfigWindowController implements Initializable {

    @FXML
    public TextField hostname;
    @FXML
    public ChoiceBox mailProtocol;
    @FXML
    public TextField port;
    @FXML
    public TextField emailaddr;
    @FXML
    public PasswordField password;
    @FXML
    public TextField inboxName;
    @FXML
    public CheckBox debugModeCheckbox;

    @FXML
    private Button cancelButton;

    private Configuration defaultConfig;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            defaultConfig = ConfigurationRepository.load();
        } catch (IOException | CorruptedConfigException e) {
            throw new IllegalStateException("configuration file corrupted", e);
        }

        setFields(defaultConfig);

        port.textProperty().addListener(this::numbersOnlyInput);
    }


    private void numbersOnlyInput(ObservableValue o, String oldValue, String newValue)
    {
        if (newValue.isEmpty())
            return;
        else {
            try {
                int parseAttempt = Integer.parseInt(newValue);
            } catch (NumberFormatException e)
            {
                ((StringProperty)o).setValue(oldValue);
            }
        }
    }

    private void setFields(final Configuration c)
    {
        int choice = mailProtocol.getItems().indexOf(c.getMailType());
        mailProtocol.getSelectionModel().select(choice>=0?choice:0);
        hostname.setText(c.getHost());
        port.setText(""+c.getPort());
        emailaddr.setText(c.getUser());
        password.setText(c.getPassword());
        inboxName.setText(c.getRootFolder());
        debugModeCheckbox.setSelected(c.isDebugMode());
    }

    private Configuration getFields(Configuration c)
    {
        c.setMailType(mailProtocol.getValue().toString());
        c.setHost(hostname.getText());
        c.setPort(Integer.parseInt(port.getText()));
        c.setUser(emailaddr.getText());
        c.setPassword(password.getText());
        c.setRootFolder(inboxName.getText());
        c.setDebugMode(debugModeCheckbox.isSelected());

        return c;
    }

    @FXML
    public void cancelButtonClicked(ActionEvent actionEvent) {
        Stage us = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        us.close();
    }

    @FXML
    public void applyButtonClicked(ActionEvent actionEvent) {

        Configuration toApply = getFields(defaultConfig);

        try {
            ConfigurationRepository.save(toApply);
        } catch (IOException e) {
            throw new IllegalStateException("failed to save config", e);
        }
        
        cancelButton.setText("Close");
    }


}
