package input;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import rom.diaz.input.InputHandler;
import rom.diaz.input.KeyBindings;
import rom.diaz.model.Bird;
import rom.diaz.model.Player;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {

    @Test
    void shouldRegisterPlayer() {
        InputHandler handler = new InputHandler();

        handler.registerPlayer(
                new Player(1, new Bird(100, 200)),
                new KeyBindings(KeyCode.SPACE)
        );

        assertTrue(handler.checkKeyConflict(KeyCode.SPACE));
    }

    @Test
    void shouldMakeCorrectPlayerJump() {
        InputHandler handler = new InputHandler();

        Bird bird1 = new Bird(100, 200);
        Bird bird2 = new Bird(100, 200);

        handler.registerPlayer(
                new Player(1, bird1),
                new KeyBindings(KeyCode.SPACE)
        );

        handler.registerPlayer(
                new Player(2, bird2),
                new KeyBindings(KeyCode.W)
        );

        handler.handleInput(KeyCode.W);

        assertEquals(0.0, bird1.getVelocityY());
        assertTrue(bird2.getVelocityY() < 0);
    }

    @Test
    void shouldDetectKeyConflict() {
        InputHandler handler = new InputHandler();

        handler.registerPlayer(
                new Player(1, new Bird(100, 200)),
                new KeyBindings(KeyCode.SPACE)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> handler.registerPlayer(
                        new Player(2, new Bird(100, 200)),
                        new KeyBindings(KeyCode.SPACE)
                )
        );
    }

    @Test
    void shouldAllowChangingKey() {
        InputHandler handler = new InputHandler();

        Bird bird = new Bird(100, 200);

        handler.registerPlayer(
                new Player(1, bird),
                new KeyBindings(KeyCode.SPACE)
        );

        handler.assignKey(1, KeyCode.F);

        handler.handleInput(KeyCode.F);

        assertTrue(bird.getVelocityY() < 0);
        assertFalse(handler.checkKeyConflict(KeyCode.SPACE));
    }

    @Test
    void shouldRejectConflictingReassignment() {
        InputHandler handler = new InputHandler();

        handler.registerPlayer(
                new Player(1, new Bird(100, 200)),
                new KeyBindings(KeyCode.SPACE)
        );

        handler.registerPlayer(
                new Player(2, new Bird(100, 200)),
                new KeyBindings(KeyCode.W)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> handler.assignKey(2, KeyCode.SPACE)
        );

        assertTrue(handler.checkKeyConflict(KeyCode.W));
    }

    @Test
    void shouldRejectDuplicatePlayerId() {
        InputHandler handler = new InputHandler();

        handler.registerPlayer(
                new Player(1, new Bird(100, 200)),
                new KeyBindings(KeyCode.SPACE)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> handler.registerPlayer(
                        new Player(1, new Bird(100, 200)),
                        new KeyBindings(KeyCode.F)
                )
        );
    }


    @Test
    void shouldUpdateRegisteredPlayer() {
        InputHandler handler = new InputHandler();

        handler.registerPlayer(
                new Player(1, new Bird(100, 200)),
                new KeyBindings(KeyCode.SPACE)
        );

        Bird newBird = new Bird(100, 200);

        handler.updatePlayer(
                new Player(1, newBird)
        );

        handler.handleInput(KeyCode.SPACE);

        assertTrue(newBird.getVelocityY() < 0);
    }

    @Test
    void shouldRejectReservedRestartKey() {
        InputHandler handler = new InputHandler();

        handler.registerPlayer(
                new Player(1, new Bird(100, 200)),
                new KeyBindings(KeyCode.SPACE)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> handler.assignKey(1, KeyCode.R)
        );
    }

    @Test
    void shouldRejectReservedKeyDuringRegistration() {
        InputHandler handler = new InputHandler();

        assertThrows(
                IllegalArgumentException.class,
                () -> handler.registerPlayer(
                        new Player(1, new Bird(100, 200)),
                        new KeyBindings(KeyCode.R)
                )
        );
    }

}
