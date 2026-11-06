package rom.diaz.logic;

import lombok.Getter;
import rom.diaz.model.Bird;
import rom.diaz.model.Obstacle;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ObstacleManager {

    private final List<Obstacle> obstacles = new ArrayList<>();

    private final double speed;

    private final double birdRadius;

    public ObstacleManager(double speed, double birdRadius) {
        if (speed <= 0 || birdRadius <= 0) {
            throw new IllegalArgumentException(
                    "Speed and bird radius must be positive"
            );
        }

        this.speed = speed;
        this.birdRadius = birdRadius;
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
                obstacle -> obstacle.getX() + obstacle.getWidth() < 0
        );
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
    }
}