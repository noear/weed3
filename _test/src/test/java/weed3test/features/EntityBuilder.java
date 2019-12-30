package weed3test.features;

import org.noear.weed.utils.StringUtils;
import org.noear.weed.wrap.ColumnWrap;
import org.noear.weed.wrap.TableWrap;
import weed3test.dso.JavaDbType;

public class EntityBuilder {
    public static String buildByTable(TableWrap tw) {
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb = new StringBuilder();


        if(StringUtils.isEmpty(tw.getName())){
            sb.append("//").append(tw.getName()).append("\n");
        }
        sb.append("@Data").append("\n");
        sb.append("public class ").append(tw.getName()).append("{").append("\n");

        for(ColumnWrap cw : tw.getColumns()) {
            if(StringUtils.isEmpty(cw.getRemarks()) == false){
                sb.append("//").append(cw.getRemarks()).append("\n");
            }
            sb.append("  private ").append(JavaDbType.getType(cw)).append(" ").append(cw.getName()).append(";").append("\n");
        }

        sb.append("}");


        sb2.append("import lombok.Data;").append("\n");
        if(sb.indexOf(" Date ") > 0){
            sb2.append("import java.util.Date;").append("\n");
        }

        sb2.append("\n");
        sb2.append(sb);

        return sb2.toString();
    }
}
