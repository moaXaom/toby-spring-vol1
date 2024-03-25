package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;

class DConnectionMaker implements ConnectionMaker {

    @Override
    public Connection makeNewConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost/moamoa", "moamoa", "moamoa");
    }
}
