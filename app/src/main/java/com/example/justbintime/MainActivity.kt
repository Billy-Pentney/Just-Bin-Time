package com.example.justbintime

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.justbintime.ui.theme.AmberWarning
import com.example.justbintime.ui.theme.AmberWarningDark
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.viewmodel.BinViewModel
import com.example.justbintime.viewmodel.BinViewModelFactory
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustBinTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val factory = BinViewModelFactory()
                    val viewModel = ViewModelProvider(this,factory).get(BinViewModel::class.java)

                    val binFactory = BinFactory()
                    val exampleState = binFactory.makeUiState()
                    viewModel.update(exampleState)
                    // 'by' auto-converts the State<BinUiState> to BinUiState
                    val bins by viewModel.uiState.collectAsState()
                    MainScreen(bins)
                }
            }
        }
    }
}


@Composable
fun MainScreen(bins: BinUiState) {
    Column (horizontalAlignment = Alignment.CenterHorizontally) {
        MainStatusText(bins)
        DisplayBins(bins)
    }
}

@Composable
fun MainStatusText(bins: BinUiState) {
    val now = LocalDateTime.now()
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.height(200.dp).padding(24.dp)
    ) {
        Text(
            text = bins.getBinStatus(now),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 32.sp
        )
        Text(
            text = bins.getNumBinsToBeCollectedSoonText(now),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 16.sp
        )
    }
}


@Composable
fun DisplayBins(binUI: BinUiState) {
    val now = LocalDateTime.now()
    val bins = binUI.getSortedBins(now)
    Column (
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        bins.forEach { b ->
            DisplayBin(b, now)
        }
    }
}

@Composable
fun DisplayBin(bin: Bin, now: LocalDateTime) {
    val statusText = remember { mutableStateOf(bin.getStatusText(now)) }
    val actionText = remember { mutableStateOf(bin.getActionText(now)) }

    val binCollected = bin.hasBeenCollected(now)

    if (binCollected)
        bin.determineNextCollectionDate(now)

    val imminentCollection = remember { mutableStateOf(bin.isCollectionImminent(now)) }
    val nextCollectionDateStr = bin.formatDate(bin.nextCollectionDate, "EEEE, dd MMMM yyyy")

    val dark = isSystemInDarkTheme()
    val bkgColor = bin.getBackgroundColor(dark)
    val frgColor = bin.getForegroundColor(dark)

    val warningColor = if (dark) AmberWarning else AmberWarningDark

    Card (
        border = BorderStroke(2.dp, Color.Black),
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = bkgColor
    ) {
        Column (
            modifier = Modifier.padding(16.dp),
        ) {
            Row (
//                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (bin.hasIcon()) {
                    Image(
                        painter = painterResource(bin.iconResId),
                        colorFilter = ColorFilter.tint(frgColor),
                        contentDescription = bin.getIconDescription(),
                        modifier = Modifier
                            .width(25.dp)
                            .aspectRatio(1.0f)
                            .align(Alignment.CenterVertically)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                // Bin-name text
                Text(
                    text = bin.name,
                    color = frgColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            // Indicates if bin has been collected
            Row {
                Text(text = "Next collection: ",
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
            Spacer(modifier = Modifier.height(4.dp))
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

            Spacer(modifier = Modifier.height(4.dp))

            if (imminentCollection.value) {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    WarningForCollection(warningColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Toggle bin status
                    Button(
                        onClick = {
                            bin.updateState(LocalDateTime.now())
                            statusText.value = bin.getStatusText(now)
                            actionText.value = bin.getActionText(now)
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = frgColor),
                        content = {
                            actionText.value?.let { Text(text = it, color = Color.White) }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WarningForCollection(warnColor: Color) {
    Row (verticalAlignment = Alignment.CenterVertically) {
        Text("COLLECTING TOMORROW", color = warnColor, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(R.drawable.warning),
            colorFilter = ColorFilter.tint(warnColor),
            contentDescription = "Warning: bin is due for collection soon",
            modifier = Modifier
                .width(25.dp)
                .aspectRatio(1.0f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val binFactory = BinFactory()
    val exampleState = binFactory.makeUiState()
    JustBinTimeTheme {
        MainScreen(exampleState)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark() {
    val binFactory = BinFactory()
    val exampleState = binFactory.makeUiState()
    JustBinTimeTheme {
        MainScreen(exampleState)
    }
}