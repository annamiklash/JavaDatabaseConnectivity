package repositories.impl;

import dto.Group;
import mappers.impl.GroupIdMapper;
import mappers.impl.GroupMapper;
import mappers.impl.GroupsMapper;
import repositories.GroupRepository;
import singleton.ConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupRepositoryImpl implements GroupRepository {

    private Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void beginTransaction() {
        connection = ConnectionSingleton.getInstance();
    }


    @Override
    public void commitTransaction() {
        try {
            final Connection connection = getConnection();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            final Connection connection = getConnection();
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Group> findByName(String name) {
        try {
            final String sql = "SELECT id, name, description FROM groups WHERE name LIKE ? OR name = ? OR name LIKE ? OR name like ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setString(1, name + '%');
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, '%' + name + '%');
            preparedStatement.setString(4, '%' + name);

            final ResultSet resultSet = preparedStatement.executeQuery();

            return new GroupsMapper().map(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public Group findById(int id) {
        try {
            final String sql = "SELECT id, name, description FROM groups WHERE id = ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, id);

            final ResultSet resultSet = preparedStatement.executeQuery();

            return new GroupMapper().map(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Group> findByUserId(int userId) {
        try {
            final String sql = "select g.id, g.name, g.description FROM users_groups ug JOIN groups g ON ug.group_id = g.id where ug.user_id = ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            final ResultSet resultSet = preparedStatement.executeQuery();

            return new GroupsMapper().map(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public int getCount() {
        try {
            final String sql = "SELECT COUNT(1) FROM groups";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            final ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public boolean exists(Group dto) {
        try {
            final String sql = "SELECT exists (SELECT 1 FROM groups WHERE id = ? LIMIT 1)";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setObject(1, dto.getId(), Types.INTEGER);

            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void add(Group group) {
        final String groupName = group.getGroupName();
        final String groupDescription = group.getGroupDescription();

        try {
            final String sql = "INSERT INTO groups (name, description) VALUES (?, ?) RETURNING ID";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setString(1, groupName);
            preparedStatement.setString(2, groupDescription);

            final ResultSet resultSet = preparedStatement.executeQuery();
            final Integer newId = new GroupIdMapper().map(resultSet);

            group.setId(newId);

            group.getUserList().stream()
                    .filter(user -> user.getId() != null)
                    .forEach(user -> insertIntoUsersGroups(user.getId(), group.getId()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Group group) {
        try {
            final String sql = "UPDATE groups SET name = ?, description = ? WHERE id = ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setString(1, group.getGroupName());
            preparedStatement.setString(2, group.getGroupDescription());
            preparedStatement.setInt(3, group.getId());

            preparedStatement.executeUpdate();

            deleteFromUsersGroups(group);

            group.getUserList().stream()
                    .filter(user -> user.getId() != null)
                    .forEach(user -> insertIntoUsersGroups(user.getId(), group.getId()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addOrUpdate(Group dto) {
        if (exists(dto)) {
            update(dto);
        } else {
            add(dto);
        }
    }

    @Override
    public void delete(Group group) {
        deleteFromUsersGroups(group);
        deleteFromGroups(group);
    }

    private void insertIntoUsersGroups(Integer userId, Integer groupId) {
        try {
            final String sql = "Insert into users_groups (user_id, group_id) values (?, ?)";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, groupId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteFromUsersGroups(Group group) {
        try {
            final String sql = "DELETE FROM users_groups WHERE group_id = ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, group.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteFromGroups(Group group) {
        try {
            final String sql = "DELETE FROM groups WHERE id = ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, group.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
