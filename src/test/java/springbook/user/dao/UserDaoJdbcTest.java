package springbook.user.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;

import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.exception.DuplicateUserIdException;

@ExtendWith({org.springframework.test.context.junit.jupiter.SpringExtension.class})
@ContextConfiguration(locations = "/applicationContext.xml")
class UserDaoJdbcTest {

    @Autowired
    private UserDao userDao;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        user1 = new User("1", "111", "1111", Level.BASIC, 1, 0, "test1@test.com");
        user2 = new User("2", "222", "2222", Level.SILVER, 55, 10, "test2@test.com");
        user3 = new User("3", "333", "3333", Level.GOLD, 100, 40, "test3@test.com");
    }

    @Test
    @DisplayName("Add two numbers")
    public void addAndGet() throws Exception {
        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        userDao.add(user1);
        userDao.add(user2);
        assertEquals(userDao.getCount(), 2);

        User userGet1 = userDao.get(user1.getId());
        checkSameUser(userGet1, user1);

        User userGet2 = userDao.get(user2.getId());
        checkSameUser(userGet2, user2);
    }

    @Test
    @DisplayName("getCount")
    public void count() {
        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        userDao.add(user1);
        assertEquals(userDao.getCount(), 1);

        userDao.add(user2);
        assertEquals(userDao.getCount(), 2);

        userDao.add(user3);
        assertEquals(userDao.getCount(), 3);

    }

    @Test
    @DisplayName("throw")
    public void getUserFailure() {
        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        assertThrows(EmptyResultDataAccessException.class, () -> userDao.get("unknown"));
    }

    @Test
    void getAll() {
        // given
        userDao.deleteAll();
        assertEquals(userDao.getCount(), 0);

        userDao.add(user1);
        List<User> users = userDao.getAll();
        Assertions.assertEquals(1, users.size());
        checkSameUser(user1, users.get(0));

        userDao.add(user2);
        users = userDao.getAll();
        Assertions.assertEquals(2, users.size());
        checkSameUser(user1, users.get(0));
        checkSameUser(user2, users.get(1));

        userDao.add(user3);
        users = userDao.getAll();
        Assertions.assertEquals(3, users.size());
        checkSameUser(user1, users.get(0));
        checkSameUser(user2, users.get(1));
        checkSameUser(user3, users.get(2));

        userDao.deleteAll();
        users = userDao.getAll();
        assertEquals(0, users.size());
    }

    @Test
    void duplicateKey() {
        userDao.deleteAll();

        userDao.add(user1);
        assertThrows(DuplicateUserIdException.class, () -> userDao.add(user1));
    }

    @Test
    public void update() {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);

        user1.setName("디우");
        user1.setPassword("그린론");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        userDao.update(user1);

        User user1update = userDao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2same = userDao.get(user2.getId());
        checkSameUser(user2, user2same);
    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getLevel(), user2.getLevel());
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getRecommend(), user2.getRecommend());
        assertEquals(user1.getEmail(), user2.getEmail());
    }
}
