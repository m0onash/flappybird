package config;

import org.junit.jupiter.api.Test;
import rom.diaz.config.GameSettings;

import static org.junit.jupiter.api.Assertions.*;

class GameSettingsTest {

    @Test
    void shouldUseDefaultSettings() {
        GameSettings settings = new GameSettings();

        assertEquals("en", settings.getLanguage());
        assertEquals(800, settings.getWindowWidth());
        assertEquals(600, settings.getWindowHeight());
        assertTrue(settings.isSoundEnabled());
    }

    @Test
    void shouldChangeLanguageToSpanish() {
        GameSettings settings = new GameSettings();

        settings.changeLanguage("es");

        assertEquals("es", settings.getLanguage());
    }

    @Test
    void shouldChangeLanguageBackToEnglish() {
        GameSettings settings = new GameSettings();

        settings.changeLanguage("es");
        settings.changeLanguage("en");

        assertEquals("en", settings.getLanguage());
    }

    @Test
    void shouldRejectUnsupportedLanguage() {
        GameSettings settings = new GameSettings();

        assertThrows(
                IllegalArgumentException.class,
                () -> settings.changeLanguage("fr")
        );

        assertEquals("en", settings.getLanguage());
    }

    @Test
    void shouldChangeResolution() {
        GameSettings settings = new GameSettings();

        settings.changeResolution(1280, 720);

        assertEquals(1280, settings.getWindowWidth());
        assertEquals(720, settings.getWindowHeight());
    }

    @Test
    void shouldRejectInvalidResolution() {
        GameSettings settings = new GameSettings();

        assertThrows(
                IllegalArgumentException.class,
                () -> settings.changeResolution(-800, 600)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> settings.changeResolution(800, 0)
        );

        assertEquals(800, settings.getWindowWidth());
        assertEquals(600, settings.getWindowHeight());
    }

    @Test
    void shouldToggleSoundOff() {
        GameSettings settings = new GameSettings();

        settings.toggleSound();

        assertFalse(settings.isSoundEnabled());
    }

    @Test
    void shouldToggleSoundBackOn() {
        GameSettings settings = new GameSettings();

        settings.toggleSound();
        settings.toggleSound();

        assertTrue(settings.isSoundEnabled());
    }


    @Test
    void shouldToggleMusicOff() {
        GameSettings settings = new GameSettings();

        settings.toggleMusic();

        assertFalse(settings.isMusicEnabled());
        assertTrue(settings.isSoundEnabled());
    }

    @Test
    void shouldToggleMusicIndependentlyFromSoundEffects() {
        GameSettings settings = new GameSettings();

        settings.toggleSound();

        assertFalse(settings.isSoundEnabled());
        assertTrue(settings.isMusicEnabled());

        settings.toggleMusic();

        assertFalse(settings.isSoundEnabled());
        assertFalse(settings.isMusicEnabled());
    }

}
