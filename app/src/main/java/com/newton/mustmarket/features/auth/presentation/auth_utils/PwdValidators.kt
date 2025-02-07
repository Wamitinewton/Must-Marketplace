package com.newton.mustmarket.features.auth.presentation.auth_utils

import com.newton.mustmarket.features.auth.presentation.forgotPassword.enums.PasswordStrength

object PwdValidators {
    const val OTP_LENGTH = 6
    val SCORE_THRESHOLDS = mapOf(
        (0..1) to PasswordStrength.WEAK,
        (2..3) to PasswordStrength.MEDIUM,
        (4..5) to PasswordStrength.STRONG,
        (6..6) to PasswordStrength.VERY_STRONG
    )
}