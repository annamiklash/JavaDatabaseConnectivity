package repositories;

import dto.Group;

import java.util.List;

public interface GroupRepository extends Repository<Group> {

    public List<Group> findByName(String name);

    Group findById(int id);

    List<Group> findByUserId(int userId);

    void add(Group user);

    void update(Group user);

    void delete(Group user);
}
