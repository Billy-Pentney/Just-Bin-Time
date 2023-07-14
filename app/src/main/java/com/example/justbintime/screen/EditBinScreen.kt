package com.example.justbintime.screen

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.viewmodel.BinViewModel

enum class BinModifyMode { MODE_EDIT, MODE_ADD }

@Composable
fun EditBinScreen(viewModel: BinViewModel?, navHostController: NavHostController?, binOrig: DisplayableBin) {
    ModifyBinScreen(
        viewModel,
        navHostController,
        binOrig,
        BinModifyMode.MODE_EDIT
    )
}


@Preview
@Composable
fun PreviewEditBin() {
    JustBinTimeTheme(darkTheme = true) {
        val bin = BinFactory().makeLandfillBinWithColours()
        Surface {
            EditBinScreen(null, null, bin)
        }
    }
}
