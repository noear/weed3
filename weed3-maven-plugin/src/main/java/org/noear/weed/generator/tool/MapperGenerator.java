package org.noear.weed.generator.tool;

import org.noear.weed.DbContext;
import org.noear.weed.utils.StringUtils;
import org.noear.weed.wrap.TableWrap;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MapperGenerator {
    public static void createByDb(String modelPck, String packDir, String packName, DbContext db, String clzNameTml) throws IOException {
        File dir = new File(packDir);
        if (dir.exists() == false) {
            dir.mkdirs();
        }

        StringBuilder nameSb = new StringBuilder();

        for (TableWrap tw : db.dbTables()) {
            if (clzNameTml == null) {
                clzNameTml = "${table}";
            }

            nameSb.setLength(0);
            String tmp = clzNameTml.replace("${table}", tw.getName());

            for (String s : tmp.split("_")) {
                if (s.length() > 0) {
                    nameSb.append(s.substring(0, 1).toUpperCase());
                }

                if (s.length() > 1) {
                    nameSb.append(s.substring(1));
                }
            }

            String clzName = nameSb.toString();


            String code = buildByTable(modelPck, packName, tw, clzName);
            String fileFullName = packDir + clzName + "Mapper.java";

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

    public static void createByDb(String modelPck, String packDir, String packName, DbContext db) throws IOException {
        createByDb(modelPck, packDir, packName, db, "${table}");
    }

    public static void createByDb(String modelPck, String packName, DbContext db) throws IOException {
        if (packName == null) {
            throw new RuntimeException("Please enter @packName");
        }

        String classesDir = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        int targetIdx = classesDir.indexOf("/target/");

        String javaDir = classesDir.substring(0, targetIdx) + "/src/main/java/";
        String packDir = javaDir + packName.replace(".", "/") + "/";

        createByDb(modelPck, packDir, packName, db);
    }

    public static String buildByTable(String modelPck, String packName, TableWrap tw, String clzName) {
        StringBuilder sb = new StringBuilder();

        if (StringUtils.isEmpty(packName) == false) {
            sb.append("package ").append(packName).append(";").append("\n\n");
        }

        sb.append("import ").append(modelPck).append(".").append(clzName).append(";\n");
        sb.append("import org.noear.weed.BaseMapper;").append("\n\n");

        if (StringUtils.isEmpty(tw.getRemarks()) == false) {
            sb.append("/** ").append(tw.getRemarks()).append(" Mapper */\n");
        }

        sb.append("public interface ").append(clzName).append("Mapper extends BaseMapper<").append(clzName).append("> {").append("\n");
        sb.append("}");

        return sb.toString();
    }
}
