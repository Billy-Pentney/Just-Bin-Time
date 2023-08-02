package com.example.justbintime.screen

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.justbintime.ProvideAppBarAction
import com.example.justbintime.ProvideAppBarTitle
import com.example.justbintime.ProvideFloatingActionButton
import com.example.justbintime.SetVisibilityForNavBackButton
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.viewmodel.IBinHolder
import com.example.justbintime.viewmodel.SimBinViewModel

enum class BinModifyMode { MODE_EDIT, MODE_ADD }

@Composable
fun EditBinScreen(
    viewModel: IBinHolder,
    binOrig: DisplayableBin,
    navigateUp: (String) -> (Boolean)
) {
    val navUpWithName = {
        navigateUp("Edit Bin")
    }

    SetVisibilityForNavBackButton(true)

    ProvideAppBarTitle { Text("Edit Bin") }
    ProvideAppBarAction {
        IconButton(onClick = {
            navUpWithName()
            viewModel.deleteBin(binOrig.bin)
        }
        ) {
            Icon(Icons.Filled.Delete, "Delete this bin")
        }
    }
    ModifyBinScreen(
        viewModel,
        navUpWithName,
        binOrig,
        BinModifyMode.MODE_EDIT,
    )
}


@Preview
@Composable
fun PreviewEditBin() {
    val simBinViewModel = SimBinViewModel()
    JustBinTimeTheme(darkTheme = true) {
        val bin = BinFactory().makeLandfillBinWithColours()
        Surface {
            EditBinScreen(simBinViewModel, bin) { true }
        }
    }
}
