
package rom.diaz.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import rom.diaz.config.ControlsSettings;
import rom.diaz.core.Game;
import rom.diaz.core.GameState;

public class GameScreen extends StackPane {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

    private static final long TICK_NANOS =
            1_000_000_000L / 60;

    private final Game game;
    private final GameCanvas canvas;
    private final AnimationTimer gameLoop;

    private final Runnable onBack;

    public GameScreen(
            ControlsSettings controlsSettings,
            Runnable onBack
    ) {

        this.onBack = onBack;

        game = new Game();

        game.getInputHandler().assignKey(
                1,
                controlsSettings.getJumpKey(1)
        );

        canvas = new GameCanvas(WIDTH, HEIGHT);

        getChildren().add(canvas);

        setStyle("-fx-background-color: #79d8f5;");

        gameLoop = new AnimationTimer() {

            private long previousTime = 0;
            private long accumulatedTime = 0;

            @Override
            public void handle(long now) {

                if (previousTime == 0) {
                    previousTime = now;
                    return;
                }

                long elapsed = now - previousTime;
                previousTime = now;

                accumulatedTime += Math.min(
                        elapsed,
                        250_000_000L
                );

                while (accumulatedTime >= TICK_NANOS) {
                    game.updateGame();
                    accumulatedTime -= TICK_NANOS;
                }

                renderGame();
            }
        };

        // Listen for keyboard input while this screen is active.
        setFocusTraversable(true);

        setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ESCAPE) {
                stop();
                onBack.run();
                return;
            }

            game.handleInput(event.getCode());
        });

        renderGame();
    }

    private void renderGame() {

        canvas.render(
                game.getPlayer().getBird(),
                game.getObstacleManager().getObstacles(),
                game.getPlayer().getScore()
        );

        if (game.getState() == GameState.FINISHED) {
            canvas.drawGameOver(
                    game.getPlayer().getScore()
            );
        }
    }

    public void start() {
        requestFocus();
        gameLoop.start();
    }

    public void stop() {
        gameLoop.stop();
    }
}
