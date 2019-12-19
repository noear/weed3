package weed3test.features;

import org.junit.Test;
import org.noear.weed.DbContext;
import weed3test.DbUtil;
import weed3test.model.AppxCopyModel;
import weed3test.util.Datetime;

public class _TimeTest {
    DbContext db = DbUtil.db;

    private AppxCopyModel _model;
    private AppxCopyModel model() throws Exception{
        if(_model == null) {
            _model = db.table("appx_copy")
                    .whereEq("app_id", 22)
                    .select("*")
                    .getItem(AppxCopyModel.class);

            System.out.println(db.lastCommand.text);
        }

        return _model;
    }

    @Test
    public void test_datetime1() throws Exception {
        AppxCopyModel m = model();
        Datetime log_fulltime = new Datetime(m.log_fulltime);

        assert m.app_id == 22;
        assert log_fulltime.getSeconds() == m.datetime1.getSecond();
    }

    @Test
    public void test_date1() throws Exception {
        AppxCopyModel m = model();
        Datetime log_fulltime = new Datetime(m.log_fulltime);

        assert m.app_id == 22;
        assert log_fulltime.getDays() == m.date1.getDayOfMonth();
    }

    @Test
    public void test_time1() throws Exception {
        AppxCopyModel m = model();
        Datetime log_fulltime = new Datetime(m.log_fulltime);

        assert m.app_id == 22;
        assert log_fulltime.getSeconds() == m.time1.getSecond();
    }
}
