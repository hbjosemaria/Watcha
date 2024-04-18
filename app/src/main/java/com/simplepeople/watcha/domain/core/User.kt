package com.simplepeople.watcha.domain.core

data class User(
    val username: String,
    var alias: String,
    var mail: String,
    var subscription: SubscriptionType
)