import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);

        /*GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

        System.out.println("Getting the font family names");

        // Array of all the fonts available in AWT
        String fonts[] = ge.getAvailableFontFamilyNames();

        // Getting the font family names
        for (String i : fonts) {
            System.out.println(i + " ");
        }*/
    }
}

//TODO: change the color of the portals