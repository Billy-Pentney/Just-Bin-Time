package com.example.justbintime.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.justbintime.BinScreen
import com.example.justbintime.data.BinUiState
import com.example.justbintime.R
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.obj.Bin
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.ui.theme.AmberWarning
import com.example.justbintime.ui.theme.AmberWarningDark
import com.example.justbintime.ui.theme.GreenPrimary100
import com.example.justbintime.ui.theme.GreenPrimary900
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.ui.theme.RedWarning
import com.example.justbintime.ui.theme.RedWarningDark
import com.example.justbintime.viewmodel.BinViewModel
import com.example.justbintime.viewmodel.IBinHolder
import com.example.justbintime.viewmodel.SimBinViewModel
import java.time.LocalDateTime


@Composable
fun ViewBinScreen(viewModel: IBinHolder, navController: NavController) {
//    val binList by viewModel.binsLive.observeAsState()
    val binUiState by viewModel.getUiState().collectAsState()
    val now by remember { mutableStateOf(LocalDateTime.now()) }

    Log.e("ViewBinScreen", "Got a BinUIState with " + (binUiState.bwcList.size) + " bins")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp),
    ) {
        item {
            MainStatusText(binUiState, now)
        }
        items(binUiState.getSortedBins(now)) { b ->
            DisplayBin(viewModel, navController, b)
        }
        Log.e("DisplayBins", "Attempt to display " + binUiState.bwcList.size)
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {
                AddBinButton(navController)
//                Spacer(modifier= Modifier.width(4.dp))
//                // Add ability to restore the default three bins
//                if (!viewModel.initialisedBins) {
//                    AddDefaultBinsButton(viewModel)
//                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MainStatusText(binUiState: BinUiState, now: LocalDateTime) {
    val numBinsAwaiting = binUiState.getNumBinsToBeCollectedSoonText(now)
//    val originalStatusPhrase =
    var binStatusText by remember { mutableStateOf(binUiState.getMainBinStatusPhrase()) }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight(0.25f)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text(
            text = binStatusText,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 36.sp,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = numBinsAwaiting,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colors.onBackground
        )
        IconButton(
            onClick = {
                binUiState.updateMainBinStatusPhrase()
                binStatusText = binUiState.getMainBinStatusPhrase()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Icon(Icons.Default.Refresh, "Refresh the phrase")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun DisplayBin(viewModel: IBinHolder, navController: NavController, bwc: DisplayableBin) {
    val now by remember { mutableStateOf(LocalDateTime.now()) }

    val statusText = bwc.getStatusText(now)
    val actionText = bwc.getActionText(now)

    val nextCollectionDateStr = bwc.getNextCollectionDateStr(now)

    val darkTheme = isSystemInDarkTheme()
    val colors = bwc.colours
    val bkgColor = colors.getBackgroundColor(darkTheme)
    val frgColor = colors.getForegroundColor(darkTheme)

    val icon = bwc.getIconId(LocalContext.current)

    Card(
        border = BorderStroke(2.dp, Color.Black),
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = bkgColor,
        shape = RoundedCornerShape(20.dp)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                ) {
                    if (icon != null) {
                        Image(
                            painter = painterResource(icon),
                            colorFilter = ColorFilter.tint(frgColor),
                            contentDescription = bwc.getIconDescription(),
                            modifier = Modifier
                                .aspectRatio(1.0f)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    // Bin-name text
                    Text(
                        text = bwc.getName(),
                        color = frgColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )
                    Spacer(Modifier.weight(1f))
                    // Button to edit bin details
                    IconButton (
                        onClick = {
                            viewModel.setVisibleBin(bwc)
                            navController.navigate(BinScreen.EditBin.name)
                        },
                        modifier = Modifier.aspectRatio(1f)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            "Edit details of this bin",
                            tint = frgColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Indicates if bin has been collected
                Row {
                    Text(
                        text = "Next collection: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = frgColor
                    )
                    Text(
                        text = nextCollectionDateStr,
                        fontSize = 18.sp,
                        color = frgColor
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Indicates if bin has been collected
                Row {
                    Text(
                        text = "Status: ",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = frgColor
                    )
                    Text(
                        text = statusText,
                        fontSize = 18.sp,
                        color = frgColor
                    )
                }

                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WarningForCollection(bwc, darkTheme)
                    actionText?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        // Toggle bin status
                        Button(
                            onClick = {
                                val bin = bwc.bin
                                bin.updateState(LocalDateTime.now())
                                viewModel.updateBin(bin)
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = frgColor),
                            content = {
                                Text(text = it, color = bkgColor)
                            },
                        )
                    }
                }
            }

            // Card transparent overlay
            if (icon != null) {
                Image(
                    painter = painterResource(icon),
                    colorFilter = ColorFilter.tint(frgColor),
                    contentDescription = bwc.getIconDescription(),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .alpha(0.08f)
                        .matchParentSize()
                )
            }
        }
    }
}



@Composable
fun AddBinButton(navController: NavController) {
    val bkgColor = if (isSystemInDarkTheme()) GreenPrimary100 else GreenPrimary900
    val frgColor = if (isSystemInDarkTheme()) GreenPrimary900 else GreenPrimary100

    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = bkgColor, contentColor = frgColor),
        shape = CircleShape,
        onClick = {
            navController.navigate(BinScreen.AddBin.name)
            Log.e("BinNavigation", "Navigated to AddBinScreen")
        },
        modifier = Modifier.fillMaxHeight()
    ) {
        Icon(painterResource(id = R.drawable.icon_add), "Button to add a new Bin", tint = frgColor)
        Spacer(Modifier.width(4.dp))
        Text("ADD", fontSize = 16.sp)
    }
}

@Composable
fun AddDefaultBinsButton(viewModel: BinViewModel?) {
    val bkgColor = if (isSystemInDarkTheme()) GreenPrimary100 else GreenPrimary900
    val frgColor = if (isSystemInDarkTheme()) GreenPrimary900 else GreenPrimary100

    val context = LocalContext.current
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = bkgColor, contentColor = frgColor),
        shape = CircleShape,
        onClick = {
            viewModel?.initDefaultBins(context)
        },
        modifier = Modifier.fillMaxHeight()
    ) {
        Spacer(Modifier.width(4.dp))
        Text("Restore Default", fontSize = 16.sp)
    }
}


@Composable
fun WarningForCollection(bwc: DisplayableBin, darkTheme: Boolean) {

    val timeOfCollectionStr = bwc.getWhenCollectionStr(LocalDateTime.now())

    if (timeOfCollectionStr != null) {
        val warnColor = when (timeOfCollectionStr) {
            Bin.COLLECT_TIME_TODAY -> if (darkTheme) RedWarningDark else RedWarning
            Bin.COLLECT_TIME_TOMORROW -> if (darkTheme) AmberWarningDark else AmberWarning
            else -> Color.White
        }

        val warnTextColor = if (darkTheme) Color.White
        else           Color.Black

        Spacer(Modifier.height(12.dp))
        Card (
            backgroundColor = warnColor,
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(10.dp,6.dp,10.dp,6.dp)
            ) {
                Text("DUE $timeOfCollectionStr",
                    color = warnTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(R.drawable.warning),
                    colorFilter = ColorFilter.tint(warnTextColor),
                    contentDescription = "Warning sign indicating that this bin is due for collection soon",
                    modifier = Modifier
                        .width(20.dp)
                        .aspectRatio(1.0f)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewMainBinScreen() {
    val simBinViewModel = SimBinViewModel(BinFactory().makeUiState())
    JustBinTimeTheme(darkTheme = true) {
        val nc = rememberNavController()
        Surface {
            ViewBinScreen(simBinViewModel, nc)
        }
    }
}