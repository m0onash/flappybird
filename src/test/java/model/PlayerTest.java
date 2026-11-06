package model;

import org.junit.jupiter.api.Test;
import rom.diaz.model.Bird;
import rom.diaz.model.Player;
import rom.diaz.model.PlayerState;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

    @Test
    void playerShouldStartAlive() {
        Bird bird = new Bird(100, 250);
        Player player = new Player(1, bird);

        assertEquals(PlayerState.ALIVE, player.getState());
        assertEquals(0, player.getScore());
    }

    @Test
    void playerShouldIncreaseScore() {
        Player player = new Player(1, new Bird(100, 250));

        player.increaseScore();

        assertEquals(1, player.getScore());
    }

    @Test
    void playerShouldStopScoringAfterGameOver() {
        Player player = new Player(1, new Bird(100, 250));

        player.finishRun();
        player.increaseScore();

        assertEquals(PlayerState.GAME_OVER, player.getState());
        assertEquals(0, player.getScore());
    }

    @Test
    void playerShouldNotJumpAfterGameOver() {
        Bird bird = new Bird(100, 250);
        Player player = new Player(1, bird);

        player.finishRun();
        player.jump();

        assertEquals(0.0, bird.getVelocityY());
    }

    @Test
    void playerShouldNotUpdateAfterGameOver() {
        Bird bird = new Bird(100, 250);
        Player player = new Player(1, bird);

        player.finishRun();
        player.update();

        assertEquals(250.0, bird.getY());
    }
}