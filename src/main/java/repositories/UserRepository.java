package repositories;

import dto.User;

import java.util.List;

public interface UserRepository extends Repository<User> {

    public List<User> findByName(String userName);

    User findById(int id);

    List<User> findByGroupId(int groupId);

    void add(User user);

    void update(User user);

    void delete(User user);

}
