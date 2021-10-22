package features;

import features.model.LogDo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.weed.elasticsearch.EsContext;
import org.noear.weed.model.Page;

/**
 * @author noear 2021/10/22 created
 */

@RunWith(SolonJUnit4ClassRunner.class)
public class EsSelectTest {

    final String indiceNoExit = "water$water_log_api";
    final String indiceNew = "water$water_log_api_new";
    final String indice = "water$water_log_api_202110";

    EsContext context = new EsContext("eshost:30480,eshost:30480");


    @Test
    public void test10() throws Exception {

        Page<LogDo> result = context.table(indice)
                .whereEq("tag", "list1")
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getSize() == 10;
    }

    @Test
    public void test11() throws Exception {

        Page<LogDo> result = context.table(indice)
                .whereEq("tag", "list1")
                .andEq("level", 3)
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getSize() == 10;
    }

    @Test
    public void test20() throws Exception {

        Page<LogDo> result = context.table(indice)
                .whereLk("tag", "list1")
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getSize() == 10;
    }

    @Test
    public void test21() throws Exception {

        Page<LogDo> result = context.table(indice)
                .whereLk("tag", "list1")
                .andEq("level", 3)
                .limit(0, 10)
                .select(LogDo.class);

        assert result.getSize() == 10;
    }
}
