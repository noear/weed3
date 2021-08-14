package weed3demo.mapper;

import lombok.Getter;

import java.util.List;

@Getter
public class UserModel {
    public long id;
    public long user_id;
    public int sex;
    public String name;
    public String mobile;

    public List<UserModel> users;
}
