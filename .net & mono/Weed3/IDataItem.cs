using System;
using System.Collections.Generic;

namespace Noear.Weed {
    public interface IDataItem  {
        int count();
        void clear();
        bool exists(string name);
        IEnumerable<string> keys();
        
        IDataItem set(String name, Object value);
        Object get(String name);
        Variate getVariate(String name);
        T toItem<T>(T item) where T : IBinder;
        short getShort(String name);
        int getInt(String name);
        long getLong(String name);
        double getDouble(String name);
        float getFloat(String name);
        String getString(String name);
        bool getBoolean(String name);
        DateTime getDateTime(String name);
        
        void forEach(Action<String, Object> callback);
    }
}
