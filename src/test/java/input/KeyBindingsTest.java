package input;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import rom.diaz.input.KeyBindings;

import static org.junit.jupiter.api.Assertions.*;

class KeyBindingsTest {

    @Test
    void shouldStoreInitialJumpKey() {
        KeyBindings bindings =
                new KeyBindings(KeyCode.SPACE);

        assertEquals(
                KeyCode.SPACE,
                bindings.getJumpKey()
        );
    }

    @Test
    void shouldAllowChangingJumpKey() {
        KeyBindings bindings =
                new KeyBindings(KeyCode.SPACE);

        bindings.setJumpKey(KeyCode.W);

        assertEquals(
                KeyCode.W,
                bindings.getJumpKey()
        );
    }

    @Test
    void shouldAcceptDifferentKeys() {
        KeyBindings bindings =
                new KeyBindings(KeyCode.UP);

        bindings.setJumpKey(KeyCode.F);

        assertEquals(
                KeyCode.F,
                bindings.getJumpKey()
        );
    }

    @Test
    void shouldRejectNullKey() {
        assertThrows(
                NullPointerException.class,
                () -> new KeyBindings(null)
        );
    }

    @Test
    void shouldRejectChangingKeyToNull() {
        KeyBindings bindings =
                new KeyBindings(KeyCode.SPACE);

        assertThrows(
                NullPointerException.class,
                () -> bindings.setJumpKey(null)
        );

        assertEquals(
                KeyCode.SPACE,
                bindings.getJumpKey()
        );
    }
}
