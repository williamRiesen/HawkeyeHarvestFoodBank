package com.md.williamriesen.hawkeyeharvest

import android.app.Application
import com.md.williamriesen.hawkeyeharvest.orderonsite.OnSiteOrderActivity
import com.md.williamriesen.hawkeyeharvest.signin.FireStoreModule
import com.md.williamriesen.hawkeyeharvest.signin.SignInByAccountActivity
import com.md.williamriesen.hawkeyeharvest.signin.SignInByAccountFragment
import dagger.Component
import javax.inject.Singleton

const val openingHour24 = 8
const val openingMinute = 45
const val closingHour24 = 16
const val closingMinute = 30

@Singleton
@Component(modules = [FireStoreModule::class])
interface ApplicationComponent {
    fun inject(activity: SignInByAccountActivity)
    fun inject(activity: OnSiteOrderActivity)
    fun inject(fragment: SignInByAccountFragment)
}

class HawkeyeHarvestFoodBankApplication : Application() {
    val appComponent = DaggerApplicationComponent.create()
}
