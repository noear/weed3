package org.noear.weed.tool;

import org.noear.weed.DbContext;
import org.noear.weed.annotation.PrimaryKey;
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
public class EntityBuilder {
    public static void createByDb(String packDir, String packName, DbContext db, String clzNameTml) throws IOException {
        File dir = new File(packDir);
        if (dir.exists() == false) {
            dir.mkdirs();
        }


        for (TableWrap tw : db.dbTables()) {
            if(clzNameTml == null){
                clzNameTml="${table}";
            }
            String upName = tw.getName().substring(0,1).toUpperCase() + tw.getName().substring(1);
            String clzName = clzNameTml.replace("${table}", upName);

            String code = buildByTable(packName, tw, clzName);
            String fileFullName = packDir + clzName + ".java";

            File file = new File(fileFullName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            try (FileWriter fw = new FileWriter(file)) {
                fw.write(code);
            }
        }
    }

    public static void createByDb(String packDir, String packName, DbContext db) throws IOException {
        createByDb(packDir, packName, db, "${table}");
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

    public static String buildByTable(String packName, TableWrap tw, String clzName) {
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isEmpty(tw.getRemarks()) == false) {
            sb.append("/** ").append(tw.getRemarks()).append(" */\n");
        }

        sb.append("@Data").append("\n");
        sb.append("@Table(\"").append(tw.getName()).append("\")\n");
        sb.append("public class ").append(clzName).append("{").append("\n");

        for (ColumnWrap cw : tw.getColumns()) {
            if (StringUtils.isEmpty(cw.getRemarks()) == false) {
                sb.append("  //").append(cw.getRemarks()).append("\n");
            }

            if(tw.getPks().contains(cw.getName())){
                sb.append("  @PrimaryKey").append("\n");
            }

            sb.append("  private ").append(SqlTypeMap.getType(cw)).append(" ").append(cw.getName()).append(";").append("\n");
        }

        sb.append("}");


        if (StringUtils.isEmpty(packName) == false) {
            sb2.append("package ").append(packName).append(";").append("\n\n");
        }

        sb2.append("import lombok.Data;").append("\n");
        if (sb.indexOf(" Date ") > 0) {
            sb2.append("import java.util.Date;").append("\n");
        }
        sb2.append("import org.noear.weed.annotation.*;").append("\n");

        sb2.append("\n");
        sb2.append(sb);

        return sb2.toString();
    }
}
