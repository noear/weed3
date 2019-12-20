package org.noear.weed.wrap;

import org.noear.weed.DbTableQueryBase;
import org.noear.weed.SQLBuilder;

public class DbH2Adapter implements DbAdapter{

    @Override
    public String tableFormat(String tb) {
        return "`" + tb + "`";
    }

    @Override
    public String columnFormat(String col) {
        return "`" + col + "`";
    }

    //top,page 和mysql一样

}
