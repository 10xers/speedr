package speedr.gui.controls;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import speedr.sources.email.Email;

public class EmailListCell extends ListCell<Email> {

    @Override
    public void updateItem(Email e, boolean empty) {

        super.updateItem(e, empty);

        getStyleClass().removeAll();
        setText(empty ? null : getString());

        if(e != null && !empty && e.isRead()){
            getStyleClass().add("unread");
        }
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }

}
