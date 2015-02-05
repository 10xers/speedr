package speedr.gui.helpers;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Speedr / Ed
 * 05/02/2015 02:35
 */
public class Helpers {


    public static void fadeIn(Node n, int ms)
    {

        FadeTransition ft = new FadeTransition(Duration.millis(ms),n);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);

        n.setVisible(true);
        ft.playFromStart();
    }


    public static void fadeOut(Node n, int ms)
    {

        FadeTransition ft = new FadeTransition(Duration.millis(ms), n);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);

        ft.playFromStart();
    }
}
