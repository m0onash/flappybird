package rom.diaz.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import rom.diaz.config.Localization;

public class MainMenuScreen extends StackPane {

    public MainMenuScreen(
            Localization localization,
            Runnable onPlay,
            Runnable onSettings,
            Runnable onStats
    ) {

        getStyleClass().add("menu-root");

        // Left side: title and mascot
        Label subtitle = new Label("— ¡A VOLAR!");
        subtitle.getStyleClass().add("menu-subtitle");

        Label title = new Label("FLAPPY");
        title.getStyleClass().add("menu-title");

        Label sky = new Label("SKY");
        sky.getStyleClass().add("menu-title-sky");

        // Temporary mascot until we import the SVG.
        Label bird = new Label("🐦");
        bird.setStyle("-fx-font-size: 90px;");

        VBox hero = new VBox(
                0,
                subtitle,
                title,
                sky,
                bird
        );

        hero.setAlignment(Pos.CENTER);

        // Right side: menu buttons
        Button playButton = createButton(
                "▶  " + localization.get("play"),
                onPlay
        );

        playButton.getStyleClass().add("play-button");


        Button settingsButton = createButton(
                "⚙  " + localization.get("settings"),
                onSettings
        );

        Button statsButton = createButton(
                "▥  " + localization.get("stats"),
                onStats
        );


        VBox buttons = new VBox(
                18,
                playButton,
                settingsButton,
                statsButton
        );

        buttons.setAlignment(Pos.CENTER);

        HBox content = new HBox(
                40,
                hero,
                buttons
        );

        content.setAlignment(Pos.CENTER);

        StackPane panel = new StackPane(content);
        panel.getStyleClass().add("menu-panel");
        panel.setMaxWidth(850);
        panel.setMaxHeight(450);

        getChildren().add(panel);
    }

    private Button createButton(
            String text,
            Runnable action
    ) {

        Button button = new Button(text);

        button.getStyleClass().add("menu-button");

        button.setOnAction(event -> action.run());

        return button;
    }
}
