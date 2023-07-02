package com.example.justbintime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
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

                    MainScreen(viewModel)
                }
            }
        }
    }
}


@Composable
fun MainScreen(viewModel: BinViewModel) {
    // 'by' auto-converts the State<BinUiState> to BinUiState
    val bins by viewModel.uiState.collectAsState()
    DisplayBins(bins)
}

@Composable
fun DisplayBins(binUI: BinUiState) {
    val now = LocalDateTime.now()
    val bins = binUI.getSortedBins(now)
    Column {
        bins.forEach { b ->
            DisplayBin(b, now)
        }
    }
}

@Composable
fun DisplayBin(bin: Bin, now: LocalDateTime) {
    val nextCollectionDate = bin.getNextCollectionDate(now)
    val nextCollectionDateStr = bin.formatDate(nextCollectionDate, "EEEE, dd MMMM yyyy")

    Card (border = BorderStroke(2.dp, Color.Black), modifier = Modifier.fillMaxWidth()) {
        Column (modifier = Modifier.padding(12.dp)) {
            Row (
//                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (bin.hasIcon()) {
                    Image(
                        painter = painterResource(bin.iconResId),
                        colorFilter = ColorFilter.tint(bin.color),
                        contentDescription = bin.getIconDescription(),
                        modifier = Modifier
                            .width(25.dp)
                            .aspectRatio(1.0f)
                            .align(Alignment.CenterVertically)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = bin.name,
                    color = bin.color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            }

            // Indicates if bin has been collected and/or put out
            Text("Status: ${bin.getStatusText()}")
            Text("Next Collection: $nextCollectionDateStr")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val binFactory = BinFactory()
    val exampleState = binFactory.makeUiState()

    JustBinTimeTheme {
        DisplayBins(exampleState)
    }
}