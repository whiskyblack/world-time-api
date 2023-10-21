package alphalabs.ads.appopen;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

public class AdmobAppOpen implements Application.ActivityLifecycleCallbacks, LifecycleObserver, DefaultLifecycleObserver {
    private static final String TEST_ADS_ID = "ca-app-pub-3940256099942544/3419835294";
    public static final int ADS_READY_TO_LOAD = 0;
    public static final int ADS_LOADING = 1;
    public static final int ADS_LOADED = 2;
    public static final int ADS_SHOWING = 3;

    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private Application application;
    private Activity currentActivity;
    private AppOpenAdsListener appOpenAdsListener;
    private int adsStatus = 0;
    private long loadTime = 0;
    public String admobId = TEST_ADS_ID;

    public AdmobAppOpen(Application application) {
        application.registerActivityLifecycleCallbacks(this);
        this.application = application;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void setAppOpenAdsListener(AppOpenAdsListener appOpenAdsListener) {
        this.appOpenAdsListener = appOpenAdsListener;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public int getAdsStatus() {
        return adsStatus;
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        if (appOpenAdsListener != null) appOpenAdsListener.onAppStart();
    }

    public void showAdIfAvailable() {
        if (isAdAvailable()) {
            FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    if (appOpenAdsListener != null) appOpenAdsListener.onAppOpenShowed();
                    adsStatus = ADS_SHOWING;
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    if (appOpenAdsListener != null) appOpenAdsListener.onAppOpenDismiss();
                    adsStatus = ADS_READY_TO_LOAD;
                    appOpenAd = null;
                    loadAds();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    if (appOpenAdsListener != null) appOpenAdsListener.onAppOpenFailedToShow();
                    adsStatus = ADS_READY_TO_LOAD;
                }
            };
            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            if (currentActivity == null) {
                if (appOpenAdsListener != null) appOpenAdsListener.onAppOpenFailedToShow();
            } else {
                try {
                    appOpenAd.show(currentActivity);
                } catch (Exception e) {
                    if (appOpenAdsListener != null) appOpenAdsListener.onAppOpenFailedToShow();
                }
            }
        } else {
            if (appOpenAdsListener != null) appOpenAdsListener.onAppOpenFailedToShow();
        }
    }

    public void loadAds() {
        if (isAdAvailable() || adsStatus == ADS_LOADING) {
            return;
        }

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                super.onAdLoaded(appOpenAd);
                AdmobAppOpen.this.appOpenAd = appOpenAd;
                AdmobAppOpen.this.loadTime = System.currentTimeMillis();
                adsStatus = ADS_LOADED;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adsStatus = ADS_READY_TO_LOAD;
            }
        };

        AdRequest request = getAdRequest();
        AppOpenAd.load(
                application, admobId, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
        adsStatus = ADS_LOADING;
    }

    public AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = System.currentTimeMillis() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
            currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
            currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
            currentActivity = null;
    }
}
