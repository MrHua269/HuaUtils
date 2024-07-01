package i.mrhua269.i.mrhua269.huautils.apis

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import i.mrhua269.i.mrhua269.huautils.HttpUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


data class LoliconPixivAPI (
    val error: String,
    val data: Array<Data>
){
    companion object{
        private val API_URL: String = "https://api.lolicon.app/setu/v2"
        private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

        fun requestNew(
            r18: Boolean = false,
            tags: Array<String> = emptyArray(),
            num: Int = 1,
            proxy: String = "i.pixiv.re",
            excludeAI: Boolean = false
        ): LoliconPixivAPI{
            var finallyUrl = API_URL
                .plus("?").plus(if (r18) "r18=1" else "r18=0")
                .plus("&").plus("num=$num")
                .plus("&").plus("proxy=$proxy")
                .plus("&").plus("excludeAI=$excludeAI")

            for (tag in tags){
                finallyUrl = finallyUrl.plus("&").plus("tag=${URLEncoder.encode(tag, StandardCharsets.UTF_8)}")
            }

            val responseContent = HttpUtils.request(finallyUrl)

            return this.gson.fromJson(String(responseContent.second), LoliconPixivAPI::class.java)
        }
    }

    data class Data(
        val pid: Int,
        val p: Int,
        val uid: Int,
        val title: String,
        val author: String,
        val r18: Boolean,
        val width: Int,
        val height: Int,
        val tags: Array<String>,
        val ext: String,
        val updateDate: Long,
        val urls: URLs,
    )

    data class URLs(
        val original: String
    )
}