package tests;

import dto.User;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import repositories.UserRepository;
import repositories.impl.UserRepositoryImpl;

import java.util.Arrays;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserRepositoryTest extends RepositoryTestBase<User, UserRepository> {

    @Test
    public void order_0_getCount() {
        Assert.assertEquals(0, repository.getCount());
    }

    @Test
    public void order_1_add() {
        final User actual = new User(null, "sample user login", "sample user password");
        repository.add(actual);

        final Integer userId = actual.getId();
        Assert.assertNotNull(userId);

        final User expected = repository.findById(userId);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void order_2_exists() {
        final User notExistingUser = new User(10000, "not existing login", "not existing password");

        Assert.assertFalse(repository.exists(notExistingUser));

        repository.add(notExistingUser);
        Assert.assertTrue(repository.exists(notExistingUser));
    }

    @Test
    public void order_3_update() {
        final User user = new User(null, "new user login", " new user password");
        repository.add(user);

        final Integer userId = user.getId();

        final User expected = new User(userId, "update user login", "update user password");
        repository.update(expected);
        final User actual = repository.findById(userId);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void order_4_addOrUpdate() {
        final User actual = new User(null, "new user login add", "new user password add");
        repository.addOrUpdate(actual);

        final Integer id = actual.getId();
        User expected = repository.findById(id);

        Assert.assertEquals(expected, actual);

        actual.setLogin("new user login update");
        repository.addOrUpdate(actual);
        expected = repository.findById(id);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void order_5_delete() {
        final User user = new User(1, "new user login delete", "new user password delete");
        repository.add(user);
        repository.delete(user);

        Assert.assertFalse(repository.exists(user));
    }

    @Test
    public void order_6_findById() {
        final User actual = new User(1, "new user login by id", "new user password by id");
        repository.add(actual);
        final Integer id = actual.getId();

        final User expected = repository.findById(id);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void order_7_findByName() {
        final List<User> actual = Arrays.asList(
                new User(null, "findByNameLogin1", "new user password1"),
                new User(null, "findByNameLogin2", "new user password2"),
                new User(null, "findByNameLogin3", "new user password3"));

        actual.forEach(user -> repository.add(user));

        final List<User> expected = repository.findByName("By");

        Assert.assertEquals(expected, actual);
        Assert.assertNotSame(expected, actual);
    }

    @Test
    public void order_8_findByGroupId() {
        final List<User> byGroupId = repository.findByGroupId(20);
        byGroupId.forEach(System.out::println);

    }

    @Test
    public void order_9_getCount() {
        Assert.assertEquals(0, repository.getCount());
    }

    @Override
    protected UserRepository Create() {
        return new UserRepositoryImpl();
    }
}
