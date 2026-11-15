package rom.diaz.logic;

import lombok.Getter;
import rom.diaz.model.Bird;
import rom.diaz.model.Obstacle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class ObstacleManager {

    private final List<Obstacle> obstacles = new ArrayList<>();
    private final Random random;
    private final double speed;
    private final double birdRadius;
    private int updatesSinceSpawn = 0;

    public ObstacleManager(double speed, double birdRadius) {
        this(speed, birdRadius, new Random());
    }

    ObstacleManager(double speed, double birdRadius,
                    Random random) {

        if (speed <= 0 || birdRadius <= 0) {
            throw new IllegalArgumentException(
                    "Speed and bird radius must be positive"
            );
        }

        this.speed = speed;
        this.birdRadius = birdRadius;
        this.random = java.util.Objects.requireNonNull(random);
    }

    public List<Obstacle> getObstacles() {
        return List.copyOf(obstacles);
    }

    public void addObstacle(Obstacle obstacle) {
        if (obstacle == null) {
            throw new IllegalArgumentException(
                    "Obstacle cannot be null"
            );
        }

        obstacles.add(obstacle);
    }

    public void update() {
        for (Obstacle obstacle : obstacles) {
            obstacle.update(speed);
        }

        obstacles.removeIf(
                obstacle -> obstacle.getX()
                        + obstacle.getWidth() < 0
        );
    }

    public void updateAndSpawn(
            double screenWidth,
            double screenHeight,
            double obstacleWidth,
            double gapHeight,
            int spawnInterval
    ) {
        validateGenerationSettings(
                screenWidth,
                screenHeight,
                obstacleWidth,
                gapHeight,
                spawnInterval
        );

        update();

        updatesSinceSpawn++;

        if (updatesSinceSpawn >= spawnInterval) {
            generateObstacle(
                    screenWidth,
                    screenHeight,
                    obstacleWidth,
                    gapHeight
            );

            updatesSinceSpawn = 0;
        }
    }

    public void generateObstacle(
            double screenWidth,
            double screenHeight,
            double obstacleWidth,
            double gapHeight
    ) {
        validateGenerationSettings(
                screenWidth,
                screenHeight,
                obstacleWidth,
                gapHeight,
                1
        );

        double margin = birdRadius * 2;

        double minGapY = margin;
        double maxGapY = screenHeight - gapHeight - margin;

        double gapY = minGapY
                + random.nextDouble() * (maxGapY - minGapY);

        obstacles.add(
                new Obstacle(
                        screenWidth,
                        gapY,
                        obstacleWidth,
                        gapHeight
                )
        );
    }

    private void validateGenerationSettings(
            double screenWidth,
            double screenHeight,
            double obstacleWidth,
            double gapHeight,
            int spawnInterval
    ) {
        if (!Double.isFinite(screenWidth)
                || !Double.isFinite(screenHeight)
                || !Double.isFinite(obstacleWidth)
                || !Double.isFinite(gapHeight)
                || screenWidth <= 0
                || screenHeight <= 0
                || obstacleWidth <= 0
                || gapHeight <= 0
                || spawnInterval <= 0) {

            throw new IllegalArgumentException(
                    "Invalid obstacle generation settings"
            );
        }

        if (gapHeight <= birdRadius * 2
                || gapHeight + birdRadius * 4
                > screenHeight) {

            throw new IllegalArgumentException(
                    "Gap does not fit the playable area"
            );
        }
    }

    public boolean checkCollision(Bird bird) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.collidesWith(bird, birdRadius)) {
                return true;
            }
        }

        return false;
    }

    public int countPassedObstacles(Bird bird) {
        int passedCount = 0;

        for (Obstacle obstacle : obstacles) {
            if (obstacle.isPassedBy(bird)) {
                passedCount++;
            }
        }

        return passedCount;
    }

    public void clear() {
        obstacles.clear();
        updatesSinceSpawn = 0;
    }
}