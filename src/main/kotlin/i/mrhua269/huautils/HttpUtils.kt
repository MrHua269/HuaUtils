package i.mrhua269.i.mrhua269.huautils

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

object HttpUtils {
    fun request(url1: String): Pair<Int, ByteArray> {
        val url = URL(url1)
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:127.0) Gecko/20100101 Firefox/127.0")
        connection.readTimeout = 30000
        connection.connectTimeout = 3000
        return Pair(connection.responseCode, FileUtils.readInputStreamToByte(connection.inputStream))
    }
}