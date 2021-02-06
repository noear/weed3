package weed3mongo.model;

import java.io.Serializable;

/**
 * @author noear 2021/2/1 created
 */

public class UserModel implements Serializable {
    public long id;
    public int type;
    public String name;
    public String nickname;

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
