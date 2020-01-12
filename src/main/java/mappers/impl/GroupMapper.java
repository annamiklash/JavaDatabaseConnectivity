package mappers.impl;

import dto.Group;
import mappers.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements Mapper<Group> {

    @Override
    public Group map(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            return new Group(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description")
            );
        } else {
            return null;
        }
    }
}
