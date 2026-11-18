package service;

import org.junit.jupiter.api.Test;
import rom.diaz.model.PlayerStats;
import rom.diaz.repository.StatsRepository;
import rom.diaz.service.StatsService;

import static org.junit.jupiter.api.Assertions.*;

class StatsServiceTest {

    @Test
    void shouldAccumulateStatistics() {

        StatsRepository repository = new StatsRepository() {

            private PlayerStats stored = new PlayerStats(1);

            @Override
            public void saveStats(PlayerStats stats) {
                stored = stats;
            }

            @Override
            public PlayerStats loadStats(int playerId) {
                return stored;
            }
        };

        StatsService service = new StatsService(repository);

        service.recordGameOver(1, 10);
        service.recordGameOver(1, 5);

        PlayerStats stats = service.getPlayerStats(1);

        assertEquals(10, stats.getMaxScore());
        assertEquals(15, stats.getObstaclesAvoided());
        assertEquals(2, stats.getTimesLost());
    }

    @Test
    void shouldRejectNegativeScores() {

        StatsRepository repository = new StatsRepository() {
            @Override
            public void saveStats(PlayerStats stats) {}

            @Override
            public PlayerStats loadStats(int playerId) {
                return new PlayerStats(playerId);
            }
        };

        StatsService service = new StatsService(repository);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.recordGameOver(1, -1)
        );
    }
}
