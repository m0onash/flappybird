
package rom.diaz.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import rom.diaz.config.Localization;

import java.util.Objects;

public class MainMenuScreen extends StackPane {

    public MainMenuScreen(
            Localization localization,
            Runnable onPlay,
            Runnable onSettings,
            Runnable onStats
    ) {
        getStyleClass().add("menu-root");

        // Left side: title and Don Quixote.
        Label subtitle = new Label("— ¡A VOLAR!");
        subtitle.getStyleClass().add("menu-subtitle");

        Label title = new Label("FLAPPY");
        title.getStyleClass().add("menu-title");

        Label sky = new Label("SKY");
        sky.getStyleClass().add("menu-title-sky");

        Image donImage = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(
                                "/images/flappy-sky-bird.png"
                        )
                )
        );

        ImageView don = new ImageView(donImage);
        don.setFitWidth(205);
        don.setPreserveRatio(true);
        don.setSmooth(true);

        VBox hero = new VBox(
                0,
                subtitle,
                title,
                sky,
                don
        );

        hero.setAlignment(Pos.CENTER);
        hero.getStyleClass().add("menu-hero");

        // Right side: menu buttons.
        VBox buttons = new VBox(
                16,
                createMenuButton(
                        "▶",
                        localization.get("play"),
                        "Elige tu modo de vuelo",
                        onPlay,
                        true
                ),
                createMenuButton(
                        "⚙",
                        localization.get("settings"),
                        "Controles, idioma y pantalla",
                        onSettings,
                        false
                ),
                createMenuButton(
                        "▥",
                        localization.get("stats"),
                        "Tus mejores vuelos",
                        onStats,
                        false
                )
        );

        buttons.setAlignment(Pos.CENTER);
        buttons.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(buttons, Priority.ALWAYS);

        HBox content = new HBox(45, hero, buttons);
        content.setAlignment(Pos.CENTER);

        StackPane panel = new StackPane(content);
        panel.getStyleClass().add("menu-panel");
        panel.setMaxWidth(960);
        panel.setMaxHeight(560);
        panel.setPadding(new Insets(35));

        getChildren().add(panel);
    }

    private Button createMenuButton(
            String icon,
            String title,
            String description,
            Runnable action,
            boolean primary
    ) {
        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("menu-button-icon");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("menu-button-title");

        Label descriptionLabel = new Label(description);
        descriptionLabel.getStyleClass().add(
                "menu-button-description"
        );

        VBox text = new VBox(
                4,
                titleLabel,
                descriptionLabel
        );

        text.setAlignment(Pos.CENTER_LEFT);

        Label arrow = new Label("›");
        arrow.getStyleClass().add("menu-button-arrow");

        HBox content = new HBox(
                16,
                iconLabel,
                text,
                arrow
        );

        content.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(text, Priority.ALWAYS);

        Button button = new Button();
        button.setGraphic(content);
        button.getStyleClass().add("menu-button");

        if (primary) {
            button.getStyleClass().add("play-button");
        }

        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(event -> action.run());

        return button;
    }
}
