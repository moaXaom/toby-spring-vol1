package springbook.user.dao;

public class DaoFactory {

    public UserDao userDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        return new UserDao(connectionMaker);
    }

    public UserDao accountDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        return new UserDao(connectionMaker);
    }

    public UserDao messageDao() {
        ConnectionMaker connectionMaker = connectionMaker();
        return new UserDao(connectionMaker);
    }

    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
