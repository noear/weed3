package org.noear.weed;

import org.noear.weed.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TmlAnalyzer {

    private static Map<String, List<TmlMark>> cache = new HashMap<>();

    public static List<TmlMark> get(String tml) {
        List<TmlMark> marks = cache.get(tml);

        if (marks == null) {
            synchronized (tml.intern()) {
                marks = cache.get(tml);

                if (marks == null) {
                    marks = parse(tml);
                    cache.put(tml, marks);
                }
            }
        }

        return marks;
    }

    private static List<TmlMark> parse(String tml){
        List<TmlMark> marks = new ArrayList<>();

        Pattern pattern = Pattern.compile("@(\\w+)|[@\\$]{1}\\{(\\w+)\\}");
        Matcher m = pattern.matcher(tml);

        while (m.find()) {
            String mark = m.group(0);
            String name = m.group(1);
            if (StringUtils.isEmpty(name)) {
                name = m.group(2);
            }

            marks.add(new TmlMark(mark, name));
        }

        return marks;
    }
}
