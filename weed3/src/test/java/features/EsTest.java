package features;

import features.model.LogDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.Utils;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.weed.elasticsearch.EsContext;

import java.time.LocalDateTime;
import java.util.*;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */
@RunWith(SolonJUnit4ClassRunner.class)
public class EsTest {
    final String indiceNoExit = "water$water_log_api";
    final String indiceNew = "water$water_log_api_new";
    final String indice = "water$water_log_api_202110";

    EsContext context = new EsContext("eshost:30480,eshost:30480");

    @Test
    public void test0() throws Exception {
        assert context.tableExist(indiceNoExit) == false;
    }

    @Test
    public void test1() throws Exception {
        if (context.tableExist(indiceNew)) {
            context.tableDrop(indiceNew);
        }

        assert context.tableExist(indiceNew) == false;

        String dsl = Utils.getResourceAsString("demo/log.json", "utf-8");
        context.tableCreate(indiceNew, dsl);


        assert context.tableExist(indiceNew) == true;
    }

    @Test
    public void test2() throws Exception {
        if (context.tableExist(indice) == false) {
            String dsl = Utils.getResourceAsString("demo/log.json", "utf-8");
            context.tableCreate(indice, dsl);
        }

        assert context.tableExist(indice) == true;
    }

    @Test
    public void test3() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        LogDo logDo = new LogDo();
        logDo.logger = "waterapi";
        logDo.log_id = SnowflakeUtils.genId();
        logDo.trace_id = Utils.guid();
        logDo.class_name = this.getClass().getName();
        logDo.thread_name = Thread.currentThread().getName();
        logDo.tag = "test1";
        logDo.level = 2;
        logDo.content = json;
        logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
        logDo.log_fulltime = new Date();


        context.table(indice).upsert(Utils.guid(), logDo);
    }

    @Test
    public void test4() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        LogDo logDo = new LogDo();
        logDo.logger = "waterapi";
        logDo.log_id = SnowflakeUtils.genId();
        logDo.trace_id = Utils.guid();
        logDo.class_name = this.getClass().getName();
        logDo.thread_name = Thread.currentThread().getName();
        logDo.tag = "test2";
        logDo.level = 3;
        logDo.content = json;
        logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
        logDo.log_fulltime = new Date();


        context.table(indice).insert(logDo);
    }

    @Test
    public void test5() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        Map<String, LogDo> docs = new LinkedHashMap<>();

        for (int i = 0; i < 20; i++) {
            LogDo logDo = new LogDo();
            logDo.logger = "waterapi";
            logDo.log_id = SnowflakeUtils.genId();
            logDo.trace_id = Utils.guid();
            logDo.class_name = this.getClass().getName();
            logDo.thread_name = Thread.currentThread().getName();
            logDo.tag = "map1";
            logDo.level = 4;
            logDo.content = json;
            logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
            logDo.log_fulltime = new Date();

            docs.put(Utils.guid(), logDo);
        }


        context.table(indice).upsertList(docs);
    }

    @Test
    public void test6() throws Exception {
        String json = Utils.getResourceAsString("demo/log.json", "utf-8");

        List<LogDo> docs = new ArrayList<>();

        for (int i = 0; i < 50; i++) {
            LogDo logDo = new LogDo();
            logDo.logger = "waterapi";
            logDo.log_id = SnowflakeUtils.genId();
            logDo.trace_id = Utils.guid();
            logDo.class_name = this.getClass().getName();
            logDo.thread_name = Thread.currentThread().getName();
            logDo.tag = "list1";
            logDo.level = 5;
            logDo.content = json;
            logDo.log_date = LocalDateTime.now().toLocalDate().getDayOfYear();
            logDo.log_fulltime = new Date();

            docs.add(logDo);
        }


        context.table(indice).insertList(docs);
    }

}
