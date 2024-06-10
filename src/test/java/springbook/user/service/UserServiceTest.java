package springbook.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith({org.springframework.test.context.junit.jupiter.SpringExtension.class})
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    private static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    private static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    List<User> users;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();

        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    void upgradeLevels() throws Exception {
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    void add() {
        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());
        assertEquals(userWithLevel.getLevel(), userWithLevelRead.getLevel());
        assertEquals(Level.BASIC, userWithoutLevelRead.getLevel());
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setDataSource(this.dataSource);

        for (User user : users) {
            userDao.add(user);
        }

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (Exception e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertEquals(user.getLevel().nextLevel(), userUpdate.getLevel());
        } else {
            assertEquals(user.getLevel(), userUpdate.getLevel());
        }
    }
}
