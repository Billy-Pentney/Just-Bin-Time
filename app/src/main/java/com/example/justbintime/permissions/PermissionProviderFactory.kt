package com.example.justbintime.permissions

import android.Manifest

class PermissionProviderFactory {
    companion object {
        fun from(permission: String): PermissionTextProvider? {
            return when (permission) {
                Manifest.permission.POST_NOTIFICATIONS -> NotificationPermissionTextProvider()
                Manifest.permission.SCHEDULE_EXACT_ALARM -> AlarmPermissionTextProvider()
                else -> null
            }
        }
    }
}
