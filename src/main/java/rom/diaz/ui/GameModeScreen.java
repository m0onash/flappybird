package rom.diaz.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import rom.diaz.config.Localization;

import java.util.function.IntConsumer;

public class GameModeScreen extends StackPane {

    private int selectedPlayers = 2;

    public GameModeScreen(
            Localization localization,
            Runnable onBack,
            Runnable onSolo,
            IntConsumer onMultiplayer
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
                localization.get("chooseMode")
        );
        subtitle.getStyleClass().add("menu-subtitle");

        Label title = new Label(
                localization.get("readyToFly")
        );
        title.getStyleClass().add("mode-title");

        Label description = new Label(
                localization.get("modeDescription")
        );
        description.getStyleClass().add("mode-description");

        VBox heading = new VBox(
                10,
                subtitle,
                title,
                description
        );
        heading.setAlignment(Pos.CENTER);

        // SOLO MODE
        Label soloIcon = new Label("1");
        soloIcon.getStyleClass().add("mode-icon");

        Label soloTitle = new Label(
                localization.get("solo")
        );
        soloTitle.getStyleClass().add("mode-card-title");

        Label soloDescription = new Label(
                localization.get("soloDescription")
        );
        soloDescription.getStyleClass().add("mode-description");

        Button soloButton = new Button(
                localization.get("start") + "  ▶"
        );
        soloButton.getStyleClass().add("mode-action");
        soloButton.setOnAction(event -> onSolo.run());

        VBox soloCard = new VBox(
                15,
                soloIcon,
                soloTitle,
                soloDescription,
                soloButton
        );

        soloCard.setAlignment(Pos.CENTER);
        soloCard.getStyleClass().addAll(
                "mode-card",
                "solo-card"
        );

        // MULTIPLAYER MODE
        Label multiIcon = new Label("4");
        multiIcon.getStyleClass().add("mode-icon");

        Label multiTitle = new Label(
                localization.get("multiplayer")
        );
        multiTitle.getStyleClass().add("mode-card-title");

        Label multiDescription = new Label(
                localization.get("multiDescription")
        );
        multiDescription.getStyleClass().add("mode-description");

        HBox playerSelector = new HBox(10);
        playerSelector.setAlignment(Pos.CENTER);

        for (int count = 2; count <= 4; count++) {

            final int players = count;

            Button playerButton = new Button(
                    String.valueOf(count)
            );

            playerButton.getStyleClass().add(
                    "player-count-button"
            );

            if (count == selectedPlayers) {
                playerButton.getStyleClass().add("selected");
            }

            playerButton.setOnAction(event -> {

                selectedPlayers = players;

                playerSelector.getChildren().forEach(node ->
                        node.getStyleClass().remove("selected")
                );

                playerButton.getStyleClass().add("selected");
            });

            playerSelector.getChildren().add(playerButton);
        }

        Button multiplayerButton = new Button(
                localization.get("createGame") + "  ▶"
        );

        multiplayerButton.getStyleClass().add("mode-action");

        multiplayerButton.setOnAction(event ->
                onMultiplayer.accept(selectedPlayers)
        );

        VBox multiplayerCard = new VBox(
                15,
                multiIcon,
                multiTitle,
                multiDescription,
                playerSelector,
                multiplayerButton
        );

        multiplayerCard.setAlignment(Pos.CENTER);
        multiplayerCard.getStyleClass().addAll(
                "mode-card",
                "multiplayer-card"
        );

        // Cards layout
        HBox cards = new HBox(
                20,
                soloCard,
                multiplayerCard
        );

        cards.setAlignment(Pos.CENTER);

        // Tip
        Label tip = new Label(
                localization.get("flightTip")
        );
        tip.getStyleClass().add("mode-description");

        // Main content
        VBox content = new VBox(
                30,
                backButton,
                heading,
                cards,
                tip
        );

        content.setAlignment(Pos.CENTER);

        StackPane panel = new StackPane(content);
        panel.getStyleClass().add("menu-panel");
        panel.setMaxWidth(900);
        panel.setMaxHeight(650);

        getChildren().add(panel);
    }
}
