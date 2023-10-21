package alphalabs.ads.appopen

interface SplashAppOpenListener {
    fun onAppOpenDismiss() {}
    fun onAppOpenShowed() {}
    fun onAppOpenFailedToShow() {}
    fun onAppOpenFailToLoad(error: String) {}
}