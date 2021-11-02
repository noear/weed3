package weed3mongo.model;

/**
 * @author noear 2021/11/1 created
 */
public class TagCountsM {
    private String tag;
    private long counts;

    public String getTag() {
        return tag;
    }

    public long getCounts() {
        return counts;
    }

    @Override
    public String toString() {
        return "TagCountsM{" +
                "tag='" + tag + '\'' +
                ", counts=" + counts +
                '}';
    }
}