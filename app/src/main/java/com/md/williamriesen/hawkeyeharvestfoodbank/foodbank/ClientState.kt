package com.md.williamriesen.hawkeyeharvestfoodbank.foodbank

import android.app.Activity
import com.md.williamriesen.hawkeyeharvestfoodbank.ErrorMessageActivity
import com.md.williamriesen.hawkeyeharvestfoodbank.orderfornextday.NextDayOrderActivity
import com.md.williamriesen.hawkeyeharvestfoodbank.orderonsite.OnSiteOrderActivity
import com.md.williamriesen.hawkeyeharvestfoodbank.signin.*
import kotlin.reflect.KClass

enum class ClientState {
    ELIGIBLE_TO_ORDER,
    PLACED_ON_SITE,
    PLACED_YESTERDAY_PENDING,
    PLACED_YESTERDAY_PACKED,
    PLACED_TODAY_FOR_TOMORROW,
    NO_SHOWED,
    PICKED_UP,
    ERROR_STATE;

    fun getNextActivity(clientIsOnSite: Boolean): Class<*> {
        return when(this) {
            ELIGIBLE_TO_ORDER -> {
                if (clientIsOnSite) {
                    OnSiteOrderActivity::class.java
                } else {
                    NextDayOrderActivity::class.java
                }
            }
            PLACED_ON_SITE -> InProgressOnSiteInstructionsActivity::class.java
            PLACED_YESTERDAY_PENDING -> PickUpLaterTodayInstructionsActivity::class.java
            PLACED_YESTERDAY_PACKED -> PickUpNowInstructionsActivity::class.java
            PLACED_TODAY_FOR_TOMORROW -> PickUpTomorrowInstructionsActivity::class.java
            NO_SHOWED -> NoShowMessageActivity::class.java
            PICKED_UP -> AlreadyServedMessageActivity::class.java
            ERROR_STATE -> ErrorMessageActivity::class.java
        }
    }
}

