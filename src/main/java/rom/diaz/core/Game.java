package rom.diaz.core;

import rom.diaz.input.InputHandler;
import rom.diaz.input.KeyBindings;
import rom.diaz.logic.ObstacleManager;
import rom.diaz.model.Bird;
import rom.diaz.model.Player;
import rom.diaz.model.PlayerState;

import javafx.scene.input.KeyCode;

import lombok.Getter;

@Getter
public class Game {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

    private static final double BIRD_RADIUS = 15;
    private static final double PIPE_SPEED = 3;
    private static final double PIPE_WIDTH = 70;
    private static final double GAP_HEIGHT = 170;

    private static final int SPAWN_INTERVAL = 110;

    private Player player;
    private ObstacleManager obstacleManager;
    private InputHandler inputHandler;

    private GameState state;

    public Game() {
        startGame();
    }

    public void startGame() {

        Bird bird = new Bird(150, 300);
        player = new Player(1, bird);

        obstacleManager = new ObstacleManager(
                PIPE_SPEED,
                BIRD_RADIUS
        );

        // Make a pipe available from the beginning.
        obstacleManager.generateObstacle(
                WIDTH,
                HEIGHT,
                PIPE_WIDTH,
                GAP_HEIGHT
        );

        inputHandler = new InputHandler();

        inputHandler.registerPlayer(
                player,
                new KeyBindings(KeyCode.SPACE)
        );

        state = GameState.PLAYING;
    }

    public void updateGame() {

        if (state != GameState.PLAYING) {
            return;
        }

        player.update();

        obstacleManager.updateAndSpawn(
                WIDTH,
                HEIGHT,
                PIPE_WIDTH,
                GAP_HEIGHT,
                SPAWN_INTERVAL
        );

        Bird bird = player.getBird();

        if (bird.getY() - BIRD_RADIUS <= 0
                || bird.getY() + BIRD_RADIUS >= HEIGHT) {

            endGame();
            return;
        }

        if (obstacleManager.checkCollision(bird)) {
            endGame();
            return;
        }

        int passed =
                obstacleManager.countPassedObstacles(bird);

        for (int i = 0; i < passed; i++) {
            player.increaseScore();
        }
    }

    public void handleInput(KeyCode key) {

        if (key == KeyCode.R
                && state == GameState.FINISHED) {

            startGame();
            return;
        }

        if (state == GameState.PLAYING) {
            inputHandler.handleInput(key);
        }
    }

    public void endGame() {

        if (state != GameState.PLAYING) {
            return;
        }

        player.finishRun();
        state = GameState.FINISHED;
    }
}
