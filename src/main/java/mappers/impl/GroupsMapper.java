package mappers.impl;

import dto.Group;
import mappers.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupsMapper implements Mapper<List<Group>> {

    @Override
    public List<Group> map(ResultSet resultSet) throws SQLException {
        List<Group> groupList = new ArrayList<>();
        while (resultSet.next()) {
            Group group = new Group(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("description")
            );

            groupList.add(group);
        }

        return groupList;
    }

}
