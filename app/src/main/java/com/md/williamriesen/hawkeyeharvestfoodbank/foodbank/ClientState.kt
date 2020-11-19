package com.md.williamriesen.hawkeyeharvestfoodbank.foodbank

enum class ClientState {
    ELIGIBLE_TO_ORDER,
    PLACED_ON_SITE,
    PLACED_YESTERDAY_PENDING,
    PLACED_YESTERDAY_PACKED,
    PLACED_TODAY_FOR_TOMORROW,
    NO_SHOWED,
    PICKED_UP,
    ERROR_STATE
}