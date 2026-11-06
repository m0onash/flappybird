package logic;

import org.junit.jupiter.api.Test;
import rom.diaz.logic.ObstacleManager;
import rom.diaz.model.Bird;
import rom.diaz.model.Obstacle;

import static org.junit.jupiter.api.Assertions.*;

class ObstacleManagerTest {

    @Test
    void shouldStartWithNoObstacles() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        assertTrue(manager.getObstacles().isEmpty());
    }

    @Test
    void shouldAddObstacle() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        manager.addObstacle(
                new Obstacle(300, 100, 60, 120)
        );

        assertEquals(1, manager.getObstacles().size());
    }

    @Test
    void shouldMoveAllObstacles() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        manager.addObstacle(
                new Obstacle(300, 100, 60, 120)
        );

        manager.update();

        assertEquals(
                295.0,
                manager.getObstacles().get(0).getX()
        );
    }

    @Test
    void shouldRemoveOffscreenObstacles() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        manager.addObstacle(
                new Obstacle(-60, 100, 50, 120)
        );

        manager.update();

        assertTrue(manager.getObstacles().isEmpty());
    }

    @Test
    void shouldDetectCollision() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        manager.addObstacle(
                new Obstacle(100, 100, 60, 120)
        );

        Bird bird = new Bird(130, 80);

        assertTrue(manager.checkCollision(bird));
    }

    @Test
    void shouldNotDetectCollisionInsideGap() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        manager.addObstacle(
                new Obstacle(100, 100, 60, 120)
        );

        Bird bird = new Bird(130, 160);

        assertFalse(manager.checkCollision(bird));
    }

    @Test
    void shouldCountPassedObstacleOnlyOnce() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        manager.addObstacle(
                new Obstacle(100, 100, 60, 120)
        );

        Bird bird = new Bird(200, 160);

        assertEquals(1, manager.countPassedObstacles(bird));
        assertEquals(0, manager.countPassedObstacles(bird));
    }

    @Test
    void shouldClearObstacles() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        manager.addObstacle(
                new Obstacle(300, 100, 60, 120)
        );

        manager.clear();

        assertTrue(manager.getObstacles().isEmpty());
    }
}