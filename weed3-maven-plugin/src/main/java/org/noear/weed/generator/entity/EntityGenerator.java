package org.noear.weed.generator.entity;

import org.noear.weed.DbContext;
import org.noear.weed.generator.utils.NamingUtils;
import org.noear.weed.utils.StringUtils;
import org.noear.weed.wrap.ColumnWrap;
import org.noear.weed.wrap.TableWrap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 实体构建工具
 *
 * demo:
 * EntityBuilder.createByDb("demo.dso.mapper",db);
 * */
public class EntityGenerator {
    public static void createByDb(String packDir, String packName, DbContext db, String clzNameTml, boolean camel) throws IOException {
        File dir = new File(packDir);
        if (dir.exists() == false) {
            dir.mkdirs();
        }

        for (TableWrap tw : db.dbTables()) {
            if(clzNameTml == null){
                clzNameTml="${table}";
            }
            String tmp = clzNameTml.replace("${table}", tw.getName());
            String clzName = NamingUtils.toCamelString(tmp, true);


            String code = buildByTable(packName, tw, clzName, camel);
            String fileFullName = packDir + clzName + ".java";

            File file = new File(fileFullName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            try (FileWriter fw = new FileWriter(file)) {
                fw.write(code);
            }

            System.out.println("Generated : "+file.getAbsolutePath());
        }
    }


    public static void createByDb(String packDir, String packName, DbContext db) throws IOException {
        createByDb(packDir, packName, db, "${table}", false);
    }

    public static void createByDb(String packName, DbContext db) throws IOException {
        if (packName == null) {
            throw new RuntimeException("Please enter @packName");
        }

        String classesDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        int targetIdx = classesDir.indexOf("/target/");

        String javaDir = classesDir.substring(0, targetIdx) + "/src/main/java/";
        String packDir = javaDir + packName.replace(".", "/") + "/";

        createByDb(packDir, packName, db);
    }

    public static String buildByTable(String packName, TableWrap tw, String clzName, boolean camel) {
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isEmpty(tw.getRemarks()) == false) {
            sb.append("/** ").append(tw.getRemarks()).append(" */\n");
        }

        sb.append("@Getter").append("\n");
        sb.append("@Setter").append("\n");
        sb.append("@Table(\"").append(tw.getName()).append("\")\n");
        sb.append("public class ").append(clzName).append("{").append("\n");

        for (ColumnWrap cw : tw.getColumns()) {
            if (StringUtils.isEmpty(cw.getRemarks()) == false) {
                sb.append("  //").append(cw.getRemarks()).append("\n");
            }

            if(tw.getPks().contains(cw.getName())){
                sb.append("  @PrimaryKey").append("\n");
            }

            sb.append("  private ").append(SqlTypeMap.getType(cw)).append(" ");
            if(camel){
                sb.append(NamingUtils.toCamelString(cw.getName()));
            }else {
                sb.append(cw.getName());
            }

            sb.append(";").append("\n");
        }

        sb.append("}");


        if (StringUtils.isEmpty(packName) == false) {
            sb2.append("package ").append(packName).append(";").append("\n\n");
        }

        sb2.append("import lombok.Getter;").append("\n");
        sb2.append("import lombok.Setter;").append("\n");
        if (sb.indexOf(" Date ") > 0) {
            sb2.append("import java.util.Date;").append("\n");
        }
        if (sb.indexOf(" BigDecimal ") > 0) {
            sb2.append("import java.math.BigDecimal;").append("\n");
        }
        sb2.append("import org.noear.weed.annotation.*;").append("\n");


        sb2.append("\n");
        sb2.append(sb);

        return sb2.toString();
    }
}
