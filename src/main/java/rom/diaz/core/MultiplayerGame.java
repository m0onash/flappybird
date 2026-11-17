
package rom.diaz.core;

import javafx.scene.input.KeyCode;
import rom.diaz.config.ControlsSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MultiplayerGame {

    private final List<Game> games = new ArrayList<>();
    private final ControlsSettings controlsSettings;

    public MultiplayerGame(
            int playerCount,
            ControlsSettings controlsSettings
    ) {
        if (playerCount < 2 || playerCount > 4) {
            throw new IllegalArgumentException(
                    "Multiplayer requires 2 to 4 players"
            );
        }

        if (controlsSettings == null) {
            throw new IllegalArgumentException(
                    "ControlsSettings cannot be null"
            );
        }

        this.controlsSettings = controlsSettings;

        for (int i = 0; i < playerCount; i++) {
            Game game = new Game();

            game.getInputHandler().assignKey(
                    1,
                    controlsSettings.getJumpKey(i + 1)
            );

            games.add(game);
        }
    }

    public List<Game> getGames() {
        return Collections.unmodifiableList(games);
    }

    public int getPlayerCount() {
        return games.size();
    }

    public void update() {
        for (Game game : games) {
            if (game.getState() == GameState.PLAYING) {
                game.updateGame();
            }
        }
    }

    public void handleInput(KeyCode key) {
        // R restarts everyone only after all players finish.
        if (key == KeyCode.R && isFinished()) {
            restart();
            return;
        }

        for (Game game : games) {
            if (game.getState() == GameState.PLAYING) {
                game.handleInput(key);
            }
        }
    }

    public boolean isFinished() {
        return games.stream()
                .allMatch(game ->
                        game.getState() == GameState.FINISHED
                );
    }

    public void restart() {
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);

            game.startGame();

            game.getInputHandler().assignKey(
                    1,
                    controlsSettings.getJumpKey(i + 1)
            );
        }
    }
}
