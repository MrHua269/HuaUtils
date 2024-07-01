package i.mrhua269.i.mrhua269.huautils.apis

import i.mrhua269.i.mrhua269.huautils.HttpUtils
import java.net.URLDecoder

object YingtallPicUtil {
    fun getNewPicLinkList(page: Int): List<String> {
        val json = String(HttpUtils.request("https://yingtall.com/wp-json/wp/v2/posts?page=$page").second)
        val split = json.split("src=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val done: MutableList<String> = ArrayList()

        for ((counter, singlePart) in split.withIndex()) {
            if (counter > 0) {
                val removeHead = singlePart.substring(2)
                val retainArg = removeHead.substring(0, removeHead.indexOf("\\\""))
                done.add(URLDecoder.decode(retainArg, "UTF-8").replace("\\/\\/", "//").replace("\\/", "/"))
            }
        }

        return done
    }
}