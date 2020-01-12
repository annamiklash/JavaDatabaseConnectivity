package singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static constants.ConnectionConstants.*;

public class ConnectionSingleton {

    private static Connection instance;

    public static Connection getInstance() {
        if (instance == null) {
            try {
                Class.forName(DRIVER);
                instance = DriverManager.getConnection(URL, LOGIN, PASSWORD);
                instance.setAutoCommit(false);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
