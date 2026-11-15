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
    @Test
    void shouldGenerateObstacle() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        manager.generateObstacle(800, 600, 60, 150);

        assertEquals(1, manager.getObstacles().size());

        Obstacle obstacle = manager.getObstacles().get(0);

        assertEquals(800.0, obstacle.getX());
        assertEquals(60.0, obstacle.getWidth());
        assertEquals(150.0, obstacle.getGapHeight());
    }

    @Test
    void generatedGapShouldStayWithinBounds() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        for (int i = 0; i < 100; i++) {
            manager.generateObstacle(800, 600, 60, 150);
        }

        for (Obstacle obstacle : manager.getObstacles()) {
            assertTrue(obstacle.getGapY() >= 20);
            assertTrue(
                    obstacle.getGapY() + obstacle.getGapHeight()
                            <= 580
            );
        }
    }

    @Test
    void shouldSpawnAfterConfiguredInterval() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        for (int i = 0; i < 9; i++) {
            manager.updateAndSpawn(800, 600, 60, 150, 10);
        }

        assertEquals(0, manager.getObstacles().size());

        manager.updateAndSpawn(800, 600, 60, 150, 10);

        assertEquals(1, manager.getObstacles().size());
    }

    @Test
    void shouldRejectImpossibleGap() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.generateObstacle(800, 100, 60, 90)
        );
    }

    @Test
    void clearShouldResetSpawnCounter() {
        ObstacleManager manager = new ObstacleManager(5, 10);

        for (int i = 0; i < 9; i++) {
            manager.updateAndSpawn(800, 600, 60, 150, 10);
        }

        manager.clear();

        manager.updateAndSpawn(800, 600, 60, 150, 10);

        assertTrue(manager.getObstacles().isEmpty());
    }
}