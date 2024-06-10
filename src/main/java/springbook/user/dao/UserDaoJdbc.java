package springbook.user.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.exception.DuplicateUserIdException;

public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        final User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("level")));
        user.setLogin(rs.getInt("login"));
        user.setRecommend(rs.getInt("recommend"));
        return user;
    };

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        try {
            this.jdbcTemplate.update("insert into users(id, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?)",
                    user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend()
            );
        } catch (DuplicateKeyException e) {
            throw new DuplicateUserIdException(e);
        }
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject("select * from users where id = ?", userRowMapper, id);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("select * from users order by id", userRowMapper);
    }

    @Override
    public void update(User user) {
        this.jdbcTemplate.update("update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ?",
                user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }
}
