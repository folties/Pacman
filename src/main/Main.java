package main;

import util.Resources;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Resources.loadResources();
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
