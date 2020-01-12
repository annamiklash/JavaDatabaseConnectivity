package tests;

import dto.Group;
import dto.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import repositories.impl.GroupRepositoryImpl;
import repositories.impl.UserRepositoryImpl;
import singleton.ConnectionSingleton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsersGroupsTest {

    private UserRepositoryImpl userRepository = new UserRepositoryImpl();
    private GroupRepositoryImpl groupRepository = new GroupRepositoryImpl();

    @Before
    public void before() {
        groupRepository.beginTransaction();
        userRepository.beginTransaction();
    }


    @After
    public void after() throws SQLException {
        ConnectionSingleton.getInstance().rollback();
    }

    @Test
    public void addGroupWithUsers() {
        final List<User> expected = Arrays.asList(
                new User(null, "login 1", "password 1", new ArrayList<>()),
                new User(null, "login 2", "password 2", new ArrayList<>())
        );

        expected.forEach(user -> userRepository.add(user));

        final Group group = new Group(null, "group name", "group description");
        expected.forEach(group::addUser);

        groupRepository.add(group);
        final Integer groupId = group.getId();

        final List<User> actual = userRepository.findByGroupId(groupId);

        Assert.assertNotSame(expected, actual);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void deleteGroupWithUsers() {
        final List<User> expected = Arrays.asList(
                new User(null, "login 1", "password 1", new ArrayList<>()),
                new User(null, "login 2", "password 2", new ArrayList<>())
        );

        expected.forEach(user -> userRepository.add(user));

        final Group group = new Group(null, "group name", "group description");
        expected.forEach(group::addUser);

        groupRepository.add(group);
        groupRepository.delete(group);

        Assert.assertFalse(groupRepository.exists(group));
    }

    @Test
    public void updateGroupWithUsers() {
        final List<User> userList = Arrays.asList(
                new User(null, "login 1", "password 1", new ArrayList<>()),
                new User(null, "login 2", "password 2", new ArrayList<>())
        );

        userList.forEach(user -> userRepository.add(user));

        final Group group = new Group(null, "group name", "group description");
        userList.forEach(group::addUser);

        groupRepository.add(group);
        final Integer groupId = group.getId();

        final List<User> expectedList = Arrays.asList(
                new User(null, "login 1 updated", "password 1 updated", new ArrayList<>()),
                new User(null, "login 2 updated", "password 2 updated", new ArrayList<>()),
                new User(null, "login 3 updated", "password 3 updated", new ArrayList<>())
        );

        expectedList.forEach(user -> userRepository.add(user));

        final Group expectedGroup = new Group(groupId, "updated group name", "updated description");
        expectedList.forEach(expectedGroup::addUser);

        groupRepository.update(expectedGroup);

        final Group actualGroup = groupRepository.findById(groupId);
        final List<User> actualList = userRepository.findByGroupId(groupId);
        actualList.forEach(actualGroup::addUser);
        Assert.assertEquals(expectedGroup, actualGroup);

    }

    @Test
    public void addUsersWithGroups() {
        final List<Group> expected = Arrays.asList(
                new Group(null, "name 1", "description 1", new ArrayList<>()),
                new Group(null, "name 2", "description 2", new ArrayList<>())
        );

        expected.forEach(group -> groupRepository.add(group));

        final User user = new User(null, "user login", "user password");
        expected.forEach(user::addGroup);

        userRepository.add(user);
        final Integer userId = user.getId();

        final List<Group> actual = groupRepository.findByUserId(userId);

        Assert.assertNotSame(expected, actual);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void deleteUsersWithGroups() {
        final List<Group> expected = Arrays.asList(
                new Group(null, "name 1", "description 1", new ArrayList<>()),
                new Group(null, "name 2", "description 2", new ArrayList<>())
        );

        expected.forEach(group -> groupRepository.add(group));

        final User user = new User(null, "user login", "user password");
        expected.forEach(user::addGroup);

        userRepository.add(user);
        userRepository.delete(user);

        Assert.assertFalse(userRepository.exists(user));
    }

    @Test
    public void updateUsersWithGroups() {
        final List<Group> expected = Arrays.asList(
                new Group(null, "name 1", "description 1", new ArrayList<>()),
                new Group(null, "name 2", "description 2", new ArrayList<>())
        );

        expected.forEach(group -> groupRepository.add(group));

        final User user = new User(null, "user login", "user password");
        expected.forEach(user::addGroup);

        userRepository.add(user);
        final Integer userId = user.getId();

        final List<Group> expectedList = Arrays.asList(
                new Group(null, "updated name 1", "updated description 1", new ArrayList<>()),
                new Group(null, "updated name 2", "updated description 2", new ArrayList<>())
        );

        expectedList.forEach(group -> groupRepository.add(group));

        final User expectedUser = new User(userId, "updated user login", "updated user password");
        expectedList.forEach(expectedUser::addGroup);

        userRepository.update(expectedUser);

        final User actualUser = userRepository.findById(userId);
        final List<Group> actualList = groupRepository.findByUserId(userId);
        actualList.forEach(actualUser::addGroup);
        Assert.assertEquals(expectedUser, actualUser);

    }


}
