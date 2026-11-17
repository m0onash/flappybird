
package rom.diaz.config;

import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

public class ControlsSettings {

    private final Map<Integer, KeyCode> jumpKeys =
            new HashMap<>();

    public ControlsSettings() {
        jumpKeys.put(1, KeyCode.SPACE);
        jumpKeys.put(2, KeyCode.W);
        jumpKeys.put(3, KeyCode.UP);
        jumpKeys.put(4, KeyCode.NUMPAD0);
    }

    public KeyCode getJumpKey(int playerId) {
        validatePlayerId(playerId);
        return jumpKeys.get(playerId);
    }

    public boolean setJumpKey(int playerId, KeyCode key) {
        validatePlayerId(playerId);

        if (key == null
                || key == KeyCode.R
                || key == KeyCode.ESCAPE
                || key == KeyCode.UNDEFINED
                || key.isModifierKey()) {
            return false;
        }

        for (Map.Entry<Integer, KeyCode> entry
                : jumpKeys.entrySet()) {

            if (entry.getKey() != playerId
                    && entry.getValue() == key) {
                return false;
            }
        }

        jumpKeys.put(playerId, key);
        return true;
    }

    private void validatePlayerId(int playerId) {
        if (playerId < 1 || playerId > 4) {
            throw new IllegalArgumentException(
                    "Player ID must be between 1 and 4"
            );
        }
    }
}
