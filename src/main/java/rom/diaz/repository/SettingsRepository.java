package rom.diaz.repository;

import javafx.scene.input.KeyCode;
import rom.diaz.config.ControlsSettings;
import rom.diaz.config.GameSettings;

import java.util.prefs.Preferences;

public class SettingsRepository {

    private final Preferences preferences =
            Preferences.userNodeForPackage(SettingsRepository.class);

    public void save(
            GameSettings settings,
            ControlsSettings controls
    ) {
        preferences.put(
                "language",
                settings.getLanguage()
        );

        preferences.putInt(
                "width",
                settings.getWindowWidth()
        );

        preferences.putInt(
                "height",
                settings.getWindowHeight()
        );

        preferences.putBoolean(
                "sound",
                settings.isSoundEnabled()
        );

        preferences.putBoolean(
                "music",
                settings.isMusicEnabled()
        );

        for (int i = 1; i <= 4; i++) {
            preferences.put(
                    "player" + i + "Key",
                    controls.getJumpKey(i).name()
            );
        }
    }

    public void load(
            GameSettings settings,
            ControlsSettings controls
    ) {
        settings.changeLanguage(
                preferences.get("language", "en")
        );

        settings.changeResolution(
                preferences.getInt("width", 800),
                preferences.getInt("height", 600)
        );

        boolean savedSound =
                preferences.getBoolean("sound", true);

        if (settings.isSoundEnabled() != savedSound) {
            settings.toggleSound();
        }

        boolean savedMusic =
                preferences.getBoolean("music", true);

        if (settings.isMusicEnabled() != savedMusic) {
            settings.toggleMusic();
        }

        for (int i = 1; i <= 4; i++) {
            String keyName = preferences.get(
                    "player" + i + "Key",
                    controls.getJumpKey(i).name()
            );

            try {
                controls.setJumpKey(
                        i,
                        KeyCode.valueOf(keyName)
                );
            } catch (IllegalArgumentException exception) {
                // Keep the default key if saved data is invalid.
            }
        }
    }
}
