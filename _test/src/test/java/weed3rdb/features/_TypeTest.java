package weed3rdb.features;

import org.junit.Test;
import org.noear.weed.DataItem;

import java.util.Map;

public class _TypeTest {
    @Test
    public void test(){
        Map<String,Object> args = new DataItem().set("date",20201010).getMap();
    }
}
