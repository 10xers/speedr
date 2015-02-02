/*
 * speedr 02/02/2015 11:18
 */
package speedr;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.lang.Override;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_window.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("speedr");

        primaryStage.setOnCloseRequest((t)->{Platform.exit(); System.exit(0); }); // this is just a shim for something that isn't working properly

        primaryStage.show();

    }

    public static void main(String[] args)
    {
        launch(args);
    }
    
    
}
