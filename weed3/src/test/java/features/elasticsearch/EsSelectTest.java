package features.elasticsearch;

import features.elasticsearch.model.LogDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.weed.elasticsearch.EsContext;
import org.noear.weed.model.Page;

/**
 * ElasticSearch 测试
 *
 * @author noear 2021/10/22 created
 */

@RunWith(SolonJUnit4ClassRunner.class)
public class EsSelectTest {

    final String indiceNoExit = "water$water_log_api";
    final String indiceNew = "water$water_log_api_new";
    final String indice = "water$water_log_api_202110";

    EsContext context = new EsContext("eshost:30480,eshost:30480");


    @Test
    public void test1() throws Exception {
        LogDo logDo = context.table(indice).selectById("1", LogDo.class);
        assert logDo != null;
        assert logDo.log_id == 1;
    }

    @Test
    public void test10() throws Exception {

        Page<LogDo> result = context.table(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test10_2() throws Exception {

        Page<LogDo> result = context.table(indice)
                .where(c -> c.termsIn("tag", "list1"))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
    }

    @Test
    public void test11() throws Exception {

        Page<LogDo> result = context.table(indice)
                .where(c -> c.must().term("tag", "list1").term("level", 3))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test20() throws Exception {

        Page<LogDo> result = context.table(indice)
                .where(c -> c.match("tag", "list1"))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test21() throws Exception {

        Page<LogDo> result = context.table(indice)
                .where(c -> c.must().match("tag", "list1").term("level", 3))
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test22() throws Exception {

        Page<LogDo> result = context.table(indice)
                .where(c -> c.must()
                        .match("tag", "list1")
                        .term("level", 3)
                        .add(c1 -> c1.mustNot()
                                .matchPrefix("summary", "${")
                                .matchPrefix("summary", "#{")))
                .limit(0, 10)
                .orderByDesc("log_id")
                .select(LogDo.class);

        System.out.println(result);
        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
    }

    @Test
    public void test30() throws Exception {
        //输出字段控制（选择模式）
        Page<LogDo> result = context.table(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .select("log_id,trace_id", LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > 0;
        assert result.getList().get(0).tag == null;
    }

    @Test
    public void test31() throws Exception {
        //输出字段控制（排除模式）
        Page<LogDo> result = context.table(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .select("!log_id,trace_id", LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id == 0;
        assert result.getList().get(0).tag != null;
    }

    @Test
    public void test40() throws Exception {
        //输出字段控制（选择模式）
        Page<LogDo> result = context.table(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .orderByAsc("log_id")
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }

    @Test
    public void test41() throws Exception {
        //输出字段控制（选择模式）
        Page<LogDo> result = context.table(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .orderByDesc("log_id")
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id > result.getList().get(1).log_id;
    }

    @Test
    public void test42() throws Exception {
        //输出字段控制（选择模式）
        Page<LogDo> result = context.table(indice)
                .where(c -> c.term("tag", "list1"))
                .limit(0, 10)
                .orderByDesc("level")
                .andByAsc("log_id")
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }

    @Test
    public void test50() throws Exception {
        //输出字段控制（选择模式）
        Page<LogDo> result = context.table(indice)
                .where(c -> c.must()
                        .term("tag", "list1")
                        .range("level", r -> r.gt(3)))
                .limit(0, 10)
                .orderByAsc("level")
                .andByAsc("log_id")
                .select(LogDo.class);

        assert result.getListSize() == 10;
        assert result.getList().get(0).level > 3;
        assert result.getList().get(0).log_id < result.getList().get(1).log_id;
    }
}
