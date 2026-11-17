package rom.diaz;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import rom.diaz.config.ControlsSettings;
import rom.diaz.config.GameSettings;
import rom.diaz.config.Localization;
import rom.diaz.ui.GameModeScreen;
import rom.diaz.ui.GameScreen;
import rom.diaz.ui.MainMenuScreen;
import rom.diaz.ui.SettingsScreen;

public class Main extends Application {

    private Stage stage;
    private Scene scene;
    private GameScreen activeGameScreen;
    private final GameSettings settings = new GameSettings();
    private final Localization localization = new Localization(settings);
    private final ControlsSettings controlsSettings = new ControlsSettings();

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        MainMenuScreen menu = createMainMenu();

        scene = new Scene(menu, 1000, 700);

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
            if (activeGameScreen != null) {
                activeGameScreen.stop();
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
                () -> System.out.println("Stats clicked")
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
                players -> System.out.println(
                        "Multiplayer selected: " + players
                )
        );

        scene.setRoot(modes);
    }

    private void startSoloGame() {

        activeGameScreen = new GameScreen(
                controlsSettings,
                () -> {
                    activeGameScreen = null;
                    showMainMenu();
                }
        );

        scene.setRoot(activeGameScreen);

        // Wait until JavaFX has attached the screen to the scene.
        Platform.runLater(activeGameScreen::start);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
