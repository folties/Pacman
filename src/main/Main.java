package main;

import util.Resources;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Resources.loadResources();
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
//TODO: do pacman movement logic the same as i have done with ghosts, so i can adjust differenet comfortable speed
//TODO: adding new sprites for pacman upgrade shield
//TODO: do logic for the shield upgrade and lifePlus upgrade
