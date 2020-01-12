package repositories.impl;

import dto.User;
import mappers.impl.UserExistsMapper;
import mappers.impl.UserIdMapper;
import mappers.impl.UserMapper;
import mappers.impl.UsersMapper;
import repositories.UserRepository;
import singleton.ConnectionSingleton;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserRepositoryImpl implements UserRepository {

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
            getConnection().commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rollbackTransaction() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findByName(String userName) {
        try {
            final String sql = "SELECT id, login, password FROM users WHERE login LIKE ? OR login = ? OR login LIKE ? OR login like ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setString(1, userName + '%');
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, '%' + userName + '%');
            preparedStatement.setString(4, '%' + userName);

            final ResultSet resultSet = preparedStatement.executeQuery();
            final List<User> userList = new UsersMapper().map(resultSet);

            return userList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    @Override
    public User findById(int id) {
        try {
            final String sql = "SELECT id, login, password FROM users WHERE id = ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, id);

            final ResultSet resultSet = preparedStatement.executeQuery();
            final User user = new UserMapper().map(resultSet);

            return user;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> findByGroupId(int groupId) {
        try {
            final String sql = "SELECT u.id, u.login, u.password FROM users_groups ug JOIN users u on ug.user_id = u.id " +
                    "where ug.group_id =  ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, groupId);

            final ResultSet resultSet = preparedStatement.executeQuery();

            final List<User> userList = new UsersMapper().map(resultSet);

            return userList;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        try {
            final String sql = "SELECT COUNT(1) FROM users";
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
    public boolean exists(User dto) {
        try {
            final String sql = "SELECT exists (SELECT 1 FROM users WHERE id = ? LIMIT 1)";
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
    public void add(User user) {
        try {
            final String sql = "INSERT INTO users (login, password) VALUES (?, ?) RETURNING ID";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());

            final ResultSet resultSet = preparedStatement.executeQuery();
            final Integer newId = new UserIdMapper().map(resultSet);

            user.setId(newId);

            user.getGroupList().stream()
                    .filter(group -> group.getId() != null)
                    .forEach(group -> insertIntoUsersGroups(user.getId(), group.getId()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        try {
            final String sql = "UPDATE users SET login = ?, password = ? WHERE id = ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setInt(3, user.getId());

            preparedStatement.executeUpdate();

            deleteFromUsersGroups(user);

            user.getGroupList().stream()
                    .filter(group -> group.getId() != null)
                    .forEach(group -> insertIntoUsersGroups(user.getId(), group.getId()));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addOrUpdate(User dto) {
        if (exists(dto)) {
            update(dto);
        } else {
            add(dto);
        }
    }

    @Override
    public void delete(User user) {
        deleteFromUsersGroups(user);
        deleteFromUsers(user);
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


    private void deleteFromUsersGroups(User user) {
        try {
            final String sql = "DELETE FROM users_groups WHERE user_id = ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, user.getId());
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteFromUsers(User user) {
        try {
            final String sql = "DELETE FROM users WHERE id = ?";
            final PreparedStatement preparedStatement = getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
