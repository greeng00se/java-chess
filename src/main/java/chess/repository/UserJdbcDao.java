package chess.repository;

import chess.db.JdbcTemplate;
import chess.domain.user.User;
import chess.dto.NameDto;

public class UserJdbcDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserJdbcDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(final NameDto nameDto) {
        jdbcTemplate.executeUpdate("INSERT INTO user (name) VALUES (?)", nameDto.getValue());
    }

    @Override
    public User findByName(final NameDto nameDto) {
        return jdbcTemplate.query("SELECT * FROM user WHERE name = ?", resultSet -> {
            if (resultSet.next()) {
                final int id = resultSet.getInt("id");
                final String name = resultSet.getString("name");
                return new User(id, name);
            }
            return null;
        }, nameDto.getValue());
    }

    public void deleteAll() {
        jdbcTemplate.executeUpdate("DELETE FROM user");
    }
}
