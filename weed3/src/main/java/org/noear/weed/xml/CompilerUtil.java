package org.noear.weed.xml;

import org.noear.liquor.DynamicCompiler;
import org.noear.liquor.DynamicCompilerException;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CompilerUtil {
    static CompilerUtil _instance;

    public static CompilerUtil instance() {
        if (_instance == null) {
            _instance = new CompilerUtil();
        }

        return _instance;
    }

    DynamicCompiler dynamicCompiler;
    Map<String, Class<?>> dynamicClassMap;

    private CompilerUtil() {
        dynamicCompiler = new DynamicCompiler();
    }

    /**
     * 编译字符串源代码,编译失败在 diagnosticsCollector 中获取提示信息
     */
    public boolean compiler(List<String> codes) {
        long startTime = System.currentTimeMillis();

        //构造源代码对象
        for (String code : codes) {
            dynamicCompiler.addSource(getFullClassName(code), code);
        }

        //生成编译任务并执行
        try {
            dynamicClassMap = dynamicCompiler.build();

            //编译耗时(单位ms)
            System.out.println("[Weed] compiler time::" + (System.currentTimeMillis() - startTime) + "ms");

            return true;
        } catch (DynamicCompilerException e) {
            return false;
        }
    }


    /**
     * 获取编译信息(错误 警告)
     */
    public String getCompilerMessage() {
        StringBuilder sb = new StringBuilder();
        for (Diagnostic<? extends JavaFileObject> diagnostic : dynamicCompiler.getOriginalErrors()) {
            sb.append(diagnostic.toString()).append("\r\n");
        }
        return sb.toString();
    }

    /**
     * 获取类的全名称（根据源码获取）
     */
    public static String getFullClassName(String sourceCode) {
        String className = "";
        Pattern pattern = Pattern.compile("package\\s+(.*?);");
        Matcher matcher = pattern.matcher(sourceCode);
        if (matcher.find()) {
            className = matcher.group(1).trim() + ".";
        }

        pattern = Pattern.compile("class\\s+(.*?)\\{");
        matcher = pattern.matcher(sourceCode);
        if (matcher.find()) {
            className += matcher.group(1).trim();
        }
        return className;
    }

    /**
     * 将编译好的类加载到 SystemClassLoader
     */
    public void loadClassAll(boolean instantiation) {
        dynamicClassMap.forEach((k, v) -> {
            try {
                Class<?> cls = dynamicClassMap.get(k);

                if (instantiation && cls != null) {
                    cls.newInstance();
                }

                System.out.println("[Weed] String class loaded::" + k);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        });
    }

    public static String varTypeParse(String str) {
        if (str.contains("[")) {
            return str.replace("[", "<").replace("]", ">");
        } else {
            return str;
        }
    }
}