package i.mrhua269.i.mrhua269.huautils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

object FileUtils {

    fun fileNameWithoutExtension(input: String): String{
        val dotIndex: Int = input.lastIndexOf(".")
        return if (dotIndex == -1) input else input.substring(0, dotIndex)
    }

    fun readInputStream(inputStream: InputStream): ByteArrayInputStream {
        val buffer = ByteArray('Ѐ'.code)
        var len: Int
        val bos = ByteArrayOutputStream()
        while (inputStream.read(buffer).also { len = it } != -1) {
            bos.write(buffer, 0, len)
        }
        bos.close()
        return ByteArrayInputStream(bos.toByteArray())
    }

    fun readInputStreamToByte(inputStream: InputStream): ByteArray {
        val buffer = ByteArray('Ѐ'.code)
        var len: Int
        val bos = ByteArrayOutputStream()
        while (inputStream.read(buffer).also { len = it } != -1) {
            bos.write(buffer, 0, len)
        }
        bos.close()
        return bos.toByteArray()
    }

}