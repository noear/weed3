package org.noear.weed;

import java.util.Map;

public interface IRender {
    String render(String path, Map<String,Object> args) throws Throwable;
}
