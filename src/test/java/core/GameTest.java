package core;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import rom.diaz.core.Game;
import rom.diaz.core.GameState;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void shouldStartPlaying() {
        Game game = new Game();

        assertEquals(GameState.PLAYING, game.getState());
        assertEquals(0, game.getPlayer().getScore());
    }

    @Test
    void shouldFinishGame() {
        Game game = new Game();

        game.endGame();

        assertEquals(GameState.FINISHED, game.getState());
    }

    @Test
    void shouldRestartAfterGameOver() {
        Game game = new Game();

        game.endGame();
        game.handleInput(KeyCode.R);

        assertEquals(GameState.PLAYING, game.getState());
        assertEquals(0, game.getPlayer().getScore());
    }

    @Test
    void shouldNotRestartDuringGameplay() {
        Game game = new Game();

        var originalPlayer = game.getPlayer();

        game.handleInput(KeyCode.R);

        assertSame(originalPlayer, game.getPlayer());
    }

    @Test
    void shouldResetObstaclesOnRestart() {
        Game game = new Game();

        var originalManager = game.getObstacleManager();

        game.endGame();
        game.handleInput(KeyCode.R);

        assertNotSame(
                originalManager,
                game.getObstacleManager()
        );
    }
}
