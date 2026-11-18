package rom.diaz.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import rom.diaz.model.PlayerStats;
import rom.diaz.service.StatsService;

public class StatsScreen extends VBox {

    public StatsScreen(
            StatsService statsService,
            Runnable onBack
    ) {
        setAlignment(Pos.CENTER);
        setSpacing(24);
        setPadding(new Insets(30));

        setStyle("-fx-background-color: #79d8f5;");

        Label title = new Label("PLAYER STATISTICS");

        title.setStyle(
                "-fx-font-size: 32px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #193451;"
        );

        VBox playerList = new VBox(12);
        playerList.setMaxWidth(550);

        for (int playerId = 1; playerId <= 4; playerId++) {
            PlayerStats stats =
                    statsService.getPlayerStats(playerId);

            VBox card = createPlayerCard(stats);
            playerList.getChildren().add(card);
        }

        Button backButton = new Button("BACK");

        backButton.setStyle(
                "-fx-background-color: #ff9f35;" +
                        "-fx-text-fill: #193451;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-padding: 12 32;" +
                        "-fx-background-radius: 12;"
        );

        backButton.setOnAction(event -> onBack.run());

        getChildren().addAll(
                title,
                playerList,
                backButton
        );
    }

    private VBox createPlayerCard(PlayerStats stats) {

        Label playerName = new Label(
                "PLAYER " + stats.getPlayerId()
        );

        playerName.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #193451;"
        );

        Label maxScore = new Label(
                "Best Score: " + stats.getMaxScore()
        );

        Label obstacles = new Label(
                "Obstacles Avoided: "
                        + stats.getObstaclesAvoided()
        );

        Label losses = new Label(
                "Times Lost: " + stats.getTimesLost()
        );

        for (Label label : new Label[]{
                maxScore, obstacles, losses
        }) {
            label.setStyle(
                    "-fx-font-size: 15px;" +
                            "-fx-text-fill: #193451;"
            );
        }

        VBox card = new VBox(
                6,
                playerName,
                maxScore,
                obstacles,
                losses
        );

        card.setPadding(new Insets(15, 24, 15, 24));

        card.setStyle(
                "-fx-background-color: #fffdf5;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #193451;" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-width: 2;"
        );

        return card;
    }
}
