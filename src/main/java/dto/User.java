package dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class User extends Base {

    private String login;
    private String password;
    private List<Group> groupList;

    public User(Integer id, String login, String password) {
        super(id);
        this.login = login;
        this.password = password;
        this.groupList = new ArrayList<>();
    }

    public User(Integer id, String login, String password, List<Group> groupList) {
        super(id);
        this.login = login;
        this.password = password;
        this.groupList = groupList;
    }

    public void addGroup(Group group) {
        if (groupList == null) {
            groupList = new ArrayList<Group>();
        }
        groupList.add(group);
    }

    public void deleteGroup(Group group) {
        if (groupList != null) {
            groupList.remove(group);
        }
    }
}
