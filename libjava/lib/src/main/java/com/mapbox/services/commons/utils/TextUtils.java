package com.mapbox.services.commons.utils;

/**
 * Some code from https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/text/TextUtils.java
 *
 * We avoid including a full library like org.apache.commons:commons-lang3 to avoid
 * an unnecessary large number of methods, which is inconvenient to Android devs.
 */
public class TextUtils {

    /**
     * Returns true if the string is null or 0-length.
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    /**
     * Returns a string containing the tokens joined by delimiters.
     * @param tokens an array objects to be joined. Strings will be formed from
     *     the objects by calling object.toString().
     */
    public static String join(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token: tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

}
