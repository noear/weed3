using System;

namespace Noear.Weed {
    internal class ValueMapping {
        internal int _hash;
        public ValueMapping(String value, String weedCode) {
            this.value = value;
            this.weedCode = weedCode;

            if (value != null)
                _hash = value.GetHashCode();
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
}
