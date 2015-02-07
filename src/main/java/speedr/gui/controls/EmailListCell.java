package speedr.gui.controls;

import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import speedr.sources.email.Email;

public class EmailListCell extends ListCell<Email> {

    @Override
    public void updateItem(Email e, boolean empty) {

        super.updateItem(e, empty);

        if(empty){
            setGraphic(new Text(""));
            return;
        }

        if(e != null){

            if(e.isRead()){
                System.out.println("style: " + this.getStyle());
                getStyleClass().removeAll();
                getStyleClass().add("unread");
            }

            setText(e.toString());

        } else {

            setText("");

        }
    }

}
