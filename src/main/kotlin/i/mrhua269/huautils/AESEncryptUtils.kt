package i.mrhua269.i.mrhua269.huautils

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object AESEncryptUtils {
    fun encryptAES(keyData: ByteArray, encryptedData: ByteArray): ByteArray {
        val secretKeySpec = SecretKeySpec(keyData, "AES")
        val ivParameterSpec = IvParameterSpec(keyData)
        return encryptAES(secretKeySpec, ivParameterSpec, encryptedData).toByteArray()
    }

    fun encryptAES(key: SecretKey, ivKey: AlgorithmParameterSpec, data: ByteArray): ByteArrayOutputStream {
        val inputBuffer = ByteArrayInputStream(data)
        val outputBuffer = ByteArrayOutputStream()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key, ivKey)
        val buffer = ByteArray(64)
        var bytesRead: Int
        while (inputBuffer.read(buffer).also { bytesRead = it } != -1) {
            val encryptedData = cipher.update(buffer, 0, bytesRead)
            if (encryptedData != null) {
                outputBuffer.write(encryptedData)
            }
        }
        val finalData = cipher.doFinal()
        if (finalData != null) {
            outputBuffer.write(finalData)
        }
        return outputBuffer
    }

    fun decryptAES(key: SecretKey, iv: AlgorithmParameterSpec, data: ByteArray): ByteArrayOutputStream {
        val inputBuffer = ByteArrayInputStream(data)
        val outputBuffer = ByteArrayOutputStream()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val buffer = ByteArray(64)
        var bytesRead: Int
        while (inputBuffer.read(buffer).also { bytesRead = it } != -1) {
            val decryptedData = cipher.update(buffer, 0, bytesRead)
            if (decryptedData != null) {
                outputBuffer.write(decryptedData)
            }
        }
        val finalData = cipher.doFinal()
        if (finalData != null) {
            outputBuffer.write(finalData)
        }
        return outputBuffer
    }

    fun randomAESKey(): SecretKey {
        val keyGenerator: KeyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        return keyGenerator.generateKey()
    }

    fun randomAESKey(keyData: ByteArray?): SecretKey {
        return SecretKeySpec(keyData, "AES")
    }

    fun randomIV(): IvParameterSpec {
        val ivBytes = ByteArray(16)
        SecureRandom().nextBytes(ivBytes)
        return IvParameterSpec(ivBytes)
    }
}