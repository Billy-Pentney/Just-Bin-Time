package com.example.justbintime.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PermissionResultDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDenied: Boolean,
    onDismiss: () -> Unit,
    onOkay: () -> Unit,
    onGotoAppSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        buttons = {
            var onClick = onGotoAppSettings
            var text = "Go To App Settings"
            if (!isPermanentlyDenied) {
                onClick = onOkay
                text = "Okay"
            }
            Row (horizontalArrangement = Arrangement.Center) {
                Button(onClick = onClick) {
                    Text(text)
                }
            }
        },
        title = {Text("Permission Denied")},
        text = {Text(permissionTextProvider.getDescription(isPermanentlyDenied))},
        modifier = modifier
    )
}