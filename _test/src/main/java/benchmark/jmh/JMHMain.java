package benchmark.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import benchmark.jmh.jdbc.JdbcService;
import benchmark.jmh.weed.WeedService;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 性能测试入口,数据是Throughput，越大越好
 */
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Threads(1)
@Fork(1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class JMHMain {
    JdbcService jdbcService = null;
    WeedService weedService = null;

    @Setup
    public void init() {

        jdbcService = new JdbcService();
        jdbcService.init();

        weedService = new WeedService();
        weedService.init();

    }


    /*   JDBC,基准，有些方法性能飞快    */
    @Benchmark
    public void jdbcInsert() {
        jdbcService.addEntity();
    }

    @Benchmark
    public void jdbcSelectById() {
        jdbcService.getEntity();
    }

    @Benchmark
    public void jdbcExecuteJdbc() {
        jdbcService.executeJdbcSql();
    }


    /*   Weed3    */
    @Benchmark
    public void weedInsert() {
        weedService.addEntity();
    }

    @Benchmark
    public void weedSelectById() {
        weedService.getEntity();
    }

    @Benchmark
    public void weedLambdaQuery() {
        weedService.lambdaQuery();
    }

    @Benchmark
    public void weedExecuteJdbc() {
        weedService.executeJdbcSql();
    }

    @Benchmark
    public void weedExecuteTemplate() {
        weedService.executeTemplateSql();
    }

    @Benchmark
    public void weedFile() {
        weedService.sqlFile();
    }

    @Benchmark
    public void weedPageQuery() {
        weedService.pageQuery();
    }


    public static void main(String[] args) throws RunnerException {

          test();
//        Options opt = new
//                OptionsBuilder()
//                .include(JMHMain.class.getSimpleName())
//                .build();
//        new Runner(opt).run();
    }

    /**
     * 先单独运行一下保证每个测试都没有错误
     */
    public static void test() {
        JMHMain jmhMain = new JMHMain();
        jmhMain.init();
        for (int i = 0; i < 3; i++) {
            Method[] methods = jmhMain.getClass().getMethods();
            for (Method method : methods) {
                if (method.getAnnotation(Benchmark.class) == null) {
                    continue;
                }
                try {

                    method.invoke(jmhMain, new Object[0]);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    throw new IllegalStateException(" method " + method.getName(), ex);
                }

            }
        }

    }


}
