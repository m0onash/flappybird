package model;

import org.junit.jupiter.api.Test;
import rom.diaz.model.Bird;
import rom.diaz.model.Obstacle;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleTest {

    @Test
    void obstacleShouldMoveLeft() {
        Obstacle obstacle = new Obstacle(300, 100, 60, 120);

        obstacle.update(5);

        assertEquals(295.0, obstacle.getX());
    }

    @Test
    void obstacleShouldDetectCollision() {
        Obstacle obstacle = new Obstacle(100, 100, 60, 120);
        Bird bird = new Bird(130, 80);

        assertTrue(obstacle.collidesWith(bird, 10));
    }

    @Test
    void birdInsideGapShouldNotCollide() {
        Obstacle obstacle = new Obstacle(100, 100, 60, 120);
        Bird bird = new Bird(130, 160);

        assertFalse(obstacle.collidesWith(bird, 10));
    }

    @Test
    void obstacleShouldRegisterPassedBirdOnce() {
        Obstacle obstacle = new Obstacle(100, 100, 60, 120);
        Bird bird = new Bird(200, 160);

        assertTrue(obstacle.isPassedBy(bird));
        assertFalse(obstacle.isPassedBy(bird));
    }

    @Test
    void obstacleShouldRejectInvalidDimensions() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Obstacle(100, 100, 0, 120)
        );
    }
}