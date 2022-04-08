package lock.web;

import org.noear.solon.Solon;
import org.noear.solon.annotation.Controller;
import org.noear.solon.annotation.Mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author noear
 * @since 1.6
 */
@Controller
public class App {
    public static void main(String[] args) {
        Solon.start(App.class, args);
    }

    List<String> ary = new ArrayList<>();

    @Mapping("contains")
    public int contains(String key) {
        return ary.contains(key) ? 1 : 0;
    }

    @Mapping("add")
    public void add(String key) {
        ary.add(key);
    }

    @Mapping("getAll")
    public List<String> getAll() {
        return ary;
    }
}
