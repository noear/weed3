package weed3builder;

import org.noear.weed.DbContext;
import org.noear.weed.utils.StringUtils;
import org.noear.weed.wrap.ColumnWrap;
import org.noear.weed.wrap.TableWrap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体构建工具
 *
 * demo:
 * EntityBuilder.createByDb("demo.dso.mapper",db);
 * */
public class EntityBuilder {
    public static void createByDb(String packName, DbContext db) throws IOException {
        if(packName == null){
            throw new RuntimeException("Please enter @packName");
        }

        String classesDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        int targetIdx = classesDir.indexOf("/target/");

        String javaDir = classesDir.substring(0, targetIdx) + "/src/main/java/";
        String packDir = javaDir + packName.replace(".", "/") + "/";

        File dir = new File(packDir);
        if(dir.exists() == false){
            dir.mkdirs();
        }


        for (TableWrap tw : db.dbTables()) {
            String code = buildByTable(packName, tw);
            String fileFullName = packDir + tw.getName() + ".java";

            File file = new File(fileFullName);
            if(file.exists() ==false){
                file.createNewFile();
            }

            try(FileWriter fw = new FileWriter(file)) {
                fw.write(code);
            }
        }
    }

    public static String buildByTable(String packName, TableWrap tw) {
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb = new StringBuilder();


        if (StringUtils.isEmpty(tw.getName())) {
            sb.append("//").append(tw.getName()).append("\n");
        }
        sb.append("@Data").append("\n");
        sb.append("public class ").append(tw.getName()).append("{").append("\n");

        for (ColumnWrap cw : tw.getColumns()) {
            if (StringUtils.isEmpty(cw.getRemarks()) == false) {
                sb.append("//").append(cw.getRemarks()).append("\n");
            }
            sb.append("  private ").append(getType(cw)).append(" ").append(cw.getName()).append(";").append("\n");
        }

        sb.append("}");


        if (StringUtils.isEmpty(packName) == false) {
            sb2.append("package ").append(packName).append(";").append("\n\n");
        }

        sb2.append("import lombok.Data;").append("\n");
        if (sb.indexOf(" Date ") > 0) {
            sb2.append("import java.util.Date;").append("\n");
        }

        sb2.append("\n");
        sb2.append(sb);

        return sb2.toString();
    }


    public static Map<Integer, String> mapping = new HashMap<Integer, String>();

    public final static String UNKNOW = "UNKNOW";
    public final static String SPECIAL = "SPECIAL";

    static {
        mapping.put(Types.BIGINT, "Long");
        mapping.put(Types.BINARY, "byte[]");
        mapping.put(Types.BIT, "Integer");
        mapping.put(Types.BLOB, "byte[]");
        mapping.put(Types.BOOLEAN, "Integer");
        mapping.put(Types.CHAR, "String");
        mapping.put(Types.CLOB, "String");
        mapping.put(Types.DATALINK, UNKNOW);
        mapping.put(Types.DATE, "Date");
        mapping.put(Types.DECIMAL, "SPECIAL");
        mapping.put(Types.DISTINCT, UNKNOW);
        mapping.put(Types.DOUBLE, "Double");
        mapping.put(Types.FLOAT, "Float");
        mapping.put(Types.INTEGER, "Integer");
        mapping.put(Types.JAVA_OBJECT, UNKNOW);
        mapping.put(Types.LONGNVARCHAR, "String");
        mapping.put(Types.LONGVARBINARY, "byte[]");
        mapping.put(Types.LONGVARCHAR, "String");
        mapping.put(Types.NCHAR, "String");
        mapping.put(Types.NVARCHAR, "String");
        mapping.put(Types.NCLOB, "String");
        mapping.put(Types.NULL, UNKNOW);
        // 根据长度制定Integer，或者Double
        mapping.put(Types.NUMERIC, SPECIAL);
        mapping.put(Types.OTHER, "Object");
        mapping.put(Types.REAL, "Double");
        mapping.put(Types.REF, UNKNOW);

        mapping.put(Types.SMALLINT, "Integer");
        mapping.put(Types.SQLXML, "SQLXML");
        mapping.put(Types.STRUCT, UNKNOW);
        mapping.put(Types.TIME, "Date");
        mapping.put(Types.TIMESTAMP, "Date");
        mapping.put(Types.TINYINT, "Integer");
        mapping.put(Types.VARBINARY, "byte[]");
        mapping.put(Types.VARCHAR, "String");

        // jdk 8 support
        mapping.put(Types.REF_CURSOR, UNKNOW);
        mapping.put(Types.TIMESTAMP_WITH_TIMEZONE, "Date");
        mapping.put(Types.TIME_WITH_TIMEZONE, "Date");

    }

    public static String getType(ColumnWrap cw){
        return getType(cw.getType(),cw.getSize(),cw.getDigit());
    }

    public static String getType(Integer sqlType, Integer size, Integer digit) {
        String type = mapping.get(sqlType);
        if (type.equals(SPECIAL)) {

            if (digit != null && digit != 0) {
                return "Double";
            } else {
                // 有可能是BigInt，但先忽略，这种情况很少，用户也可以手工改
                if (size >= 9) {
                    return "Long";
                } else {
                    return "Integer";
                }
            }
        } else {
            return type;
        }
    }
}
