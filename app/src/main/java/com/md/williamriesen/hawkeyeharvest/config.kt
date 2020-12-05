package com.md.williamriesen.hawkeyeharvest

import android.app.Application
import dagger.Component

const val openingHour24 = 8
const val openingMinute = 45
const val closingHour24 = 16
const val closingMinute = 30

@Component
interface ApplicationComponent

class HawkeyeHarvestFoodBankApplication : Application() {
    val appComponent = DaggerApplicationComponent.create()
}
