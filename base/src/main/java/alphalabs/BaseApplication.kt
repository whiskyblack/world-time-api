package alphalabs

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import alphalabs.ads.TestAds
import alphalabs.ads.appopen.AdmobAppOpen
import alphalabs.base.BuildConfig
import alphalabs.tracking.Tracking

open class BaseApplication(val moduleList: MutableList<Module>) : Application() {
    var admobAppOpen: AdmobAppOpen? = null

    override fun onCreate() {
        super.onCreate()
        startKoin {
            fragmentFactory()
            androidContext(this@BaseApplication)
            modules(moduleList)
        }

        Tracking.init(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        TestAds.stateTestDevice(this, BuildConfig.DEBUG)
    }
}