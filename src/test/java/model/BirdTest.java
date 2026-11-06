package model;

import org.junit.jupiter.api.Test;
import rom.diaz.model.Bird;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BirdTest {

    @Test
    void birdShouldStartWithZeroVelocity() {
        Bird bird = new Bird(100, 250);

        assertEquals(100.0, bird.getX());
        assertEquals(250.0, bird.getY());
        assertEquals(0.0, bird.getVelocityY());
    }

    @Test
    void birdShouldJumpUpwards() {
        Bird bird = new Bird(100, 250);

        bird.jump();

        assertTrue(bird.getVelocityY() < 0);
    }

    @Test
    void gravityShouldIncreaseVerticalVelocity() {
        Bird bird = new Bird(100, 250);

        bird.update();

        assertTrue(bird.getVelocityY() > 0);
    }

    @Test
    void birdShouldFallWhenUpdated() {
        Bird bird = new Bird(100, 250);

        bird.update();

        assertTrue(bird.getY() > 250);
    }

    @Test
    void birdShouldMoveUpAfterJumping() {
        Bird bird = new Bird(100, 250);

        bird.jump();
        bird.update();

        assertTrue(bird.getY() < 250);
    }

    @Test
    void birdShouldEventuallyFallAfterJumping() {
        Bird bird = new Bird(100, 250);

        bird.jump();

        for (int i = 0; i < 100; i++) {
            bird.update();
        }

        assertTrue(bird.getVelocityY() > 0);
    }
}