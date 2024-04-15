package springbook.user.dao;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.domain.User;

public class UserDaoTest2 {

    public static void main(String[] args) {
        DaoFactory factory = new DaoFactory();
        UserDao userDao = factory.userDao();
        UserDao userDao2 = factory.userDao();

        System.out.println("userDao = " + userDao);
        System.out.println("userDao2 = " + userDao2);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao userDao3 = context.getBean("userDao", UserDao.class);
        UserDao userDao4 = context.getBean("userDao", UserDao.class);

        System.out.println("userDao3 = " + userDao3);
        System.out.println("userDao4 = " + userDao4);
    }
}
