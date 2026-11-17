package rom.diaz.ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;

import rom.diaz.config.ControlsSettings;
import rom.diaz.config.GameSettings;
import rom.diaz.config.Localization;

public class SettingsScreen extends StackPane {

    public SettingsScreen(
            GameSettings settings,
            Localization localization,
            ControlsSettings controlsSettings,
            Runnable onBack,
            Runnable onResolutionChanged,
            Runnable onLanguageChanged
    ) {

        getStyleClass().add("menu-root");

        // Back button
        Button backButton = new Button(
                "‹ " + localization.get("back")
        );

        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> onBack.run());

        // Heading
        Label subtitle = new Label(
                settings.getLanguage().equals("es")
                        ? "A TU MANERA"
                        : "YOUR WAY"
        );
        subtitle.getStyleClass().add("menu-subtitle");

        Label title = new Label(
                localization.get("settings")
        );
        title.getStyleClass().add("mode-title");

        VBox heading = new VBox(8, subtitle, title);
        heading.setAlignment(Pos.CENTER);

        // Sound effects
        Button soundButton = new Button();
        soundButton.getStyleClass().add("setting-button");

        updateToggleText(
                soundButton,
                localization.get("sound"),
                settings.isSoundEnabled()
        );

        soundButton.setOnAction(event -> {
            settings.toggleSound();

            updateToggleText(
                    soundButton,
                    localization.get("sound"),
                    settings.isSoundEnabled()
            );
        });

        // Music
        Button musicButton = new Button();
        musicButton.getStyleClass().add("setting-button");

        updateToggleText(
                musicButton,
                localization.get("music"),
                settings.isMusicEnabled()
        );

        musicButton.setOnAction(event -> {
            settings.toggleMusic();

            updateToggleText(
                    musicButton,
                    localization.get("music"),
                    settings.isMusicEnabled()
            );
        });

        // Language
        ComboBox<String> languageBox = new ComboBox<>();

        languageBox.getItems().addAll(
                "Español",
                "English"
        );

        languageBox.setValue(
                settings.getLanguage().equals("es")
                        ? "Español"
                        : "English"
        );

        languageBox.setOnAction(event -> {

            String language =
                    languageBox.getValue().equals("Español")
                            ? "es"
                            : "en";

            settings.changeLanguage(language);

            // Refresh the screen with the new language.
            onLanguageChanged.run();
        });

        // Resolution
        ComboBox<String> resolutionBox = new ComboBox<>();

        resolutionBox.getItems().addAll(
                "800 x 600",
                "1024 x 768",
                "1280 x 720",
                "1600 x 900"
        );

        String currentResolution =
                settings.getWindowWidth()
                        + " x "
                        + settings.getWindowHeight();

        if (!resolutionBox.getItems().contains(currentResolution)) {
            resolutionBox.getItems().add(currentResolution);
        }

        resolutionBox.setValue(currentResolution);

        resolutionBox.setOnAction(event -> {

            String[] dimensions =
                    resolutionBox.getValue().split(" x ");

            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);

            settings.changeResolution(width, height);

            // Apply the new window dimensions.
            onResolutionChanged.run();
        });

        VBox keybindingControls = new VBox(10);

        for (int playerId = 1; playerId <= 4; playerId++) {
            keybindingControls.getChildren().add(
                    createKeybindingButton(
                            controlsSettings,
                            playerId
                    )
            );
        }

        keybindingControls.setAlignment(Pos.CENTER);


        // Setting rows
        HBox languageRow = createSettingRow(
                localization.get("language"),
                languageBox
        );

        HBox resolutionRow = createSettingRow(
                localization.get("resolution"),
                resolutionBox
        );

        VBox controls = new VBox(
                14,
                soundButton,
                musicButton,
                createSettingRow(
                        localization.get("language"),
                        languageBox
                ),
                createSettingRow(
                        localization.get("resolution"),
                        resolutionBox
                ),
                keybindingControls
        );


        controls.setAlignment(Pos.CENTER);
        controls.setMaxWidth(600);

        // Main panel
        VBox content = new VBox(
                24,
                backButton,
                heading,
                controls
        );

        content.setAlignment(Pos.CENTER);

        ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background: transparent;"
        );

        StackPane panel = new StackPane(scrollPane);
        panel.getStyleClass().add("menu-panel");
        panel.setMaxWidth(850);
        panel.setMaxHeight(650);

        getChildren().add(panel);
    }

    private HBox createSettingRow(
            String name,
            ComboBox<String> selector
    ) {

        Label label = new Label(name);
        label.getStyleClass().add("setting-label");

        HBox row = new HBox(20, label, selector);
        row.setAlignment(Pos.CENTER);

        return row;
    }

    private void updateToggleText(
            Button button,
            String name,
            boolean enabled
    ) {

        button.setText(
                name + ": " + (enabled ? "ON" : "OFF")
        );
    }

    private Button createKeybindingButton(
            ControlsSettings controlsSettings,
            int playerId
    ) {
        Button button = new Button();
        button.getStyleClass().add("setting-button");

        updateKeybindingText(
                button,
                controlsSettings,
                playerId
        );

        button.setOnAction(event -> {

            button.setText("Press any key...");
            button.requestFocus();

            button.setOnKeyPressed(keyEvent -> {

                KeyCode selectedKey = keyEvent.getCode();

                // Escape cancels key capture.
                if (selectedKey == KeyCode.ESCAPE) {
                    updateKeybindingText(
                            button,
                            controlsSettings,
                            playerId
                    );

                    button.setOnKeyPressed(null);
                    keyEvent.consume();
                    return;
                }

                boolean success = controlsSettings.setJumpKey(
                        playerId,
                        selectedKey
                );

                if (!success) {
                    Alert alert = new Alert(
                            Alert.AlertType.WARNING,
                            "This key is reserved or already assigned."
                    );

                    alert.showAndWait();
                }

                updateKeybindingText(
                        button,
                        controlsSettings,
                        playerId
                );

                button.setOnKeyPressed(null);
                keyEvent.consume();
            });
        });

        return button;
    }

    private void updateKeybindingText(
            Button button,
            ControlsSettings controlsSettings,
            int playerId
    ) {
        button.setText(
                "Player " + playerId + " — Jump: "
                        + controlsSettings
                        .getJumpKey(playerId)
                        .getName()
        );
    }

}
