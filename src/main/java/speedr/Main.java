/*
 * speedr 02/02/2015 11:18
 */
package speedr;

import javafx.fxml.FXMLLoader;

import java.lang.Override;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main_window.fxml"));

        Scene scene = new Scene(root);
        primaryStage.setTitle("speedr");

        stage.setOnCloseRequest((t)->{Platform.exit(); System.exit(0); }); // this is just a shim for something that isn't working properly

        stage.show();

    }

    public static void main(String[] args)
    {
        launch(args);
    }
    
    
}
