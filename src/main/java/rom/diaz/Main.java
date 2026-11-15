package rom.diaz;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import rom.diaz.core.Game;
import rom.diaz.core.GameState;
import rom.diaz.ui.GameCanvas;

public class Main extends Application {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

    private static final long TICK_NANOS =
            1_000_000_000L / 60;

    private AnimationTimer gameLoop;

    @Override
    public void start(Stage stage) {

        Game game = new Game();
        GameCanvas canvas = new GameCanvas(WIDTH, HEIGHT);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setOnKeyPressed(event ->
                game.handleInput(event.getCode())
        );

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
        };

        stage.setTitle("Flappy Sky");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(event -> gameLoop.stop());
        stage.show();

        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
