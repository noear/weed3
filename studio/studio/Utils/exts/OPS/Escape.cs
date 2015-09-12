using System;
using System.Text;
using System.Text.RegularExpressions;

namespace Weed
{
	/// <summary>
	/// Escape (转换 @C#)
	/// 
	/// 谢月甲,2006-01-14
	/// </summary>
	public static class Escape
	{
		#region Javascript Escape

		#region 基础支持

		private static string[] asc = new string[]{ "00", "01", "02", "03", "04", "05",
													  "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10",
													  "11", "12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B",
													  "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26",
													  "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31",
													  "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C",
													  "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47",
													  "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52",
													  "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D",
													  "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68",
													  "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73",
													  "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E",
													  "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89",
													  "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94",
													  "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F",
													  "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA",
													  "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5",
													  "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0",
													  "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB",
													  "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6",
													  "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1",
													  "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC",
													  "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
													  "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

		private static byte[] val = new byte[]{	0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x00, 0x01,
												  0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F,
												  0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F, 0x3F };

		//是否为特别字符
		private static bool IsSignalment(int ch)
		{
			if (ch == '-' || ch == '_' || ch == '.' || ch == '*' || ch == '+' || ch == '@' || ch == '/')
				return true;
			else
				return false;
		}

		//是否为16进制字符
		private static bool IsHex(char ch)
		{
			if ('a' <= ch && ch <= 'z')
				return true;

			if ('A' <= ch && ch <= 'Z') 
				return true;

			if ('0' <= ch && ch <= '9') 
				return true;
			else
				return false;
		}

		#endregion

		#region Escape 编码

        public static string JsEscape(String s)
        {
            if (s == null)
                return "";

            int len = s.Length;
            StringBuilder sbuf = new StringBuilder(len * 2);

            for (int i = 0; i < len; i++)
            {
                int ch = s[i];

                if ('A' <= ch && ch <= 'Z')
                    sbuf.Append((char)ch);
                else if ('a' <= ch && ch <= 'z')
                    sbuf.Append((char)ch);
                else if ('0' <= ch && ch <= '9')
                    sbuf.Append((char)ch);
                else if (IsSignalment(ch))
                    sbuf.Append((char)ch);
                else if (ch <= 0x00FF)
                { // other ASCII : map to %XX
                    sbuf.Append('%');
                    sbuf.Append(asc[ch]);
                }
                else
                { // unicode : map to %uXXXX
                    sbuf.Append('%');
                    sbuf.Append('u');
                    sbuf.Append(asc[(ch >> 8)]);//原为:>>>
                    sbuf.Append(asc[(0x00FF & ch)]);
                }
            }
            return sbuf.ToString();
        }

		#endregion

		#region Unescape 解码

		public static string JsUnescape(String s) 
		{
            if (s == null)
                return "";

			int i = 0;
			int len = s.Length;
			StringBuilder sbuf = new StringBuilder(len/2);

			while (i < len) 
			{
				int ch = s[i];

				if ('A' <= ch && ch <= 'Z') 
					sbuf.Append((char) ch);
				else if ('a' <= ch && ch <= 'z') 
					sbuf.Append((char) ch);
				else if ('0' <= ch && ch <= '9') 
					sbuf.Append((char) ch);
				else if (IsSignalment(ch))
					sbuf.Append((char) ch);
				else if (ch == '%') 
				{
					int cint = 0;

					if ('u' == s[i + 1]) 
					{// %uXXXX : map to unicode(XXXX)
						cint = (cint << 4) | val[s[i + 2]];
						cint = (cint << 4) | val[s[i + 3]];
						cint = (cint << 4) | val[s[i + 4]];
						cint = (cint << 4) | val[s[i + 5]];
						i += 5;
					}
					else if(IsHex(s[i + 1]) && IsHex(s[i + 2]))
					{// %XX : map to ascii(XX)
						cint = (cint << 4) | val[s[i + 1]];
						cint = (cint << 4) | val[s[i + 2]];
						i += 2;
					}
					else //说明,虽然格式像%XX,但是XX不是16进制数据
						cint = ch;

					sbuf.Append((char) cint);
				} 
				else 
				{ // 对应的字符未经过编码
					sbuf.Append((char) ch);
				}
				i++;
			}

			return sbuf.ToString();
		}

		#endregion
		#endregion

		#region Xml Escape

        public static string Xml(string xmlText)
        {
            return XmlEscape(xmlText);
        }

		public static string XmlEscape(string xmlText)
		{
            if (xmlText == null)
				return "";
			else
			{
                StringBuilder sb = new StringBuilder(xmlText);
                sb.Replace("&", "&amp;");
                sb.Replace("<", "&lt;");
                sb.Replace(">", "&gt;");
                sb.Replace("'", "&#039;");
                sb.Replace("\"", "&quot;");

                return sb.ToString();
			}
		}

        public static string UnXml(string text)
        {
            return XmlUnEscape(text);
        }

		public static string XmlUnEscape(string text)
		{
			if (text == null)
				return "";
			else
			{
                StringBuilder sb = new StringBuilder(text);
                sb.Replace("&lt;", "<");
                sb.Replace("&gt;", ">");
                sb.Replace("&amp;", "&");
                sb.Replace("&#039;", "'");
                sb.Replace("&quot;", "\"");

                return sb.ToString(); ;
			}
		}

		#endregion

        #region S64 Escape

        public static string S64(string clearText)
        {
            return S64Escape(clearText);
        }

        public static string S64Escape(string clearText)
		{
			byte[] bytes = Encoding.UTF8.GetBytes(clearText);

			return System.Convert.ToBase64String(bytes);
		}

        public static string UnS64(string base64Text)
        {
            return S64Unsecape(base64Text);
        }

		public static string S64Unsecape(string base64Text)
		{
			byte[] bytes = System.Convert.FromBase64String(base64Text);

			return Encoding.UTF8.GetString(bytes);
		}

		#endregion		

        #region Base 62

        private static string Alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        public static String Base62Encoding(long num)
        {
            if (num < 1)
                return "0";

            StringBuilder sb = new StringBuilder();
            for (; num > 0; num /= 62)
                sb.Append(Alphabet[(int)(num % 62)]);

            return sb.ToString();
        }

        public static long Base62Decoding(String str)
        {
            str = str.Trim();
            if (str.Length < 1)
                throw new Exception("str must not be empty.");

            long result = 0;
            for (int i = 0; i < str.Length; i++)
                result += (long)(Alphabet.IndexOf(str[i]) * Math.Pow(62, i));

            return result;
        }

        #endregion

        #region Json Escape
        public static string Json(string text)
        {
            return JsonEscape(text);
        }

        public static string JsonEscape(string text)
        {
            if (text == null)
                return "";
            else
            {
                StringBuilder sb = new StringBuilder(text);
                
                sb.Replace("'", @"\'");
                sb.Replace("\"", "\\\"");
                sb.Replace("\b", @"\b");
                sb.Replace("\t", @"\t");
                sb.Replace("\n", @"\n");               
                sb.Replace("\f", @"\f");
                sb.Replace("\r", @"\r");

                return sb.ToString();
            }
        }
        public static string UnJson(string jsonText)
        {
            return JsonUnEscape(jsonText);
        }
        public static string JsonUnEscape(string jsonText)
        {
            if (jsonText == null)
                return "";
            else
            {
                StringBuilder sb = new StringBuilder(jsonText);

                sb.Replace(@"\'", "'");
                sb.Replace("\\\"", "\"");
                sb.Replace(@"\b", "\b");
                sb.Replace(@"\t", "\t");
                sb.Replace(@"\n", "\n");
                sb.Replace(@"\f", "\f");
                sb.Replace(@"\r", "\r");

                return sb.ToString();
            }
        }
        #endregion

        #region Quote Escape

        /// <summary>
        /// 将半角的引号转为全角
        /// </summary>
        /// <param name="text"></param>
        /// <returns></returns>
        //public static string QuoteEscape(string text)
        //{
        //    if (text == null)
        //        return "";
        //    else
        //        return text.Replace('\'', '＇').Replace('\"', '＂');
        //}

        /// <summary>
        /// 将全角的引号转为半角
        /// </summary>
        /// <param name="text"></param>
        /// <returns></returns>
        //public static string QuoteUnEscape(string text)
        //{
        //    if (text == null)
        //        return "";
        //    else
        //        return text.Replace('＇', '\'').Replace('＂', '\"');
        //}

        #endregion

        #region 单数 <-> 复数

        ///// <summary>
        ///// 单数转为复数
        ///// </summary>
        ///// <param name="word">单词</param>
        ///// <returns></returns>
        //public static string GetPlural(string singular)
        //{
        //    string plural;

        //    if (singular != null && singular.Length > 0)
        //    {
        //        char ch = singular[singular.Length - 1];
        //        StringBuilder pluralBuilder = new StringBuilder(singular);
        //        if (ch == 's' || ch == 'x')
        //            pluralBuilder.Append("es");
        //        else if (ch == 'y')
        //        {
        //            pluralBuilder.Remove(pluralBuilder.Length - 1, 1);
        //            pluralBuilder.Append("ies");
        //        }
        //        else
        //            pluralBuilder.Append("s");

        //        plural = pluralBuilder.ToString();
        //    }
        //    else
        //        plural = singular;

        //    return plural;
        //}

        /// <summary>
        /// 单词变成单数形式
        /// </summary>
        /// <param name="word"></param>
        /// <returns></returns>
        public static string ToSingular(string word)
        {
            Regex plural1 = new Regex("(?<keep>[^aeiou])ies$");
            Regex plural2 = new Regex("(?<keep>[aeiou]y)s$");
            Regex plural3 = new Regex("(?<keep>[sxzh])es$");
            Regex plural4 = new Regex("(?<keep>[^sxzhyu])s$");

            if (plural1.IsMatch(word))
                return plural1.Replace(word, "${keep}y");
            else if (plural2.IsMatch(word))
                return plural2.Replace(word, "${keep}");
            else if (plural3.IsMatch(word))
                return plural3.Replace(word, "${keep}");
            else if (plural4.IsMatch(word))
                return plural4.Replace(word, "${keep}");

            return word;
        }
        /// <summary>
        /// 单词变成复数形式
        /// </summary>
        /// <param name="word"></param>
        /// <returns></returns>
        public static string ToPlural(string word)
        {
            Regex plural1 = new Regex("(?<keep>[^aeiou])y$");
            Regex plural2 = new Regex("(?<keep>[aeiou]y)$");
            Regex plural3 = new Regex("(?<keep>[sxzh])$");
            Regex plural4 = new Regex("(?<keep>[^sxzhy])$");

            if (plural1.IsMatch(word))
                return plural1.Replace(word, "${keep}ies");
            else if (plural2.IsMatch(word))
                return plural2.Replace(word, "${keep}s");
            else if (plural3.IsMatch(word))
                return plural3.Replace(word, "${keep}es");
            else if (plural4.IsMatch(word))
                return plural4.Replace(word, "${keep}s");

            return word;
        }

        ///// <summary>
        ///// 复数转为单数
        ///// </summary>
        ///// <param name="plural"></param>
        ///// <returns></returns>
        //public static string GetSingular(string plural)
        //{           
        //    // convert to lowercase for easier comparison           
        //    string lower = plural.ToLower();
        //    string res = string.Empty;
        //    int len = lower.Length;

        //    // rule out a few exceptions
        //    if (lower == "feet")
        //        res = "Foot";
        //    else if (lower == "geese")
        //        res = "Goose";
        //    else if (lower == "men")
        //        res = "Man";
        //    else if (lower == "women")
        //        res = "Woman";
        //    else if (lower == "criteria")
        //        res = "Criterion";
        //    else if (lower == "alias")
        //        return plural;
        //    else if (lower.LastIndexOf("files") >= 0 && lower.LastIndexOf("files") == lower.Length - 5)
        //        return plural.Substring(0, lower.Length - 1);
        //    else if (lower.EndsWith("ies"))
        //    {
        //        if ("aeiou".IndexOf(lower.Substring(lower.Length - 4, 1)) < 0)
        //            res = plural.Substring(0, plural.Length - 3) + "y";
        //        else
        //            res = plural;
        //    }
        //    else if (lower.EndsWith("es"))
        //    {
        //        if (len > 2)
        //            res = plural.Substring(0, plural.Length - 2);
        //        else
        //            res = plural;
        //    }
        //    else if (lower.EndsWith("s"))
        //    {
        //        if (len > 1)
        //            res = plural.Substring(0, plural.Length - 1);
        //        else
        //            res = plural;
        //    }
        //    else
        //        res = plural;

        //    // the result must preserve the original word//s capitalization          
        //    if (plural == lower)
        //        return res.ToLower();                // it was an all-lowercase word 
        //    else if (plural == plural.ToUpper())
        //        return res.ToUpper();   // it was an all-uppercase word  
        //    else
        //        return res;    // return whatever is in "res" 
        //}

        #endregion

        public static T Enum<T>(string value)
        {
            return (T)System.Enum.Parse(typeof(T), value, true);
        }

        public static T Enum<T>(string value, T defValue)
        {
            if (System.Enum.IsDefined(typeof(T), value))
                return (T)System.Enum.Parse(typeof(T), value, true);
            else
                return defValue;
        }
    }
}
