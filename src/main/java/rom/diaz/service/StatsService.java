package rom.diaz.service;

import rom.diaz.model.PlayerStats;
import rom.diaz.repository.StatsRepository;

import java.util.Objects;

public class StatsService {

    private final StatsRepository repository;

    public StatsService(StatsRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public void recordGameOver(int playerId, int finalScore) {
        if (finalScore < 0) {
            throw new IllegalArgumentException(
                    "Final score cannot be negative"
            );
        }

        PlayerStats stats = repository.loadStats(playerId);

        stats.updateMaxScore(finalScore);
        stats.addObstaclesAvoided(finalScore);
        stats.incrementTimesLost();

        repository.saveStats(stats);
    }

    public PlayerStats getPlayerStats(int playerId) {
        return repository.loadStats(playerId);
    }
}
