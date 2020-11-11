package com.me.optionbarview

/**
 * dysen.
 * dy.sen@qq.com    11/3/20  2:54 PM

 * Info：
 */
object OptionStringUtils {
    /**
     * 检查内容过长用省略号代替
     */
    fun checkStr(text: String, len: Int): String {
        return if (text.length > len) "${text.substring(0, len)}..." else text
    }
}