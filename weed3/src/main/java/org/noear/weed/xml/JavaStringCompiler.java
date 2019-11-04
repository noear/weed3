package org.noear.weed.xml;

import org.noear.weed.utils.AssertUtils;
import org.noear.weed.utils.StringUtils;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JavaStringCompiler {
    static JavaStringCompiler _instance;
    public static JavaStringCompiler instance(){
        if(_instance == null){
            _instance = new JavaStringCompiler();
        }

        return _instance;
    }

    //java的放编译后字节码(key:类全名,value:编译之后输出的字节码)
    private final Map<String, ByteJavaFileObject> javaFileMap = new ConcurrentHashMap<>();
    //java的编译器
    private final JavaCompiler javaCompiler;
    //java的文件管理器
    private JavaFileManager javaFileManager;
    //java的类加载器
    private StringClassLoader javaClassLoader;

    //存放编译过程中输出的信息
    private final DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<>();

    public JavaStringCompiler() {

        javaCompiler = ToolProvider.getSystemJavaCompiler();

        AssertUtils.notNull(javaCompiler,"ToolProvider.getSystemJavaCompiler() failed to execute");

        //标准的内容管理器,更换成自己的实现，覆盖部分方法
        StandardJavaFileManager standardFileManager = javaCompiler.getStandardFileManager(diagnosticsCollector, null, null);
        javaFileManager = new StringJavaFileManage(standardFileManager);
        javaClassLoader = new StringClassLoader();
    }

    /** 编译字符串源代码,编译失败在 diagnosticsCollector 中获取提示信息 */
    public boolean compiler(List<String> codes) {
        long startTime = System.currentTimeMillis();
        List<JavaFileObject> files = new ArrayList<>();

        //构造源代码对象
        for(String code : codes){
            files.add(new StringJavaFileObject(getFullClassName(code), code));
        }

        //生成编译任务并执行
        boolean is_ok = javaCompiler.getTask(null, javaFileManager, diagnosticsCollector, null, null, files).call();

        //编译耗时(单位ms)
        System.out.println(" compiler time::" + (System.currentTimeMillis() - startTime) + "ms");

        return is_ok;
    }


    /** 获取编译信息(错误 警告) */
    public String getCompilerMessage() {
        StringBuilder sb = StringUtils.borrowBuilder();
        for (Diagnostic diagnostic : diagnosticsCollector.getDiagnostics()) {
            sb.append(diagnostic.toString()).append("\r\n");
        }
        return StringUtils.releaseBuilder(sb);
    }

    /** 获取类的全名称（根据源码获取）*/
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

    /** 定义一个字符串的源码对象类 */
    private class StringJavaFileObject extends SimpleJavaFileObject {
        //等待编译的源码字段
        private String contents;

        //java源代码 => StringJavaFileObject对象 的时候使用
        public StringJavaFileObject(String className, String contents) {
            super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
            this.contents = contents;
        }

        //字符串源码会调用该方法
        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return contents;
        }
    }

    /** 定义一个编译之后的字节码对象类 */
    private class ByteJavaFileObject extends SimpleJavaFileObject {
        //存放编译后的字节码
        private ByteArrayOutputStream outPutStream;

        public ByteJavaFileObject(String className, Kind kind) {
            super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), kind);
        }

        //StringJavaFileManage 编译之后的字节码输出会调用该方法（把字节码输出到outputStream）
        @Override
        public OutputStream openOutputStream() {
            outPutStream = new ByteArrayOutputStream();
            return outPutStream;
        }

        //在类加载器加载的时候需要用到
        public byte[] getCompiledBytes() {
            return outPutStream.toByteArray();
        }
    }

    /**  定义一个 JavaFileManage 来控制编译之后字节码的输出位置 */
    private class StringJavaFileManage extends ForwardingJavaFileManager {
        StringJavaFileManage(JavaFileManager fileManager) {
            super(fileManager);
        }

        //获取输出的文件对象，它表示给定位置处指定类型的指定类。
        @Override
        public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
            ByteJavaFileObject javaFileObject = new ByteJavaFileObject(className, kind);
            javaFileMap.put(className, javaFileObject);
            return javaFileObject;
        }
    }

    /** 定义字符串类加载器, 用来加载动态的字节码 */
    private class StringClassLoader extends ClassLoader {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            ByteJavaFileObject fileObject = javaFileMap.get(name);
            if (fileObject != null) {
                byte[] bytes = fileObject.getCompiledBytes();
                return defineClass(name, bytes, 0, bytes.length);
            }
            try {
                return ClassLoader.getSystemClassLoader().loadClass(name);
            } catch (Exception e) {
                return super.findClass(name);
            }
        }
    }

    /** 将编译好的类加载到 SystemClassLoader */
    public void loadClassAll(boolean instantiation){
        javaFileMap.forEach((k,v)->{
            try{
                Class<?> cls = javaClassLoader.findClass(k);

                if(instantiation && cls!=null){
                    cls.newInstance();
                }

                System.out.println("String class loaded::" + k);
            }catch (Throwable ex){
                ex.printStackTrace();
            }
        });
    }
}