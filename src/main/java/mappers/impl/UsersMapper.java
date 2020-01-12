package mappers.impl;

import dto.User;
import mappers.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsersMapper implements Mapper<List<User>> {

    @Override
    public List<User> map(ResultSet resultSet) throws SQLException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("login"),
                    resultSet.getString("password")
            );

            userList.add(user);
        }

        return userList;
    }
}
