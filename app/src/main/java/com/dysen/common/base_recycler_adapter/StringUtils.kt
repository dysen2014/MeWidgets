package com.dysen.baselib.common.base_recycler_adapter

import android.text.TextUtils

/**
 * @email dy.sen@qq.com
 * created by dysen on 2018/9/19 - 上午10:38
 * @info
 */
object StringUtils {

    const val defaultContent = "--"
    /**
     * 获取非空的text，null或者empty时候可以设置默认值
     * 对content, defaultContent都进行判空操作
     */
    /**
     * 获取非空的text
     */
    @JvmStatic
    @JvmOverloads
    fun obtainNoNullText(content: String?, defaultContent: String = ""): String? {
        return if (!TextUtils.isEmpty(content)) content else if(content == null) "--"  else if (!TextUtils.isEmpty(defaultContent)) defaultContent else ""
    }

    fun rePlaceStr(content: String): String {
        return content.replace("00:00:00", "")
    }
}