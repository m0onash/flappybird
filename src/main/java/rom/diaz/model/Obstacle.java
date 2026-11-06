package rom.diaz.model;

import lombok.Getter;

@Getter

public class Obstacle {

    private double x;
    private final double gapY;
    private final double width;
    private final double gapHeight;

    private boolean passed;

    public Obstacle(double x, double gapY,
                    double width, double gapHeight) {

        if (width <= 0 || gapHeight <= 0) {
            throw new IllegalArgumentException(
            );
        }

        this.x = x;
        this.gapY = gapY;
        this.width = width;
        this.gapHeight = gapHeight;
        this.passed = false;
    }

    public void update(double speed) {
        x -= speed;
    }

    public boolean isPassedBy(Bird bird) {
        if (!passed && bird.getX() > x + width) {
            passed = true;
            return true;
        }

        return false;
    }

    public boolean collidesWith(Bird bird, double birdRadius) {
        double birdLeft = bird.getX() - birdRadius;
        double birdRight = bird.getX() + birdRadius;
        double birdTop = bird.getY() - birdRadius;
        double birdBottom = bird.getY() + birdRadius;

        boolean overlapsHorizontally =
                birdRight > x && birdLeft < x + width;

        boolean touchesPipe =
                birdTop < gapY ||
                        birdBottom > gapY + gapHeight;

        return overlapsHorizontally && touchesPipe;
    }
}