package rom.diaz.input;

import javafx.scene.input.KeyCode;
import lombok.Getter;

import java.util.Objects;

@Getter
public class KeyBindings {

    private KeyCode jumpKey;

    public KeyBindings(KeyCode jumpKey) {
        setJumpKey(jumpKey);
    }

    public void setJumpKey(KeyCode jumpKey) {
        this.jumpKey = Objects.requireNonNull(
                jumpKey,
                "Jump key cannot be null"
        );
    }
}
