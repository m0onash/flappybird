package repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import rom.diaz.model.PlayerStats;
import rom.diaz.repository.LocalStatsRepository;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LocalStatsRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldSaveAndLoadPlayerStats() {

        String url = databaseUrl();

        LocalStatsRepository repository =
                new LocalStatsRepository(url);

        PlayerStats stats = new PlayerStats(1);
        stats.updateMaxScore(25);
        stats.addObstaclesAvoided(100);
        stats.incrementTimesLost();

        repository.saveStats(stats);

        PlayerStats loaded = repository.loadStats(1);

        assertEquals(25, loaded.getMaxScore());
        assertEquals(100, loaded.getObstaclesAvoided());
        assertEquals(1, loaded.getTimesLost());
    }

    @Test
    void shouldPersistAfterReopeningRepository() {

        String url = databaseUrl();

        LocalStatsRepository first =
                new LocalStatsRepository(url);

        PlayerStats stats = new PlayerStats(2);
        stats.updateMaxScore(42);

        first.saveStats(stats);

        LocalStatsRepository second =
                new LocalStatsRepository(url);

        assertEquals(
                42,
                second.loadStats(2).getMaxScore()
        );
    }

    @Test
    void shouldReturnZeroStatsForNewPlayer() {

        LocalStatsRepository repository =
                new LocalStatsRepository(databaseUrl());

        PlayerStats stats = repository.loadStats(3);

        assertEquals(0, stats.getMaxScore());
        assertEquals(0, stats.getObstaclesAvoided());
        assertEquals(0, stats.getTimesLost());
    }

    @Test
    void shouldKeepPlayersSeparate() {

        LocalStatsRepository repository =
                new LocalStatsRepository(databaseUrl());

        PlayerStats playerOne = new PlayerStats(1);
        playerOne.updateMaxScore(50);

        PlayerStats playerTwo = new PlayerStats(2);
        playerTwo.updateMaxScore(12);

        repository.saveStats(playerOne);
        repository.saveStats(playerTwo);

        assertEquals(
                50,
                repository.loadStats(1).getMaxScore()
        );

        assertEquals(
                12,
                repository.loadStats(2).getMaxScore()
        );
    }

    private String databaseUrl() {
        return "jdbc:h2:file:"
                + tempDir.resolve("flappy_stats")
                .toAbsolutePath();
    }
}
