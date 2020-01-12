package mappers.impl;

import mappers.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserExistsMapper implements Mapper<Boolean> {

    @Override
    public Boolean map(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return resultSet.getBoolean(1);
        } else {
            return false;
        }
    }
}
