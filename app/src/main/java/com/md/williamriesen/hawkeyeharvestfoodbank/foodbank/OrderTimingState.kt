package com.md.williamriesen.hawkeyeharvestfoodbank.foodbank

enum class OrderTimingState {
    PLACED_ON_SITE_PENDING,
    PLACED_ON_SITE_PACKED,
    PLACED_YESTERDAY_PENDING,
    PLACED_YESTERDAY_PACKED,
    PLACED_TODAY_FOR_TOMORROW,
    ERROR_STATE
}