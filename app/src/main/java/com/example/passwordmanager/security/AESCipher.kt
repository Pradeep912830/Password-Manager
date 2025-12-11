package com.example.passwordmanager.security

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

object AESCipher {

    private const val SECRET = "my_super_secret_key_for_passwords"
    private const val SALT = "my_salt_123"
    private const val ITERATION_COUNT = 65536
    private const val KEY_LENGTH = 256

    // Generate SecretKey from SECRET + SALT
    private fun generateKey(): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(SECRET.toCharArray(), SALT.toByteArray(), ITERATION_COUNT, KEY_LENGTH)
        val secret = factory.generateSecret(spec)
        return SecretKeySpec(secret.encoded, "AES")
    }

    // Encrypt
    fun encrypt(plainText: String): String {
        return try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val key = generateKey()

            // Random IV (16 bytes)
            val iv = ByteArray(16)
            Random.nextBytes(iv)

            cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
            val encrypted = cipher.doFinal(plainText.toByteArray())

            // Store IV + ciphertext together
            Base64.encodeToString(iv + encrypted, Base64.DEFAULT)

        } catch (e: Exception) {
            e.printStackTrace()
            plainText
        }
    }

    // Decrypt
    fun decrypt(cipherText: String): String {
        return try {
            val decoded = Base64.decode(cipherText, Base64.DEFAULT)

            val iv = decoded.copyOfRange(0, 16)
            val encrypted = decoded.copyOfRange(16, decoded.size)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val key = generateKey()

            cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
            val originalBytes = cipher.doFinal(encrypted)

            String(originalBytes)

        } catch (e: Exception) {
            e.printStackTrace()
            "Decryption Failed"
        }
    }
}
