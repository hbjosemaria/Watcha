package com.simplepeople.watcha.ui.navigation.topbar.common

import androidx.compose.ui.unit.Dp

/**
* This function calculates the value of a dynamic parameter considering its minimum and maximum value, and a fraction.
 * It takes only the following data types: Int, Long, Float and Dp.
 * @param minValue The minimum value the parameter can reach.
 * @param maxValue The maximum value the parameter can reach.
 * @param fraction The fraction extracted from the TopBar ScrollBehavior. You can use, for example, the overlappedFraction or collapsedFraction value.
 * @return The value returned is calculated as a percentage of the sent parameters.
 * @throws IllegalStateException When this function is called with non supported classes, it throws an IllegalStateException.
* */

inline fun <reified T> TopBarDynamicParamCalc(
    minValue : T,
    maxValue : T,
    fraction : Float = 0f
) : T {
    return when (T::class) {
        Float::class ->  {
            val _minValue = minValue as Float
            val _maxValue = maxValue as Float
            val availableValueAmount = _maxValue - _minValue
            minValue + (availableValueAmount * (1-fraction))
        }
        Dp::class ->  {
            val _minValue = minValue as Dp
            val _maxValue = maxValue as Dp
            val availableValueAmount = _maxValue - _minValue
            minValue + (availableValueAmount * (1-fraction))
        }
        Int::class ->  {
            val _minValue = minValue as Dp
            val _maxValue = maxValue as Dp
            val availableValueAmount = _maxValue - _minValue
            minValue + (availableValueAmount * (1-fraction))
        }
        Long::class ->  {
            val _minValue = minValue as Dp
            val _maxValue = maxValue as Dp
            val availableValueAmount = _maxValue - _minValue
            minValue + (availableValueAmount * (1-fraction))
        }
        else -> throw IllegalStateException (
            "An Int, Long, Float or a Dp was expected."
        )
    } as T
}