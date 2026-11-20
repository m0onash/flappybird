package rom.diaz.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import rom.diaz.config.Localization;
import rom.diaz.model.Bird;
import rom.diaz.model.Obstacle;

import java.util.List;

public class GameCanvas extends Canvas {

    private static final double BIRD_RADIUS = 15;
    private final Localization localization;

    public GameCanvas(
            double width,
            double height,
            Localization localization
    ) {
        super(width, height);
        this.localization = localization;
    }

    public void render(
            Bird bird,
            List<Obstacle> obstacles,
            int score
    ) {
        GraphicsContext gc = getGraphicsContext2D();

        drawBackground(gc);
        drawObstacles(gc, obstacles);
        drawBird(gc, bird);
        drawScore(gc, score);
    }

    private void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.SKYBLUE);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawBird(GraphicsContext gc, Bird bird) {
        double diameter = BIRD_RADIUS * 2;

        gc.setFill(Color.GOLD);

        gc.fillOval(
                bird.getX() - BIRD_RADIUS,
                bird.getY() - BIRD_RADIUS,
                diameter,
                diameter
        );

        // Bird's eye
        gc.setFill(Color.BLACK);
        gc.fillOval(
                bird.getX() + 4,
                bird.getY() - 7,
                5,
                5
        );
    }

    private void drawObstacles(
            GraphicsContext gc,
            List<Obstacle> obstacles
    ) {
        gc.setFill(Color.FORESTGREEN);

        for (Obstacle obstacle : obstacles) {

            double x = obstacle.getX();
            double width = obstacle.getWidth();
            double gapY = obstacle.getGapY();
            double gapHeight = obstacle.getGapHeight();

            // Upper pipe
            gc.fillRect(
                    x,
                    0,
                    width,
                    gapY
            );

            // Lower pipe
            gc.fillRect(
                    x,
                    gapY + gapHeight,
                    width,
                    getHeight() - (gapY + gapHeight)
            );
        }
    }

    private void drawScore(GraphicsContext gc, int score) {
        gc.setFill(Color.WHITE);
        gc.setFont(
                javafx.scene.text.Font.font(28)
        );

        gc.fillText(
                localization.get("score") + ": " + score,
                20,
                40
        );
    }


    public void drawGameOver(int score) {

        GraphicsContext gc = getGraphicsContext2D();

        double centerX = getWidth() / 2;
        double centerY = getHeight() / 2;

        // Dark translucent overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.65));
        gc.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
        );

        gc.setFill(Color.WHITE);
        gc.setTextAlign(
                javafx.scene.text.TextAlignment.CENTER
        );

        gc.setFont(Font.font(48));
        gc.fillText(
                localization.get("gameOver"),
                centerX,
                centerY - 40
        );

        gc.setFont(Font.font(28));
        gc.fillText(
                localization.get("score") + ": " + score,
                centerX,
                centerY + 10
        );

        gc.setFont(Font.font(22));
        gc.fillText(
                localization.get("restart"),
                centerX,
                centerY + 55
        );

        gc.setTextAlign(
                javafx.scene.text.TextAlignment.LEFT
        );
    }

}
