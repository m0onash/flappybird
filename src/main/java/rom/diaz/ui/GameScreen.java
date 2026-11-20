
package rom.diaz.ui;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;

import rom.diaz.audio.AudioManager;
import rom.diaz.config.ControlsSettings;
import rom.diaz.config.Localization;
import rom.diaz.core.Game;
import rom.diaz.core.GameState;
import rom.diaz.service.StatsService;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;


public class GameScreen extends StackPane {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

    private static final long TICK_NANOS =
            1_000_000_000L / 60;

    private final Game game;
    private final GameCanvas canvas;
    private final AnimationTimer gameLoop;

    private final StatsService statsService;
    private boolean statsSaved = false;

    private final AudioManager audioManager;

    private final Runnable onBack;

    public GameScreen(
            ControlsSettings controlsSettings,
            StatsService statsService,
            Localization localization,
            AudioManager audioManager,
            Runnable onBack
    ) {
        this.statsService = statsService;
        this.audioManager = audioManager;

        this.onBack = onBack;

        game = new Game();

        game.getInputHandler().assignKey(
                1,
                controlsSettings.getJumpKey(1)
        );


        canvas = new GameCanvas(
                WIDTH,
                HEIGHT,
                localization
        );

// Container that fills the available screen.
        Pane viewport = new Pane();
        viewport.setMinSize(0, 0);

// Scale the 800x600 game world to match the viewport.
        Scale scale = new Scale(1, 1, 0, 0);

        scale.xProperty().bind(
                viewport.widthProperty().divide(WIDTH)
        );

        scale.yProperty().bind(
                viewport.heightProperty().divide(HEIGHT)
        );

        canvas.getTransforms().add(scale);
        viewport.getChildren().add(canvas);

// Prevent graphics from extending outside the viewport.
        Rectangle clip = new Rectangle();

        clip.widthProperty().bind(viewport.widthProperty());
        clip.heightProperty().bind(viewport.heightProperty());

        viewport.setClip(clip);

        getChildren().add(viewport);

        getStyleClass().add("game-root");


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

                    int previousScore = game.getPlayer().getScore();
                    GameState previousState = game.getState();

                    game.updateGame();

                    // Play when the player successfully passes an obstacle.
                    if (game.getPlayer().getScore() > previousScore) {
                        audioManager.playScore();
                    }

                    // Play once when the player loses.
                    if (previousState != GameState.FINISHED
                            && game.getState() == GameState.FINISHED) {
                        audioManager.playCollision();
                    }

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

            KeyCode pressedKey = event.getCode();

            if (game.getState() == GameState.PLAYING
                    && pressedKey == controlsSettings.getJumpKey(1)) {
                audioManager.playJump();
            }

            game.handleInput(pressedKey);

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

            if (!statsSaved) {
                statsService.recordGameOver(
                        1,
                        game.getPlayer().getScore()
                );

                statsSaved = true;
            }

            canvas.drawGameOver(
                    game.getPlayer().getScore()
            );
        } else {
            statsSaved = false;
        }
    }

    public void start() {
        requestFocus();
        gameLoop.start();
        audioManager.startMusic();
    }

    public void stop() {
        gameLoop.stop();
        audioManager.stopMusic();
    }
}
