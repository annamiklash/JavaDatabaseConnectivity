package dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class Group extends Base {

    private String groupName;
    private String groupDescription;
    private List<User> userList;

    public Group(Integer id, String groupName, String groupDescription) {
        super(id);
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.userList = new ArrayList<>();
    }

    public Group(Integer id, String groupName, String groupDescription, List<User> userList) {
        super(id);
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.userList = userList;
    }

    public void addUser(User user) {
        if (userList == null) {
            userList = new LinkedList<User>();
        }
        userList.add(user);
    }

    public void deleteUser(User user) {
        if (userList != null) {
            userList.remove(user);
        }
    }
}
