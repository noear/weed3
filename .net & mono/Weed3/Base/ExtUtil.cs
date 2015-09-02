using System;
using System.Text;

namespace Noear.Weed {
    internal static class ExtUtil {

        public static StringBuilder Replace(this StringBuilder input, int start, int end, string newValue) {
            string oldValue = input.Substring(start, end);
            return input.Replace(oldValue, newValue, start, end - start);
        }

        public static StringBuilder ReplaceFirst(this StringBuilder input, String oldValue, String newValue) {
            int idx = input.ToString().IndexOf(oldValue);
            return input.Replace(oldValue, newValue, idx, oldValue.Length);
        }

        public static int IndexOf(this StringBuilder input, char c, int fromIndex) {
            for (int i = fromIndex, len = input.Length; i < len; i++) {
                if (input[i] == c)
                    return i;
            }

            return -1;
        }

        public static void DeleteCharAt(this StringBuilder input, int chatIndex) {
            input.Remove(chatIndex, 1);
        }

        public static string Substring(this StringBuilder input, int startIndex, int endIndex) {
            return input.ToString(startIndex,endIndex-startIndex);
        }
    }
}
