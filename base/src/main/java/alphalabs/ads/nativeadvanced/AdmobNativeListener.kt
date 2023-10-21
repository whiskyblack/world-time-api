package alphalabs.ads.nativeadvanced

import com.google.android.gms.ads.nativead.NativeAd

interface AdmobNativeListener {
    fun onAdLoaded(nativeAd: NativeAd?) {}

    fun onAdLoadFailed(message: String?) {}
}