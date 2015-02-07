package speedr.gui;

import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import speedr.sources.email.Email;

public class EmailListCell extends ListCell<Email> {

    @Override
    public void updateItem(Email e, boolean empty) {


        super.updateItem(e, empty);


        if(e != null){
            setGraphic(new Text(e.toString()));
        }
    }

}
