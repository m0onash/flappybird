
package rom.diaz.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import rom.diaz.config.Localization;
import rom.diaz.model.PlayerStats;
import rom.diaz.service.StatsService;

public class StatsScreen extends VBox {

    public StatsScreen(
            StatsService statsService,
            Localization localization,
            Runnable onBack
    ) {
        getStyleClass().add("menu-root");

        setAlignment(Pos.CENTER);
        setSpacing(24);
        setPadding(new Insets(30));

        Label title = new Label(
                localization.get("playerStats")
        );
        title.getStyleClass().add("stats-title");

        VBox playerList = new VBox(12);
        playerList.setMaxWidth(600);
        playerList.setFillWidth(true);

        for (int playerId = 1; playerId <= 4; playerId++) {
            PlayerStats stats =
                    statsService.getPlayerStats(playerId);

            VBox card = createPlayerCard(stats, localization);
            playerList.getChildren().add(card);
        }

        ScrollPane scrollPane = new ScrollPane(playerList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setMaxWidth(650);

        scrollPane.getStyleClass().add("stats-scroll");

        VBox.setVgrow(
                scrollPane,
                javafx.scene.layout.Priority.ALWAYS
        );

        Button backButton = new Button(
                localization.get("back")
        );

        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(event -> onBack.run());

        getChildren().addAll(
                title,
                scrollPane,
                backButton
        );
    }

    private VBox createPlayerCard(
            PlayerStats stats,
            Localization localization
    ) {
        Label playerName = new Label(
                localization.get("player") + " " + stats.getPlayerId()
        );
        playerName.getStyleClass().add("stats-player-name");

        Label maxScore = new Label(
                localization.get("bestScore") + ": "
                        + stats.getMaxScore()
        );

        Label obstacles = new Label(
                localization.get("obstaclesAvoided") + ": "
                        + stats.getObstaclesAvoided()
        );

        Label losses = new Label(
                localization.get("timesLost") + ": "
                        + stats.getTimesLost()
        );

        for (Label label : new Label[]{
                maxScore, obstacles, losses
        }) {
            label.getStyleClass().add("stats-value");
        }

        VBox card = new VBox(
                8,
                playerName,
                maxScore,
                obstacles,
                losses
        );

        card.setPadding(new Insets(18, 24, 18, 24));
        card.getStyleClass().add("stats-card");

        return card;
    }
}
