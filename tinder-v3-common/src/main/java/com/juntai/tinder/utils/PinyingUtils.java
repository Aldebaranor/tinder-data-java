package com.juntai.tinder.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description: $
 * @Author: nemo
 * @Date: 2023/6/7 15:08
 */
public class PinyingUtils {


    public static String[] stringToPinyin(String src) {
        return stringToPinyin(src, false, (String) null);
    }

    public static String[] stringToPinyin(String src, String separator) {
        return stringToPinyin(src, true, separator);
    }

    public static String[] stringToPinyin(String src, boolean isPolyphone, String separator) {
        if (!"".equals(src) && null != src) {
            char[] srcChar = src.toCharArray();
            int srcCount = srcChar.length;
            String[] srcStr = new String[srcCount];

            for (int i = 0; i < srcCount; ++i) {
                srcStr[i] = charToPinyin(srcChar[i], isPolyphone, separator);
            }

            return srcStr;
        } else {
            return null;
        }
    }

    public static String charToPinyin(char src, boolean isPolyphone, String separator) {
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        StringBuffer tempPinying = new StringBuffer();
        if (src > 128) {
            try {
                String[] strs = PinyinHelper.toHanyuPinyinStringArray(src, defaultFormat);
                if (isPolyphone && null != separator) {
                    for (int i = 0; i < strs.length; ++i) {
                        tempPinying.append(strs[i]);
                        if (strs.length != i + 1) {
                            tempPinying.append(separator);
                        }
                    }
                } else {
                    tempPinying.append(strs[0]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination var7) {
                var7.printStackTrace();
            }
        } else {
            tempPinying.append(src);
        }

        return tempPinying.toString();
    }

    public static String hanziToPinyin(String hanzi) {
        return StringUtils.isBlank(hanzi) ? "" : hanziToPinyin(hanzi, "");
    }

    public static String hanziToPinyinHead(String hanzi) {
        return StringUtils.isBlank(hanzi) ? "" : stringArrayToString(getHeadByString(hanzi));
    }

    public static String hanziToPinyin(String hanzi, String separator) {
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        String pinyingStr = "";

        try {
            pinyingStr = PinyinHelper.toHanYuPinyinString(hanzi, defaultFormat, separator, true);
        } catch (BadHanyuPinyinOutputFormatCombination var5) {
            var5.printStackTrace();
        }

        return pinyingStr;
    }

    public static String stringArrayToString(String[] str, String separator) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length; ++i) {
            sb.append(str[i]);
            if (str.length != i + 1) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    public static String stringArrayToString(String[] str) {
        return stringArrayToString(str, "");
    }

    public static String charArrayToString(char[] ch, String separator) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < ch.length; ++i) {
            sb.append(ch[i]);
            if (ch.length != i + 1) {
                sb.append(separator);
            }
        }

        return sb.toString();
    }

    public static String charArrayToString(char[] ch) {
        return charArrayToString(ch, " ");
    }

    public static char[] getHeadByChar(char src, boolean isCapital) {
        if (src <= 128) {
            return new char[]{src};
        } else {
            String[] pinyingStr = PinyinHelper.toHanyuPinyinStringArray(src);
            if (pinyingStr == null) {
                pinyingStr = new String[0];
            }

            int polyphoneSize = pinyingStr.length;
            char[] headChars = new char[polyphoneSize];
            int i = 0;
            String[] var6 = pinyingStr;
            int var7 = pinyingStr.length;

            for (int var8 = 0; var8 < var7; ++var8) {
                String s = var6[var8];
                char headChar = s.charAt(0);
                if (isCapital) {
                    headChars[i] = Character.toUpperCase(headChar);
                } else {
                    headChars[i] = headChar;
                }

                ++i;
            }

            return headChars;
        }
    }

    public static char[] getHeadByChar(char src) {
        return getHeadByChar(src, false);
    }

    public static String[] getHeadByString(String src) {
        return getHeadByString(src, false);
    }

    public static String[] getHeadByString(String src, boolean isCapital) {
        return getHeadByString(src, isCapital, (String) null);
    }

    public static String[] getHeadByString(String src, boolean isCapital, String separator) {
        char[] chars = src.toCharArray();
        String[] headString = new String[chars.length];
        int i = 0;
        char[] var6 = chars;
        int var7 = chars.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            char ch = var6[var8];
            char[] chs = getHeadByChar(ch, isCapital);
            StringBuffer sb = new StringBuffer();
            if (null != separator) {
                int j = 1;
                char[] var13 = chs;
                int var14 = chs.length;

                for (int var15 = 0; var15 < var14; ++var15) {
                    char ch1 = var13[var15];
                    sb.append(ch1);
                    if (j != chs.length) {
                        sb.append(separator);
                    }

                    ++j;
                }
            } else if (chs.length > 0) {
                sb.append(chs[0]);
            }

            headString[i] = sb.toString();
            ++i;
        }

        return headString;
    }

}
