package config;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import rom.diaz.config.ControlsSettings;

import static org.junit.jupiter.api.Assertions.*;

class ControlsSettingsTest {

    @Test
    void shouldHaveDefaultKeys() {
        ControlsSettings controls = new ControlsSettings();

        assertEquals(KeyCode.SPACE, controls.getJumpKey(1));
        assertEquals(KeyCode.W, controls.getJumpKey(2));
        assertEquals(KeyCode.UP, controls.getJumpKey(3));
        assertEquals(KeyCode.NUMPAD0, controls.getJumpKey(4));
    }

    @Test
    void shouldAllowChangingJumpKey() {
        ControlsSettings controls = new ControlsSettings();

        assertTrue(controls.setJumpKey(1, KeyCode.F));

        assertEquals(
                KeyCode.F,
                controls.getJumpKey(1)
        );
    }

    @Test
    void shouldRejectDuplicateKeys() {
        ControlsSettings controls = new ControlsSettings();

        assertFalse(
                controls.setJumpKey(1, KeyCode.W)
        );

        assertEquals(
                KeyCode.SPACE,
                controls.getJumpKey(1)
        );
    }

    @Test
    void shouldRejectReservedKeys() {
        ControlsSettings controls = new ControlsSettings();

        assertFalse(controls.setJumpKey(1, KeyCode.R));
        assertFalse(controls.setJumpKey(1, KeyCode.ESCAPE));
        assertFalse(controls.setJumpKey(1, KeyCode.SHIFT));
    }

    @Test
    void shouldRejectInvalidPlayerIds() {
        ControlsSettings controls = new ControlsSettings();

        assertThrows(
                IllegalArgumentException.class,
                () -> controls.getJumpKey(5)
        );
    }
}
