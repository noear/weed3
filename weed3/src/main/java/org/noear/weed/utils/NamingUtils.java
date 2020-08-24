package org.noear.weed.utils;

import java.util.regex.Pattern;

public class NamingUtils {
    private static Pattern LOWER_CASE = Pattern.compile("[a-z]");

    /**
     * 如果全是大写字母, 则转换为小写字母
     *
     * @param name 待转换的名称
     * @return 转换后的名称
     */
    public static String toLowerCaseIfAllUpperCase(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (LOWER_CASE.matcher(name).find()) {
            return name; // 有小写字母, 不处理, 直接返回
        } else {
            return name.toLowerCase(); // 整个字符串都没有小写字母则转换为小写字母
        }
    }

    /**
     * 转换为驼峰命名法格式<br>
     * 如: user_name = userName, iuser_service = iuserService, i_user_service = iUserService
     *
     * @author zhaohuihua
     * @param name 待转换的名称
     * @return 驼峰命名法名称
     */
    public static String toCamelString(String name) {
        return toCamelString(name, false);
    }

    /**
     * 转换为驼峰命名法格式<br>
     * 如startsWithUpperCase=true时:<br>
     * user_name = UserName, iuser_service = IuserService, i_user_service = IUserService
     *
     * @author zhaohuihua
     * @param name 待转换的名称
     * @param startsWithUpperCase 是否以大写字母开头
     * @return 驼峰命名法名称
     */
    public static String toCamelString(String name, boolean startsWithUpperCase) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char[] chars = toLowerCaseIfAllUpperCase(name.trim()).toCharArray();

        StringBuilder buffer = new StringBuilder();
        boolean underline = startsWithUpperCase;
        for (char c : chars) {
            if (c == '_') {
                underline = true;
            } else {
                if (underline) {
                    buffer.append(Character.toUpperCase(c));
                } else {
                    buffer.append(c);
                }
                underline = false;
            }
        }
        return buffer.toString();
    }

    /**
     * 转换为下划线命名法格式<br>
     * 如: userName = user_name, SiteURL = site_url, IUserService = iuser_service<br>
     * user$Name = user$name, user_Name = user_name, user name = user_name, md5String = md5_string
     *
     * @author zhaohuihua
     * @param name 待转换的名称
     * @return 下划线命名法名称
     */
    public static String toUnderlineString(String name) {
        return toSeparatorString(name, '_');
    }

    /**
     * 转换为空格拆分格式<br>
     * 如: userName = user name, SiteURL = site url, IUserService = iuser service<br>
     * user$Name = user$name, user Name = user name, user name = user name, md5String = md5 string
     *
     * @author zhaohuihua
     * @param name 待转换的名称
     * @return 空格拆分的字符串
     */
    public static String toSpaceSplitString(String name) {
        return toSeparatorString(name, ' ');
    }

    private static String toSeparatorString(String name, char separator) {
        if (name == null || name.length() == 0) {
            return name;
        }
        char[] chars = name.trim().toCharArray();

        boolean lastLowerCase = false;
        StringBuilder buffer = new StringBuilder();
        for (char c : chars) {
            if (Character.isWhitespace(c)) {
                if (lastLowerCase) {
                    buffer.append(separator);
                }
                lastLowerCase = false;
            } else if (Character.isUpperCase(c)) {
                if (lastLowerCase) {
                    buffer.append(separator);
                }
                buffer.append(Character.toLowerCase(c));
                lastLowerCase = false;
            } else {
                buffer.append(c);
                lastLowerCase = Character.isLowerCase(c) || Character.isDigit(c);
            }
        }
        return buffer.toString();
    }

    /**
     * <p>
     * Capitalizes a String changing the first letter to title case as per {@link Character#toTitleCase(char)}. No other
     * letters are changed.
     * </p>
     *
     *
     *
     * <pre>
     * NamingUtils.capitalize(null)  = null
     * NamingUtils.capitalize("")    = ""
     * NamingUtils.capitalize("cat") = "Cat"
     * NamingUtils.capitalize("cAt") = "CAt"
     * </pre>
     *
     * @param str the String to capitalize, may be null
     * @return the capitalized String, {@code null} if null String input
     * @since 2.0
     */
    public static String capitalize(final String str) {
        int len;
        if (str == null || (len = str.length()) == 0) {
            return str;
        }

        char firstChar = str.charAt(0);
        if (Character.isTitleCase(firstChar)) {
            // already capitalized
            return str;
        }

        return new StringBuilder(len).append(Character.toTitleCase(firstChar)).append(str.substring(1)).toString();
    }
}
