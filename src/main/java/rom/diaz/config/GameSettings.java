package rom.diaz.config;

import lombok.Getter;

import java.util.Set;

@Getter
public class GameSettings {

    private static final Set<String> SUPPORTED_LANGUAGES =
            Set.of("en", "es");

    private String language;
    private int windowWidth;
    private int windowHeight;

    private boolean soundEnabled;
    private boolean musicEnabled;

    public GameSettings() {
        language = "en";
        windowWidth = 800;
        windowHeight = 600;
        soundEnabled = true;
        musicEnabled = true;
    }

    public void changeLanguage(String language) {
        if (!SUPPORTED_LANGUAGES.contains(language)) {
            throw new IllegalArgumentException(
                    "Unsupported language: " + language
            );
        }

        this.language = language;
    }

    public void changeResolution(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Resolution must be positive"
            );
        }

        windowWidth = width;
        windowHeight = height;
    }

    public void toggleSound() {
        soundEnabled = !soundEnabled;
    }

    public void toggleMusic() {
        musicEnabled = !musicEnabled;
    }
}
