package core;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import rom.diaz.config.ControlsSettings;
import rom.diaz.core.Game;
import rom.diaz.core.GameState;
import rom.diaz.core.MultiplayerGame;

import static org.junit.jupiter.api.Assertions.*;

class MultiplayerGameTest {

    @Test
    void shouldCreateTwoPlayers() {
        MultiplayerGame multiplayer = new MultiplayerGame(
                2,
                new ControlsSettings()
        );

        assertEquals(2, multiplayer.getPlayerCount());
        assertEquals(2, multiplayer.getGames().size());

        assertNotSame(
                multiplayer.getGames().get(0),
                multiplayer.getGames().get(1)
        );
    }

    @Test
    void shouldCreateFourPlayers() {
        MultiplayerGame multiplayer = new MultiplayerGame(
                4,
                new ControlsSettings()
        );

        assertEquals(4, multiplayer.getPlayerCount());
    }

    @Test
    void shouldRejectInvalidPlayerCounts() {
        ControlsSettings controls = new ControlsSettings();

        assertThrows(
                IllegalArgumentException.class,
                () -> new MultiplayerGame(1, controls)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new MultiplayerGame(5, controls)
        );
    }

    @Test
    void shouldUseDifferentBirds() {
        MultiplayerGame multiplayer = new MultiplayerGame(
                2,
                new ControlsSettings()
        );

        assertNotSame(
                multiplayer.getGames().get(0)
                        .getPlayer().getBird(),
                multiplayer.getGames().get(1)
                        .getPlayer().getBird()
        );
    }

    @Test
    void shouldUseConfiguredKeys() {
        ControlsSettings controls = new ControlsSettings();

        assertTrue(
                controls.setJumpKey(1, KeyCode.F)
        );

        MultiplayerGame multiplayer =
                new MultiplayerGame(2, controls);

        Game playerOneGame = multiplayer.getGames().get(0);
        Game playerTwoGame = multiplayer.getGames().get(1);

        multiplayer.handleInput(KeyCode.F);

        assertTrue(
                playerOneGame.getPlayer()
                        .getBird().getVelocityY() < 0
        );

        assertEquals(
                0.0,
                playerTwoGame.getPlayer()
                        .getBird().getVelocityY(),
                0.0001
        );
    }

    @Test
    void shouldNotEndEveryoneWhenOnePlayerLoses() {
        MultiplayerGame multiplayer = new MultiplayerGame(
                2,
                new ControlsSettings()
        );

        multiplayer.getGames().get(0).endGame();

        assertFalse(multiplayer.isFinished());

        assertEquals(
                GameState.PLAYING,
                multiplayer.getGames().get(1).getState()
        );
    }

    @Test
    void shouldFinishWhenEveryoneLoses() {
        MultiplayerGame multiplayer = new MultiplayerGame(
                2,
                new ControlsSettings()
        );

        multiplayer.getGames().get(0).endGame();
        multiplayer.getGames().get(1).endGame();

        assertTrue(multiplayer.isFinished());
    }

    @Test
    void shouldRestartAllPlayers() {
        MultiplayerGame multiplayer = new MultiplayerGame(
                2,
                new ControlsSettings()
        );

        multiplayer.getGames().get(0).endGame();
        multiplayer.getGames().get(1).endGame();

        multiplayer.handleInput(KeyCode.R);

        assertFalse(multiplayer.isFinished());

        for (Game game : multiplayer.getGames()) {
            assertEquals(
                    GameState.PLAYING,
                    game.getState()
            );
        }
    }
}
