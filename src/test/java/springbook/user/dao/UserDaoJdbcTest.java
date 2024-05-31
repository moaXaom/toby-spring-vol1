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
import org.springframework.test.context.ContextConfiguration;
import springbook.user.domain.User;
import springbook.user.exception.DuplicateUserIdException;

@ExtendWith({org.springframework.test.context.junit.jupiter.SpringExtension.class})
@ContextConfiguration(locations = "/applicationContext.xml")
class UserDaoJdbcTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        this.user1 = new User("1", "111", "1111");
        this.user2 = new User("2", "222", "2222");
        this.user3 = new User("3", "333", "3333");
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
        assertEquals(userGet1.getName(), user1.getName());
        assertEquals(userGet1.getPassword(), user1.getPassword());

        User userGet2 = userDao.get(user2.getId());
        assertEquals(userGet2.getName(), user2.getName());
        assertEquals(userGet2.getPassword(), user2.getPassword());
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
    void sqlExceptioNTranslate() {
        // given
        userDao.deleteAll();

        // when & then
        try {
            userDao.add(user1);
            userDao.add(user1);
        } catch (DuplicateUserIdException ex) {
            final SQLException sqlEx = (SQLException) ex.getCause().getCause();
            final SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

            assertEquals(DuplicateKeyException.class, translator.translate(null, null, sqlEx).getClass());
        }
    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
    }
}