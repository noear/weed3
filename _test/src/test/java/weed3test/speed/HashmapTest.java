package weed3test.speed;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class HashmapTest {
    @Test
    public void test0(){

    }

    @Test
    public void test1(){
        Map<String,String> map = new HashMap<>();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            map.put("key","key");
            map.get("key");
        }
        long times = System.currentTimeMillis() - start;

        System.out.println("test1用时：" + times);
    }

    @Test
    public void test2(){
        Map<Class<?>,String> map = new HashMap<>();
        Class<?> clz = HashmapTest.class;

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            map.put(clz,"key");
            map.get(clz);
        }
        long times = System.currentTimeMillis() - start;

        System.out.println("test2用时：" + times);
    }

    @Test
    public void test3(){
        Map<String,String> map = new HashMap<>();
        Class<?> clz = HashmapTest.class;

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            map.put(clz.getName(),"key");
            map.get(clz.getName());
        }
        long times = System.currentTimeMillis() - start;

        System.out.println("test3用时：" + times);
    }

    @Test
    public void test4(){
        Map<HashmapTest,String> map = new HashMap<>();

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            map.put(this,"key");
            map.get(this);
        }
        long times = System.currentTimeMillis() - start;

        System.out.println("test4用时：" + times);
    }
}
