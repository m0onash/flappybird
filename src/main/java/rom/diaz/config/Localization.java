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
                            Map.entry("restart", "Press R to restart"),
                            Map.entry("player", "Player"),
                            Map.entry("players", "Players"),
                            Map.entry("bestScore", "Best Score"),
                            Map.entry("obstaclesAvoided", "Obstacles Avoided"),
                            Map.entry("timesLost", "Times Lost"),
                            Map.entry("playerStats", "PLAYER STATISTICS"),
                            Map.entry("score", "Score"),
                            Map.entry("jumpKey", "Jump Key"),
                            Map.entry("pressKey", "Press a key..."),
                            Map.entry("on", "ON"),
                            Map.entry("off", "OFF"),
                            Map.entry("selectPlayers", "Select number of players"),
                            Map.entry("controls", "Controls"),
                            Map.entry("pressEscape", "Press ESC to return to menu"),
                            Map.entry("allPlayersLost", "All players lost!"),
                            Map.entry("chooseMode", "CHOOSE YOUR GAME MODE"),
                            Map.entry("readyToFly", "Ready to Fly?"),
                            Map.entry("modeDescription", "Choose how you want to play!"),
                            Map.entry("soloDescription", "Fly solo and beat your high score!"),
                            Map.entry("multiDescription", "Play with 2 to 4 players!"),
                            Map.entry("flightTip", "Tip: Avoid the pipes and keep flying!"),
                            Map.entry("settingsSubtitle", "YOUR WAY"),
                            Map.entry("pressAnyKey", "Press any key..."),
                            Map.entry("keyUnavailable", "This key is reserved or already assigned.")


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
                            Map.entry("restart", "Pulsa R para reiniciar"),
                            Map.entry("player", "Jugador"),
                            Map.entry("players", "Jugadores"),
                            Map.entry("bestScore", "Mejor puntuación"),
                            Map.entry("obstaclesAvoided", "Obstáculos evitados"),
                            Map.entry("timesLost", "Veces perdidas"),
                            Map.entry("playerStats", "ESTADÍSTICAS DE JUGADORES"),
                            Map.entry("score", "Puntuación"),
                            Map.entry("jumpKey", "Tecla de salto"),
                            Map.entry("pressKey", "Pulsa una tecla..."),
                            Map.entry("on", "ACTIVADO"),
                            Map.entry("off", "DESACTIVADO"),
                            Map.entry("selectPlayers", "Selecciona el número de jugadores"),
                            Map.entry("controls", "Controles"),
                            Map.entry("pressEscape", "Pulsa ESC para volver al menú"),
                            Map.entry("allPlayersLost", "¡Todos los jugadores perdieron!"),
                            Map.entry("chooseMode", "ELIGE TU MODO DE JUEGO"),
                            Map.entry("readyToFly", "¿Listo para volar?"),
                            Map.entry("modeDescription", "¡Elige cómo quieres jugar!"),
                            Map.entry("soloDescription", "¡Vuela solo y supera tu récord!"),
                            Map.entry("multiDescription", "¡Juega con 2 a 4 jugadores!"),
                            Map.entry("flightTip", "Consejo: ¡Esquiva las tuberías y sigue volando!"),
                            Map.entry("settingsSubtitle", "A TU MANERA"),
                            Map.entry("pressAnyKey", "Pulsa cualquier tecla..."),
                            Map.entry("keyUnavailable", "Esta tecla está reservada o ya está asignada.")


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
