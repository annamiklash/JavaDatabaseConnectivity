package mappers.impl;

import mappers.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserIdMapper implements Mapper<Integer> {
    @Override
    public Integer map(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return resultSet.getInt(1);
        } else {
            return null;
        }
    }
}
