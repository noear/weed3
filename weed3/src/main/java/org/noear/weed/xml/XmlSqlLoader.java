package org.noear.weed.xml;

import org.noear.weed.utils.IOUtils;
import org.noear.weed.utils.ThrowableUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class XmlSqlLoader {
    public static XmlSqlLoader _g = new XmlSqlLoader();

    private static String _lock = "";

    private boolean is_loaed = false;
    private List<URL> xmlFiles = new ArrayList<>();

    /**
     * 加载扩展文件夹（或文件）
     */
    public static void load() throws Exception {
        if (_g.is_loaed == false) {
            synchronized (_lock) {
                if (_g.is_loaed == false) {
                    _g.is_loaed = true;

                    _g.load0();
                }
            }
        }
    }

    public static void tryLoad() {
        try {
            load();
        } catch (Throwable ex) {
            ex = ThrowableUtils.throwableUnwrap(ex);
            throw ThrowableUtils.throwableWrap(ex);
        }
    }

    private void load0() throws Exception {
        XmlFileScaner.scan("weed3/", ".xml")
                .stream()
                .map(k -> IOUtils.getResource(k))
                .filter(url -> url != null)
                .forEach(url -> _g.xmlFiles.add(url));

        if (_g.xmlFiles.size() == 0) {
            return;
        }

        //构建代码
        List<String> codes = new ArrayList<>();
        for (URL file : _g.xmlFiles) {
            System.out.println("[Weed] Xml Compiler: " + file);

            String code = XmlSqlCompiler.parse(file);

            if (code != null) {
                codes.add(code);
            }
        }

        if (codes.size() == 0) {
            return;
        }

        boolean is_ok = CompilerUtil.instance().compiler(codes);
        if (is_ok) {
            CompilerUtil.instance().loadClassAll(true);
        } else {
            String error = CompilerUtil.instance().getCompilerMessage();
            System.out.println("[Weed] " + error);
            throw new RuntimeException(error);
        }
    }


    private XmlSqlLoader() {
    }
}
