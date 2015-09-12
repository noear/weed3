using System.Collections.Generic;

namespace Weed.OPS
{
    /// <summary>
    /// OPS-Xml序列化支持
    /// --------------------------
    /// 创建:谢月甲,20080429
    /// </summary>
    public interface IOpsSerializable
    {
        /// <summary>
        /// 绑定OPS-XML
        /// </summary>
        /// <param name="opsXml">符合OPS规范的XML文本</param>
        void Bind(string opsXml);

        /// <summary>
        /// 输出为OPS-XML
        /// </summary>
        string ToXml();

        /// <summary>
        /// 输出为OPS-JSON
        /// </summary>
        /// <returns></returns>
        string ToJson();

        IEnumerator<XItem> GetEnumerator();
    }
}
