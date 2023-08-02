package com.example.justbintime.viewmodel

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

//data class AppBarState(
//    val title: String = "",
//    val actions: (@Composable RowScope.() -> Unit)? = null
//)
//
//data class FloatingActionButtonState(
//    val icon: Icon? = null,
//    val action: (() -> Unit)? = null
//)

class MainScaffoldViewModel: ViewModel() {

    var topBarTitleState by mutableStateOf(null as (@Composable () -> Unit)?, referentialEqualityPolicy())
    var topBarActionState by mutableStateOf(null as (@Composable RowScope.() -> Unit)?, referentialEqualityPolicy())
    var fabState by mutableStateOf(null as (@Composable () -> Unit)?, referentialEqualityPolicy())

    var visibleTopBarBackButton by mutableStateOf(false)
}