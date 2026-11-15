
package rom.diaz.input;

import javafx.scene.input.KeyCode;
import rom.diaz.model.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InputHandler {

    private final Map<Integer, Player> players =
            new HashMap<>();

    private final Map<Integer, KeyBindings> bindings =
            new HashMap<>();

    public void registerPlayer(
            Player player,
            KeyBindings keyBindings
    ) {
        Objects.requireNonNull(player, "Player cannot be null");
        Objects.requireNonNull(
                keyBindings,
                "Key bindings cannot be null"
        );

        if (keyBindings.getJumpKey() == KeyCode.R) {
            throw new IllegalArgumentException(
                    "R is reserved for restarting"
            );
        }

        int playerId = player.getPlayerId();

        if (players.containsKey(playerId)) {
            throw new IllegalArgumentException(
                    "Player is already registered"
            );
        }

        if (checkKeyConflict(keyBindings.getJumpKey())) {
            throw new IllegalArgumentException(
                    "Jump key is already assigned"
            );
        }

        players.put(playerId, player);
        bindings.put(playerId, keyBindings);
    }

    public void handleInput(KeyCode key) {
        if (key == null) {
            return;
        }

        for (Map.Entry<Integer, KeyBindings> entry
                : bindings.entrySet()) {

            if (entry.getValue().getJumpKey() == key) {
                Player player = players.get(entry.getKey());

                if (player != null) {
                    player.jump();
                }
            }
        }
    }

    public void assignKey(int playerId, KeyCode key) {
        Objects.requireNonNull(key, "Key cannot be null");

        if (key == KeyCode.R) {
            throw new IllegalArgumentException(
                    "R is reserved for restarting"
            );
        }

        KeyBindings currentBindings =
                bindings.get(playerId);

        if (currentBindings == null) {
            throw new IllegalArgumentException(
                    "Player is not registered"
            );
        }

        if (currentBindings.getJumpKey() == key) {
            return;
        }

        if (checkKeyConflict(key)) {
            throw new IllegalArgumentException(
                    "Key is already assigned to another player"
            );
        }

        currentBindings.setJumpKey(key);
    }

    public boolean checkKeyConflict(KeyCode key) {
        for (KeyBindings keyBindings : bindings.values()) {
            if (keyBindings.getJumpKey() == key) {
                return true;
            }
        }

        return false;
    }


    public void updatePlayer(Player player) {
        Objects.requireNonNull(
                player,
                "Player cannot be null"
        );

        int playerId = player.getPlayerId();

        if (!players.containsKey(playerId)) {
            throw new IllegalArgumentException(
                    "Player is not registered"
            );
        }

        players.put(playerId, player);
    }



}
