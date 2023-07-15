package com.example.justbintime.screen

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.viewmodel.BinViewModel
import com.example.justbintime.viewmodel.IBinHolder
import com.example.justbintime.viewmodel.SimBinViewModel

enum class BinModifyMode { MODE_EDIT, MODE_ADD }

@Composable
fun EditBinScreen(viewModel: IBinHolder, navHostController: NavHostController?, binOrig: DisplayableBin) {
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
    val simBinViewModel = SimBinViewModel()
    JustBinTimeTheme(darkTheme = true) {
        val bin = BinFactory().makeLandfillBinWithColours()
        Surface {
            EditBinScreen(simBinViewModel, null, bin)
        }
    }
}
