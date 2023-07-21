package com.example.justbintime.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.justbintime.data.BinUiState
import com.example.justbintime.data.obj.Bin
import com.example.justbintime.data.obj.BinColours
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.viewmodel.IBinHolder
import com.example.justbintime.viewmodel.SimBinViewModel

@Composable
fun AddBinScreen(viewModel: IBinHolder, navHostController: NavHostController?) {

    val defaultNewBin = Bin.makeDefault()
    val primaryColour = BinColours.GREY
    val icon = viewModel.getIconForNewBin()
    // Make a new bin with the given data
    val displayableBin = DisplayableBin(
        defaultNewBin,
        BinColours(primaryColour),
        icon
    )

    // Use the UI for editing an existing Bin
    ModifyBinScreen(viewModel, navHostController, displayableBin, BinModifyMode.MODE_ADD)
}

@Composable
fun SingleColourBlob(color: Color) {
    Surface (
        shape = CircleShape,
        modifier = Modifier
            .width(30.dp)
            .aspectRatio(1f),
        border = BorderStroke(1.dp, Color.Black),
        color = color
    ) {}
}

@Composable
fun BinColourBlobs(binColours: BinColours, onClick: () -> Unit) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick.invoke() }
    ) {
        SingleColourBlob(color = binColours.colorLight)
        SingleColourBlob(color = binColours.colorPrimary)
        SingleColourBlob(color = binColours.colorDark)
    }
}

@Preview
@Composable
fun PreviewAddBin() {
    val simBinViewModel = SimBinViewModel(BinUiState())

    JustBinTimeTheme (darkTheme = true) {
        Surface {
            // Preview the available colour schemes
//            LazyColumn {
//                items (ALL_COLORS) {
//                    col -> BinColourBlobs(BinColours(col))
//                }
//            }
            AddBinScreen(simBinViewModel, null)
        }
    }
}