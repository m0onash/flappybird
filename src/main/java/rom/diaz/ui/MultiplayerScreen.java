package rom.diaz.ui;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Scale;

import rom.diaz.config.ControlsSettings;
import rom.diaz.core.Game;
import rom.diaz.core.GameState;
import rom.diaz.core.MultiplayerGame;
import rom.diaz.service.StatsService;

import java.util.ArrayList;
import java.util.List;

public class MultiplayerScreen extends StackPane {

    private static final double WORLD_WIDTH = 800;
    private static final double WORLD_HEIGHT = 600;
    private static final long TICK_NANOS =
            1_000_000_000L / 60;

    private final MultiplayerGame multiplayer;
    private final StatsService statsService;
    private final boolean[] statsSaved;
    private final List<GameCanvas> canvases =
            new ArrayList<>();

    private final AnimationTimer gameLoop;

    public MultiplayerScreen(
            int playerCount,
            ControlsSettings controls,
            StatsService statsService,
            Runnable onBack
    ) {
        this.statsService = statsService;
        this.statsSaved = new boolean[playerCount];

        multiplayer = new MultiplayerGame(
                playerCount,
                controls
        );

        setStyle("-fx-background-color: #193451;");
        setFocusTraversable(true);

        GridPane grid = new GridPane();
        grid.setHgap(4);
        grid.setVgap(4);

        int columns = playerCount == 4 ? 2 : playerCount;
        int rows = playerCount == 4 ? 2 : 1;

        for (int i = 0; i < columns; i++) {
            ColumnConstraints column =
                    new ColumnConstraints();

            column.setPercentWidth(100.0 / columns);
            grid.getColumnConstraints().add(column);
        }

        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / rows);
            grid.getRowConstraints().add(row);
        }

        for (int i = 0; i < playerCount; i++) {
            StackPane cell = createPlayerCell(i + 1);

            int column = i % columns;
            int row = i / columns;

            grid.add(cell, column, row);
        }

        getChildren().add(grid);

        setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                stop();
                onBack.run();
            } else {
                multiplayer.handleInput(event.getCode());
            }

            event.consume();
        });

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
                    multiplayer.update();
                    accumulatedTime -= TICK_NANOS;
                }

                renderGames();
            }
        };

        renderGames();
    }

    private StackPane createPlayerCell(int playerId) {

        GameCanvas canvas = new GameCanvas(
                WORLD_WIDTH,
                WORLD_HEIGHT
        );

        canvases.add(canvas);

        Pane viewport = new Pane();
        viewport.setMinSize(0, 0);

        // Scale the existing 800x600 game world to fit
        // the available area without changing game physics.
        Scale scale = new Scale(1, 1, 0, 0);

        scale.xProperty().bind(
                viewport.widthProperty().divide(WORLD_WIDTH)
        );

        scale.yProperty().bind(
                viewport.heightProperty().divide(WORLD_HEIGHT)
        );

        canvas.getTransforms().add(scale);
        viewport.getChildren().add(canvas);

        // Keep each player's graphics inside their section.
        Rectangle clip = new Rectangle();

        clip.widthProperty().bind(viewport.widthProperty());
        clip.heightProperty().bind(viewport.heightProperty());

        viewport.setClip(clip);

        Label playerLabel = new Label("PLAYER " + playerId);

        playerLabel.setStyle(
                "-fx-background-color: #193451;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 15px;" +
                        "-fx-padding: 7 12;" +
                        "-fx-background-radius: 8;"
        );

        StackPane cell = new StackPane(
                viewport,
                playerLabel
        );

        cell.setMinSize(0, 0);

        StackPane.setAlignment(
                playerLabel,
                Pos.TOP_LEFT
        );

        StackPane.setMargin(
                playerLabel,
                new javafx.geometry.Insets(10)
        );

        return cell;
    }

    private void renderGames() {

        List<Game> games = multiplayer.getGames();

        for (int i = 0; i < games.size(); i++) {

            Game game = games.get(i);
            GameCanvas canvas = canvases.get(i);

            canvas.render(
                    game.getPlayer().getBird(),
                    game.getObstacleManager().getObstacles(),
                    game.getPlayer().getScore()
            );

            if (game.getState() == GameState.FINISHED) {

                if (!statsSaved[i]) {
                    statsService.recordGameOver(
                            i + 1,
                            game.getPlayer().getScore()
                    );

                    statsSaved[i] = true;
                }

                canvas.drawGameOver(
                        game.getPlayer().getScore()
                );

            } else {
                statsSaved[i] = false;
            }
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
