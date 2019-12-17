package weed3test.speed;

import org.junit.Test;

public class StringTest {
    //3个字符串，性能区别不大；4个或以上 StringBuilder 好
    @Test
    public void test1() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String tmp = "test" + i + "a";
        }
        long times = System.currentTimeMillis() - start;

        System.out.println("用时：" + times);
    }

    @Test
    public void test2() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String tmp = new StringBuilder().append("test").append(i).append("a").toString();
        }
        long times = System.currentTimeMillis() - start;

        System.out.println("用时：" + times);
    }


    @Test
    public void test11() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String tmp = "test" + i + "a" + "d";
        }
        long times = System.currentTimeMillis() - start;

        System.out.println("用时：" + times);
    }

    @Test
    public void test12() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String tmp = new StringBuilder().append("test").append(i).append("a").append("d").toString();
        }
        long times = System.currentTimeMillis() - start;

        System.out.println("用时：" + times);
    }
}
