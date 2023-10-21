package alphalabs.ads

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class TestAds {

    companion object {
        @JvmStatic
        fun stateTestDevice(context: Context, state: Boolean) {
            if (state) {
                val testId = TestAds()
                val element = testId.getDeviceTest(context)
                val testDeviceIds = listOf(element)
                val configuration =
                    RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
                MobileAds.setRequestConfiguration(configuration)
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceTest(context: Context): String {
        return try {
            md5(
                        Settings.Secure.getString(
                            context.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    ).uppercase(Locale.getDefault())
        } catch (e: Exception) {
            ""
        }
    }

    private fun md5(s: String): String {
        try {
            val digest: MessageDigest = MessageDigest
                .getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest: ByteArray = digest.digest()
            val hexString = StringBuffer()
            for (i in messageDigest.indices) {
                var h = Integer.toHexString(0xFF and messageDigest[i].toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
        }
        return ""
    }

}