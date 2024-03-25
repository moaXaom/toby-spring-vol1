package springbook.user.dao;

import springbook.user.domain.User;

public class UserDaoTest {

    public static void main(String[] args) throws Exception {
        UserDao userDao = new DaoFactory().userDao();

        User user = new User();
        user.setId("verus");
        user.setName("정진혁");
        user.setPassword("password");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }
}
