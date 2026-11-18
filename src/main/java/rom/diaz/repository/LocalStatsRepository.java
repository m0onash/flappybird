package rom.diaz.repository;

import rom.diaz.model.PlayerStats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LocalStatsRepository implements StatsRepository {

    private final String databaseUrl;

    public LocalStatsRepository(String databaseUrl) {
        this.databaseUrl = databaseUrl;
        initializeDatabase();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(databaseUrl);
    }

    private void initializeDatabase() {
        String sql = """
                CREATE TABLE IF NOT EXISTS player_stats (
                    player_id INT PRIMARY KEY,
                    max_score INT NOT NULL,
                    obstacles_avoided INT NOT NULL,
                    times_lost INT NOT NULL
                )
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {

            statement.execute(sql);

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Failed to initialize statistics database",
                    exception
            );
        }
    }

    @Override
    public void saveStats(PlayerStats stats) {
        String sql = """
                MERGE INTO player_stats
                (player_id, max_score, obstacles_avoided, times_lost)
                KEY (player_id)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, stats.getPlayerId());
            statement.setInt(2, stats.getMaxScore());
            statement.setInt(3, stats.getObstaclesAvoided());
            statement.setInt(4, stats.getTimesLost());

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Failed to save player statistics",
                    exception
            );
        }
    }

    @Override
    public PlayerStats loadStats(int playerId) {
        // Validate even when the database has no matching row.
        PlayerStats emptyStats = new PlayerStats(playerId);

        String sql = """
                SELECT max_score, obstacles_avoided, times_lost
                FROM player_stats
                WHERE player_id = ?
                """;

        try (Connection connection = connect();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, playerId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return new PlayerStats(
                            playerId,
                            result.getInt("max_score"),
                            result.getInt("obstacles_avoided"),
                            result.getInt("times_lost")
                    );
                }
            }

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Failed to load player statistics",
                    exception
            );
        }

        return emptyStats;
    }
}
