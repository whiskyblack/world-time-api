package alphalabs.ads.interstitial

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError

interface AdmobInterstitialListener {
    fun onAdLoaded() {}
    fun onAdFailToLoad(error: LoadAdError) {}
    fun onAdFailToShow(error: AdError) {}
    fun onAdShowedFullScreen() {}
    fun onAdDismissedFullScreen() {}
}