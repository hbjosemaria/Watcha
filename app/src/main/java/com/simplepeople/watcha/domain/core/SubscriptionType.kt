package com.simplepeople.watcha.domain.core

import com.simplepeople.watcha.R

enum class SubscriptionType(name: Int) {
    FREE(R.string.subscription_free),
    BASIC(R.string.subscription_basic),
    STANDARD(R.string.subscription_standard),
    PREMIUM(R.string.subscription_premium)
}