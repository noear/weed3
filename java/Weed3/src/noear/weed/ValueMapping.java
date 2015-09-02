package noear.weed;

/**
 * Created by noear on 14-7-21.
 * 值与weedCode的映射
 */
public class ValueMapping {
    public ValueMapping(String value, String weedCode)
    {
        this.value    = value;
        this.weedCode = weedCode;
    }

    public String value;

    /// <summary>
    /// 是否已缓存
    /// </summary>
    public Boolean isCached;

    /// <summary>
    /// 缓存的KEY
    /// </summary>
    public String weedCode;
}
