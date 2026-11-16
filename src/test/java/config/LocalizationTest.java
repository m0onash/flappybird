package config;

import org.junit.jupiter.api.Test;
import rom.diaz.config.GameSettings;
import rom.diaz.config.Localization;

import static org.junit.jupiter.api.Assertions.*;

class LocalizationTest {

    @Test
    void shouldTranslateToEnglishByDefault() {
        GameSettings settings = new GameSettings();
        Localization localization = new Localization(settings);

        assertEquals("Play", localization.get("play"));
        assertEquals("Settings", localization.get("settings"));
    }

    @Test
    void shouldTranslateToSpanish() {
        GameSettings settings = new GameSettings();
        settings.changeLanguage("es");

        Localization localization = new Localization(settings);

        assertEquals("Jugar", localization.get("play"));
        assertEquals("Opciones", localization.get("settings"));
    }

    @Test
    void shouldReflectLanguageChanges() {
        GameSettings settings = new GameSettings();
        Localization localization = new Localization(settings);

        assertEquals("Play", localization.get("play"));

        settings.changeLanguage("es");

        assertEquals("Jugar", localization.get("play"));
    }

    @Test
    void shouldReturnKeyWhenTranslationIsMissing() {
        GameSettings settings = new GameSettings();
        Localization localization = new Localization(settings);

        assertEquals(
                "unknownKey",
                localization.get("unknownKey")
        );
    }
}
