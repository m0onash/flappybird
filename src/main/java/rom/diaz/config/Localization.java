
package rom.diaz.config;

import java.util.Map;

public class Localization {

    private static final Map<String, Map<String, String>> TEXTS =
            Map.of(
                    "en", Map.ofEntries(
                            Map.entry("play", "Play"),
                            Map.entry("settings", "Settings"),
                            Map.entry("stats", "Statistics"),
                            Map.entry("back", "Back"),
                            Map.entry("solo", "Play Solo"),
                            Map.entry("multiplayer", "Multiplayer"),
                            Map.entry("start", "Start"),
                            Map.entry("createGame", "Create Game"),
                            Map.entry("language", "Language"),
                            Map.entry("resolution", "Resolution"),
                            Map.entry("music", "Music"),
                            Map.entry("sound", "Sound Effects"),
                            Map.entry("gameOver", "GAME OVER"),
                            Map.entry("restart", "Press R to restart")
                    ),
                    "es", Map.ofEntries(
                            Map.entry("play", "Jugar"),
                            Map.entry("settings", "Opciones"),
                            Map.entry("stats", "Estadísticas"),
                            Map.entry("back", "Menú"),
                            Map.entry("solo", "Jugar solo"),
                            Map.entry("multiplayer", "Multijugador"),
                            Map.entry("start", "Comenzar"),
                            Map.entry("createGame", "Crear partida"),
                            Map.entry("language", "Idioma"),
                            Map.entry("resolution", "Resolución"),
                            Map.entry("music", "Música"),
                            Map.entry("sound", "Efectos de sonido"),
                            Map.entry("gameOver", "FIN DEL JUEGO"),
                            Map.entry("restart", "Pulsa R para reiniciar")
                    )
            );

    private final GameSettings settings;

    public Localization(GameSettings settings) {
        this.settings = settings;
    }

    public String get(String key) {
        Map<String, String> translations =
                TEXTS.get(settings.getLanguage());

        return translations.getOrDefault(key, key);
    }
}
