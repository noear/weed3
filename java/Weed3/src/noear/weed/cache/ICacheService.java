package noear.weed.cache;

/**
 * Created by noear on 14-6-12.
 */
public interface ICacheService {

    public void store(String key, Object obj, int seconds);

    public Object get(String key);

    public void remove(String key);

    public int getDefalutSeconds();

    public String getCacheKeyHead();
}
