package rom.diaz.ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import rom.diaz.config.Localization;
import rom.diaz.model.Bird;
import rom.diaz.model.Obstacle;
import javafx.scene.image.Image;

import java.util.List;

public class GameCanvas extends Canvas {

    private static final double BIRD_RADIUS = 15;
    private final Localization localization;
    private double previousVelocityY = 0;
    private double spinAngle = 0;
    private boolean spinning = false;
    private final Image backgroundImage;
    private final Image birdImage;

    public GameCanvas(
            double width,
            double height,
            Localization localization

    ) {
        super(width, height);
        this.localization = localization;



        backgroundImage = new Image(
                getClass().getResourceAsStream("/images/flappy-sky-bg.png")
        );

        if (backgroundImage.isError()) {
            System.err.println(
                    "Background error: " + backgroundImage.getException()
            );
        }

        birdImage = new Image(
                getClass().getResourceAsStream("/images/flappy-sky-bird.png")
        );
    }

    public void render(
            Bird bird,
            List<Obstacle> obstacles,
            int score
    ) {
        GraphicsContext gc = getGraphicsContext2D();

        gc.clearRect(0, 0, getWidth(), getHeight());

        drawBackground(gc);
        drawObstacles(gc, obstacles);
        drawBird(gc, bird);
        drawScore(gc, score);
    }

    private void drawBackground(GraphicsContext gc) {
        System.out.println("Background loaded: "
                + !backgroundImage.isError());

        System.out.println("Background dimensions: "
                + backgroundImage.getWidth()
                + " x "
                + backgroundImage.getHeight());

        gc.drawImage(
                backgroundImage,
                0,
                0,
                getWidth(),
                getHeight()
        );
    }


    private void drawBird(GraphicsContext gc, Bird bird) {
        double spriteSize = 65;

        // Detect a jump from the sudden change in velocity.
        if (bird.getVelocityY() < previousVelocityY - 2) {
            spinning = true;
            spinAngle = 0;
        }

        previousVelocityY = bird.getVelocityY();

        if (spinning) {
            spinAngle += 45;

            if (spinAngle >= 360) {
                spinAngle = 0;
                spinning = false;
            }
        }

        gc.save();

        // Rotate around Don's center.
        gc.translate(bird.getX(), bird.getY());
        gc.rotate(spinAngle);

        gc.drawImage(
                birdImage,
                -spriteSize / 2,
                -spriteSize / 2,
                spriteSize,
                spriteSize
        );

        gc.restore();
    }



    private void drawObstacles(
            GraphicsContext gc,
            List<Obstacle> obstacles
    ) {
        Color pipeColor = Color.web("#A34BB0");
        Color pipeHighlight = Color.web("#E27BD7");
        Color pipeShadow = Color.web("#3A124A");

        for (Obstacle obstacle : obstacles) {
            double x = obstacle.getX();
            double width = obstacle.getWidth();
            double gapY = obstacle.getGapY();
            double gapHeight = obstacle.getGapHeight();

            double bottomY = gapY + gapHeight;
            double bottomHeight = getHeight() - bottomY;

            // Upper pipe
            drawCarnivalPipe(
                    gc, x, 0, width, gapY,
                    true,
                    pipeColor, pipeHighlight, pipeShadow
            );

            // Lower pipe
            drawCarnivalPipe(
                    gc, x, bottomY, width, bottomHeight,
                    false,
                    pipeColor, pipeHighlight, pipeShadow
            );
        }
    }

    private void drawCarnivalPipe(
            GraphicsContext gc,
            double x,
            double y,
            double width,
            double height,
            boolean upper,
            Color pipeColor,
            Color highlight,
            Color shadow
    ) {
        if (height <= 0) {
            return;
        }

        // Main pipe
        gc.setFill(shadow);
        gc.fillRect(x, y, width, height);

        gc.setFill(pipeColor);
        gc.fillRect(x + 5, y, width - 10, height);

        // Bright center highlight
        gc.setFill(highlight);
        gc.fillRect(x + width * 0.35, y, width * 0.25, height);

        // Pipe rim
        double rimHeight = Math.min(24, height);
        double rimY = upper
                ? y + height - rimHeight
                : y;

        gc.setFill(shadow);
        gc.fillRect(x, rimY, width, rimHeight);

        gc.setFill(highlight);
        gc.fillRect(
                x + 5,
                rimY + 4,
                width - 10,
                Math.max(0, rimHeight - 8)
        );
    }


    private void drawScore(GraphicsContext gc, int score) {
        double centerX = getWidth() / 2;

        gc.setTextAlign(
                javafx.scene.text.TextAlignment.CENTER
        );

        gc.setFont(Font.font("Arial", 18));
        gc.setFill(Color.web("#FFD3EC"));
        gc.fillText(
                localization.get("score").toUpperCase(),
                centerX,
                38
        );

        gc.setFont(Font.font("Arial", 48));

        gc.setFill(Color.web("#8B2B85"));
        gc.fillText(String.valueOf(score), centerX + 2, 87);

        gc.setFill(Color.WHITE);
        gc.fillText(String.valueOf(score), centerX, 84);

        gc.setTextAlign(
                javafx.scene.text.TextAlignment.LEFT
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
