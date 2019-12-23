package org.noear.weed.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class IOUtils {

    public static void fileWrite(File file, String content) throws Exception{
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        try {
            writer.write(content);
        }finally {
            writer.close();
        }
    }

    /** 根据字符串加载为一个类*/
    public static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        }catch (Throwable ex) {
            return null;
        }
    }

    public static <T> T loadEntity(String className) {
        try {
            Class<?> clz = Class.forName(className);
            if (clz != null) {
                return (T) clz.newInstance();
            }
        } catch (Throwable ex) {}
        return null;
    }

    //res::获取资源的RUL
    public static URL getResource(String name) {
        URL url = IOUtils.class.getResource(name);
        if (url == null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null) {
                url = loader.getResource(name);
            } else {
                url = ClassLoader.getSystemResource(name);
            }
        }

        return url;
    }

    /** 获取资源URL集*/
    public static Enumeration<URL> getResources(String name) throws IOException {
        Enumeration<URL> urls = IOUtils.class.getClassLoader().getResources(name);
        if (urls == null || urls.hasMoreElements()==false) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader != null) {
                urls = loader.getResources(name);
            } else {
                urls = ClassLoader.getSystemResources(name);
            }
        }

        return urls;
    }
}
