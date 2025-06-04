package game;

import java.util.List;

public class GameController {

    private final PacmanLoop gameLoop;
    private final TimerLoop timerLoop;
    private final List<GhostLoop> ghostLoops;

    public GameController(PacmanLoop gameLoop, TimerLoop timerLoop, List<GhostLoop> ghostLoops) {
        this.gameLoop = gameLoop;
        this.timerLoop = timerLoop;
        this.ghostLoops = ghostLoops;
    }

    public void pauseGame() {
        gameLoop.pause();
        timerLoop.pause();
        ghostLoops.forEach(GhostLoop::pause);
    }

    public void resumeGame() {
        gameLoop.resumeLoop();
        timerLoop.resumeLoop();
        ghostLoops.forEach(GhostLoop::resumeLoop);
    }

    public void stopAll() {
        gameLoop.stopLoop();
        timerLoop.stopLoop();
        ghostLoops.forEach(GhostLoop::stopLoop);
    }
}
