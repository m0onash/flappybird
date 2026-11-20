package rom.diaz;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import rom.diaz.config.ControlsSettings;
import rom.diaz.config.GameSettings;
import rom.diaz.config.Localization;
import rom.diaz.repository.SettingsRepository;
import rom.diaz.ui.*;

import rom.diaz.repository.LocalStatsRepository;
import rom.diaz.service.StatsService;
import java.nio.file.Path;

public class Main extends Application {

    private Stage stage;
    private Scene scene;
    private GameScreen activeGameScreen;
    private MultiplayerScreen activeMultiplayerScreen;
    private final GameSettings settings = new GameSettings();
    private final Localization localization = new Localization(settings);
    private final ControlsSettings controlsSettings = new ControlsSettings();
    private final SettingsRepository settingsRepository = new SettingsRepository();
    private StatsService statsService;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        settingsRepository.load(settings, controlsSettings);
        Path databasePath = Path.of(
                System.getProperty("user.home"),
                "FlappySky",
                "stats"
        );

        try {
            java.nio.file.Files.createDirectories(
                    databasePath.getParent()
            );
        } catch (java.io.IOException exception) {
            throw new IllegalStateException(
                    "Could not create database directory",
                    exception
            );
        }

        statsService = new StatsService(
                new LocalStatsRepository(
                        "jdbc:h2:file:" + databasePath.toAbsolutePath()
                )
        );
        MainMenuScreen menu = createMainMenu();

        scene = new Scene(
                menu,
                settings.getWindowWidth(),
                settings.getWindowHeight()
        );

        scene.getStylesheets().add(
                getClass()
                        .getResource("/css/menu.css")
                        .toExternalForm()
        );

        stage.setTitle("Flappy Sky");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);


        stage.setOnCloseRequest(event -> {

            settingsRepository.save(settings, controlsSettings);

            if (activeGameScreen != null) {
                activeGameScreen.stop();
            }

            if (activeMultiplayerScreen != null) {
                activeMultiplayerScreen.stop();
            }
        });


        stage.show();
    }


    private void showSettings() {

        SettingsScreen settingsScreen = new SettingsScreen(
                settings,
                localization,
                controlsSettings,
                this::showMainMenu,
                () -> {
                    stage.setWidth(settings.getWindowWidth());
                    stage.setHeight(settings.getWindowHeight());
                },
                this::showSettings
        );

        scene.setRoot(settingsScreen);
    }




    private MainMenuScreen createMainMenu() {
        return new MainMenuScreen(
                localization,
                this::showGameModes,
                this::showSettings,
                this::showStats
        );
    }

    private void showMainMenu() {

        scene.setRoot(createMainMenu());
    }

    private void showGameModes() {

        GameModeScreen modes = new GameModeScreen(
                localization,
                this::showMainMenu,
                this::startSoloGame,
                this::startMultiplayerGame
        );

        scene.setRoot(modes);
    }


    private void startSoloGame() {

        activeGameScreen = new GameScreen(
                controlsSettings,
                statsService,
                () -> {
                    activeGameScreen = null;
                    showMainMenu();
                }
        );

        scene.setRoot(activeGameScreen);

        Platform.runLater(activeGameScreen::start);
    }

    public static void main(String[] args) {
        launch(args);
    }


    private void startMultiplayerGame(int playerCount) {

        MultiplayerScreen screen = new MultiplayerScreen(
                playerCount,
                controlsSettings,
                statsService,
                () -> {
                    activeMultiplayerScreen = null;
                    showMainMenu();
                }
        );

        activeMultiplayerScreen = screen;
        scene.setRoot(screen);

        Platform.runLater(screen::start);
    }

    private void showStats() {

        StatsScreen statsScreen = new StatsScreen(
                statsService,
                localization,
                this::showMainMenu
        );

        scene.setRoot(statsScreen);
    }


}

