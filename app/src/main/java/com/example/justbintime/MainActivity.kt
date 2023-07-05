package com.example.justbintime

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.WorkerThread
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.justbintime.data.Bin
import com.example.justbintime.data.Bin.Companion.COLLECT_TIME_TODAY
import com.example.justbintime.data.Bin.Companion.COLLECT_TIME_TOMORROW
import com.example.justbintime.data.BinFactory
import com.example.justbintime.ui.theme.AmberWarning
import com.example.justbintime.ui.theme.AmberWarningDark
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.ui.theme.RedWarning
import com.example.justbintime.ui.theme.RedWarningDark
import com.example.justbintime.viewmodel.BinViewModel
import com.example.justbintime.viewmodel.BinViewModelFactory
import kotlinx.coroutines.Job
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the bin-repository and construct a view model
        val app = (application as BinApplication)
        val bvmfactory = BinViewModelFactory(app.repo)
        val viewModel = ViewModelProvider(this,bvmfactory)[BinViewModel::class.java]

        val binList: MutableState<List<Bin>> = mutableStateOf(listOf())

        viewModel.binsLive.observe(this) { vmBinList ->
            vmBinList.let {
                viewModel.initDefaultBins()
                // Initialise the bins, if there are none
                binList.value = it
                Log.e("BinObserver", "Got " + binList.value.size + " bins!")
            }
        }

        setContent {
            JustBinTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Load the phrases from the Resources
                    BinPhraseGenerator.initArrays(LocalContext.current)

                    val updateBin = { b: Bin ->
                        viewModel.updateBin(b)
                    }

                    // Display the bin state
//                    val uiState by viewModel.uiState.collectAsState()
                    MainScreen(binList.value, updateBin)
                }
            }
        }
    }
}

@Composable
fun MainScreen(binList: List<Bin>, updateBin: (Bin) -> Job) {
    val binUiState = BinUiState(binList)
    Log.e("BinUISetup", "Made a BinUIState with " + binList.size)

    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        MainStatusText(binUiState)
        DisplayBins(binUiState, updateBin)
    }
}

@Composable
fun MainStatusText(binUiState: BinUiState) {
    val now = LocalDateTime.now()

    val numBinsAwaiting = binUiState.getNumBinsToBeCollectedSoonText(now)
    val binStatusText = binUiState.getMainBinStatusPhrase()

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
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 18.sp,
            color = MaterialTheme.colors.onBackground
        )
    }
}


@Composable
fun DisplayBins(binUiState: BinUiState, updateBin: (Bin) -> Job) {
    val now = LocalDateTime.now()
    val bins = binUiState.getSortedBins(now)
    Log.e("DisplayBins", "Attempt to display " + bins.size)
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        items(bins) { b ->
            DisplayBin(b, now, updateBin)
        }
    }
}

@Composable
fun DisplayBin(bin: Bin, now: LocalDateTime, updateBin: (Bin) -> Job) {
    val statusText = remember { mutableStateOf(bin.getStatusText(now)) }
    val actionText = remember { mutableStateOf(bin.getActionText(now)) }

    val binCollected = remember { mutableStateOf(bin.hasBeenCollected(now)) }
    if (binCollected.value)
        bin.determineNextCollectionDate(now)

    val nextCollectionDateStr = remember { mutableStateOf(bin.getNextCollectionDateStr()) }

    val darkTheme = isSystemInDarkTheme()
    val colors = bin.colors
    val bkgColor = colors.getBackgroundColor(darkTheme)
    val frgColor = colors.getForegroundColor(darkTheme)

    val icon = bin.getIconId(LocalContext.current)

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
    //                horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp)
                ) {
                    if (icon != null) {
                        Image(
                            painter = painterResource(icon),
                            colorFilter = ColorFilter.tint(frgColor),
                            contentDescription = bin.getIconDescription(),
                            modifier = Modifier
                                .aspectRatio(1.0f)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                    // Bin-name text
                    Text(
                        text = bin.name,
                        color = frgColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )
                    Spacer(Modifier.weight(1f))
                    // Button to show DatePicker for changing the Bin Collection Date
                    val context = LocalContext.current
                    val onDateChange = { b: Bin ->
                        updateBin(b)
                        nextCollectionDateStr.value = b.getNextCollectionDateStr()
                    }
                    IconButton (
                        onClick = { DatePickerOverlay(context, bin, onDateChange).show() },
                        modifier = Modifier.aspectRatio(1f)
                    ) {
                        Icon(
                            painterResource(R.drawable.icon_datepick),
                            "Pick a collection date for this bin",
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
                        text = nextCollectionDateStr.value,
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
                        text = statusText.value,
                        fontSize = 18.sp,
                        color = frgColor
                    )
                }

                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    WarningForCollection(bin, darkTheme)
                    actionText.value?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        // Toggle bin status
                        Button(
                            onClick = {
                                val updateNow = LocalDateTime.now()
                                bin.updateState(updateNow)
                                updateBin(bin)
                                statusText.value = bin.getStatusText(updateNow)
                                actionText.value = bin.getActionText(updateNow)
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
                    contentDescription = bin.getIconDescription(),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .alpha(0.08f)
                        .matchParentSize()
                )
            }
        }
    }


}

fun DatePickerOverlay(context: Context, bin: Bin, updateBin: (Bin) -> Unit): DatePickerDialog {
    val year = bin.lastCollectionDate.year
    val month = bin.lastCollectionDate.monthValue-1
    val day = bin.lastCollectionDate.dayOfMonth

    return DatePickerDialog(context, {
        // Add 1 since Jan=0 in the dialog
        _, y, m, d -> bin.updateLastCollection(y,m+1,d)
        updateBin(bin)
    }, year, month, day)
}

@Composable
fun WarningForCollection(bin: Bin, darkTheme: Boolean) {

    val timeOfCollectionStr = bin.getWhenCollectionStr(LocalDateTime.now())

    if (timeOfCollectionStr != null) {
        val warnColor = when (timeOfCollectionStr) {
            COLLECT_TIME_TODAY -> if (darkTheme) RedWarningDark else RedWarning
            COLLECT_TIME_TOMORROW -> if (darkTheme) AmberWarningDark else AmberWarning
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val exampleState = BinFactory().makeUiState()
    // No need to update the bin in the preview, so do nothing
    val updateBin = { _: Bin -> Job() }
    BinPhraseGenerator.initArrays(LocalContext.current)
    JustBinTimeTheme {
        MainScreen(exampleState.bins, updateBin)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark() {
    val exampleState = BinFactory().makeUiState()
    // No need to update the bin in the preview, so do nothing
    val updateBin = { _: Bin -> Job() }
    BinPhraseGenerator.initArrays(LocalContext.current)
    JustBinTimeTheme {
        MainScreen(exampleState.bins, updateBin)
    }
}