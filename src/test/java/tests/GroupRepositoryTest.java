package tests;

import dto.Group;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import repositories.GroupRepository;
import repositories.impl.GroupRepositoryImpl;

import java.util.Arrays;
import java.util.List;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupRepositoryTest extends RepositoryTestBase<Group, GroupRepository> {


    @Test
    public void order_0_getCount() {
        Assert.assertEquals(0, repository.getCount());
    }

    @Test
    public void order_1_add() {
        final Group actual = new Group(null, "sample group name", "sample group description");
        repository.add(actual);

        final Integer actualGroupId = actual.getId();
        Assert.assertNotNull(actualGroupId);

        final Group expected = repository.findById(actualGroupId);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void order_2_update() {
        final Group group = new Group(null, "new group name", "new group description");
        repository.add(group);

        final Integer groupId = group.getId();

        final Group expected = new Group(groupId, "update group name", "update group description");
        repository.update(expected);
        final Group actual = repository.findById(groupId);

        Assert.assertEquals(expected, actual);

    }

    @Test
    public void order_3_addOrUpdate() {
        final Group actual = new Group(null, "new group name add", "new group description");
        repository.addOrUpdate(actual);

        final Integer id = actual.getId();
        Group expected = repository.findById(id);

        Assert.assertEquals(actual, expected);

        actual.setGroupName("new group name update");
        repository.addOrUpdate(actual);
        expected = repository.findById(id);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void order_4_delete() {
        final Group group = new Group(null, "new group name delete", "new group description delete");
        repository.add(group);
        repository.delete(group);

        Assert.assertFalse(repository.exists(group));
    }

    @Test
    public void order_5_findById() {
        final Group actual = new Group(null, "new group name by id", "new group description by id");
        repository.add(actual);
        final Integer id = actual.getId();

        final Group expected = repository.findById(id);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void order_6_findByName() {
       final List<Group> actual = Arrays.asList(
                new Group(null, "findByName1", "new description1 group"),
                new Group(null, "findByName2", "new description2 group"),
                new Group(null, "findByName3", "new description3 group"));

        actual.forEach(group -> repository.add(group));

        final List<Group> expected = repository.findByName("By");

        Assert.assertEquals(expected, actual);
        Assert.assertNotSame(expected, actual);
    }

    @Test
    public void order_7_exists() {
        final Group notExistingGroup = new Group(1000, "not existing group", "new group not existsing");

        Assert.assertFalse(repository.exists(notExistingGroup));

        repository.add(notExistingGroup);
        Assert.assertTrue(repository.exists(notExistingGroup));
    }

    @Test
    public void order_8_findByUserId() {
        final List<Group> byUserId = repository.findByUserId(5);
        byUserId.forEach(System.out::println);

    }

    @Test
    public void order_9_getCount() {
        Assert.assertEquals(0, repository.getCount());
    }

    @Override
    protected GroupRepository Create() {
        return new GroupRepositoryImpl();
    }


}
