
package speedr;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.lang.Override;

/**
 *
 * Speedr - A Speed Reading App for Email.
 *
 * Speedr acts as a light-weight email client, with functionality to speed read your way through emails at
 * alarming speed.
 *
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Font.loadFont(this.getClass().getResource("/font/UbuntuMono-Regular.ttf").toExternalForm(), 12);
        Font.loadFont(this.getClass().getResource("/font/OpenSans-Regular.ttf").toExternalForm(), 12);
        Font.loadFont(this.getClass().getResource("/font/OpenSans-BoldItalic.ttf").toExternalForm(), 12);

        Parent main = FXMLLoader.load(getClass().getResource("/fxml/main_window.fxml"));
        Parent splash = FXMLLoader.load(getClass().getResource("/fxml/splash.fxml"));

        Scene scene = new Scene(splash);

        scene.getStylesheets().add("/style/speedr.css");

        primaryStage.getIcons().add(new Image("/icons/glyphicons/glyphicons-23-fire.png"));
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest((t)->{Platform.exit(); System.exit(0); }); // this is just a shim for something that isn't working properly

        primaryStage.setTitle("speedr");
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
