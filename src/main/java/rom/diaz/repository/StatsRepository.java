package rom.diaz.repository;

import rom.diaz.model.PlayerStats;

public interface StatsRepository {

    void saveStats(PlayerStats stats);

    PlayerStats loadStats(int playerId);
}
