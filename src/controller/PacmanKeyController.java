package controller;

import model.entities.Pacman;
import util.Direction;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PacmanKeyController extends KeyAdapter {

    private final Pacman pacman;

    public PacmanKeyController(Pacman pacman) {
        this.pacman = pacman;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> pacman.setDirection(Direction.UP);
            case KeyEvent.VK_DOWN -> pacman.setDirection(Direction.DOWN);
            case KeyEvent.VK_LEFT -> pacman.setDirection(Direction.LEFT);
            case KeyEvent.VK_RIGHT -> pacman.setDirection(Direction.RIGHT);
        }
    }
}
