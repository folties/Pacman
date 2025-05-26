import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);

        /*GraphicsEnvironment font = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fonts[] = font.getAvailableFontFamilyNames();
        for (String i : fonts) {
            System.out.println(i);
        }*/
    }
}
