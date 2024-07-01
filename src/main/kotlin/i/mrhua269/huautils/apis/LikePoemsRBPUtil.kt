package i.mrhua269.i.mrhua269.huautils.apis

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import i.mrhua269.i.mrhua269.huautils.HttpUtils
import kotlin.random.Random

data class LikePoemsRBPUtil(
    val code: Int,
    val url: String
){
    companion object{
        private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

        fun getNew(): LikePoemsRBPUtil?{
            val json = String(HttpUtils.request(randomAPI()).second)
            return gson.fromJson(json, LikePoemsRBPUtil::class.java)
        }

        private fun randomAPI(): String {
            return when (Random.nextInt(2)) {
                1 -> "https://api.likepoems.com/img/upyun/pixiv/?type=json"
                2 -> "https://api.likepoems.com/img/sina/pixiv/?type=json"
                0 -> "https://api.likepoems.com/img/upyun/pc/?type=json"
                else -> "https://api.likepoems.com/img/sina/pixiv/?type=json"
            }
        }
    }
}