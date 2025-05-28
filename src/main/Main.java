package main;

import util.Resources;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Resources.loadResources();
        SwingUtilities.invokeLater(MainWindow::new);

        /*GraphicsEnvironment font = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String fonts[] = font.getAvailableFontFamilyNames();
        for (String i : fonts) {
            System.out.println(i);
        }*/
    }
}
