package weed3mongo.model;

import java.io.Serializable;

/**
 * @author noear 2021/2/1 created
 */

public class UserModel implements Serializable {
    public long userId;
    public int type;
    public String name;
    public String nickName;

    @Override
    public String toString() {
        return "UserModel{" +
                "userId=" + userId +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
