package noear.weed.cache;

import java.util.*;

/**
 * Created by yuety on 14-6-16.
 * 嵌入式缓存效率高；基于总量与超时双重管理(比自动超时要简单)
 *
 */
public class LocalCache implements ICacheService {
    private String _cacheKeyHead;
    private int _defaultSeconds;

    private int _max = 50000;
    private int _count = 0;

    private List<Integer> mks = new ArrayList<Integer>(); //key的顺序记录
    private HashMap<Integer, LocalCacheRecord> mcc = new HashMap<Integer, LocalCacheRecord>();   //缓存存储器

    public LocalCache(String keyHeader, int defSeconds) {
        this(keyHeader, defSeconds, 50000);
    }

    public LocalCache(String keyHeader, int defSeconds, int recordMax) {
        _cacheKeyHead = keyHeader;
        _defaultSeconds = defSeconds;
        _max = recordMax;
    }

    public void store(String key, Object obj, int seconds) {
        Integer hashKey = (_cacheKeyHead + "$" + key).hashCode();
        LocalCacheRecord val = new LocalCacheRecord(obj, seconds);

        mcc.put(hashKey, val);
        mks.add(hashKey);

        _count++;
        if (_count > _max) //总量控制
        {
            Integer k = mks.get(0);
            mcc.remove(k);
            mks.remove(0);
            _count--;
        }
    }

    public Object get(String key) {
        Integer hashKey = (_cacheKeyHead + "$" + key).hashCode();
        LocalCacheRecord val = mcc.get(hashKey);

        if (val == null)
            return null;

        if (val.time < new Date().getTime()) {
            mcc.remove(hashKey);
            mks.remove(hashKey);
            _count--;
            return null;
        } else
            return val.data;
    }

    public void remove(String key) {
        Integer hashKey = (_cacheKeyHead + "$" + key).hashCode();

        mcc.remove(hashKey);
        mks.remove(hashKey);
        _count--;
    }

    public void clear() {
        mks.clear();
        mcc.clear();
        _count = 0;
    }

    public int getDefalutSeconds() {
        return _defaultSeconds;
    }

    public String getCacheKeyHead() {
        return _cacheKeyHead;
    }
}
