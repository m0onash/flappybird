
package rom.diaz.model;

import lombok.Getter;

@Getter
public class PlayerStats {

    private final int playerId;
    private int maxScore;
    private int obstaclesAvoided;
    private int timesLost;

    public PlayerStats(int playerId) {
        if (playerId < 1 || playerId > 4) {
            throw new IllegalArgumentException(
                    "Player ID must be between 1 and 4"
            );
        }

        this.playerId = playerId;
    }

    public PlayerStats(
            int playerId,
            int maxScore,
            int obstaclesAvoided,
            int timesLost
    ) {
        this(playerId);

        if (maxScore < 0
                || obstaclesAvoided < 0
                || timesLost < 0) {
            throw new IllegalArgumentException(
                    "Statistics cannot be negative"
            );
        }

        this.maxScore = maxScore;
        this.obstaclesAvoided = obstaclesAvoided;
        this.timesLost = timesLost;
    }

    public void updateMaxScore(int score) {
        if (score < 0) {
            throw new IllegalArgumentException(
                    "Score cannot be negative"
            );
        }

        maxScore = Math.max(maxScore, score);
    }

    public void addObstaclesAvoided(int count) {
        if (count < 0) {
            throw new IllegalArgumentException(
                    "Obstacle count cannot be negative"
            );
        }

        obstaclesAvoided = Math.addExact(
                obstaclesAvoided,
                count
        );
    }

    public void incrementTimesLost() {
        timesLost = Math.incrementExact(timesLost);
    }
}
