package com.example.justbintime.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.util.Queue

enum class PermissionStatus {
    NOT_REQUESTED, ACCEPTED, DENIED_ONCE, DENIED_TWICE
}

class AppViewModel: ViewModel() {

    val visiblePermissionDialogQueue = mutableStateListOf<String>()
    private val permissionStatus = mutableMapOf<String, PermissionStatus>()

    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onPermissionResult(permission: String, isGranted: Boolean) {
        if (isGranted) {
            permissionStatus[permission] = PermissionStatus.ACCEPTED
            return
        }

        visiblePermissionDialogQueue.add(permission)
        if (permissionStatus[permission] == PermissionStatus.NOT_REQUESTED) {
            permissionStatus[permission] = PermissionStatus.DENIED_ONCE
        }
        else {
            permissionStatus[permission] = PermissionStatus.DENIED_TWICE
        }
    }

    // Indicates if the given permission should be requested again (i.e. if it hasn't been granted
    // but hasn't been denied twice)
    fun shouldRetry(permission: String): Boolean {
        return permissionStatus[permission] == PermissionStatus.NOT_REQUESTED
                || permissionStatus[permission] == PermissionStatus.DENIED_ONCE
    }
}