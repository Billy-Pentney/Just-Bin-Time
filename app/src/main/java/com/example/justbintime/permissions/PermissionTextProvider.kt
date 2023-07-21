package com.example.justbintime.permissions

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class NotificationPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined)
            "This app cannot send reminders without the Notification Permission. " +
                    "Reminders will not be shown until the permission is granted in the App Settings."
        else
            "Uh-oh. This app cannot send reminders without the Notification Permission." +
                    "Please grant the permission to use this feature."
    }
}

class AlarmPermissionTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined)
            "The Alarm permission is required to schedule your reminder notifications." +
                    "Reminders will not be shown until the permission is granted in the App Settings."
        else
            "Uh-oh. This app cannot send reminders without the Alarm Permission." +
                    "Please grant the permission to use this feature."
    }
}