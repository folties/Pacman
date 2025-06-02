package main;

import util.Resources;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Resources.loadResources();
        SwingUtilities.invokeLater(MainWindow::new);
    }
}

//TODO: after new level upgrades disapear
//TODO: upgrades onlu for 15 seconds

