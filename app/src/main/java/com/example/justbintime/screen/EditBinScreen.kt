package com.example.justbintime.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.justbintime.BinScreen
import com.example.justbintime.data.`object`.BinColours
import com.example.justbintime.data.`object`.BinColours.Companion.ALL_COLORS
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.BinWithColours
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.viewmodel.BinViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun EditBinScreen(viewModel: BinViewModel?, navHostController: NavHostController?, bwcOrig: BinWithColours) {

    val bwc by remember { mutableStateOf(bwcOrig) }
    val drawableRefStr by remember { mutableStateOf(bwc.getIconResStr()) }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    val colorDialogState = rememberMaterialDialogState()

    var binNameStr by remember { mutableStateOf(bwc.getName()) }
    var binPrimaryColour by remember { mutableStateOf(bwc.colours.colorPrimary) }
    var binLastCollectDate by remember { mutableStateOf(bwc.getLastCollectionDate()) }
    var binCollectTime by remember { mutableStateOf(bwc.getLastCollectionTime()) }
    var binCollectInterval by remember { mutableStateOf(bwc.getCollectInterval()) }

    val lblTextSize = 14.sp

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(24.dp)
    ) {
        Text(
            text = "Edit Bin",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp, horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card (
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp, 12.dp, 20.dp, 8.dp)
            ) {
                // Bin Name Text Input
                TextField(
                    value = binNameStr,
                    onValueChange = { binNameStr = it },
                    label = { Text("Bin Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Colour Scheme:",
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    )
                    BinColourBlobs(BinColours(binPrimaryColour))
                    // Preview the chosen color, and click to edit
                    Button(
                        onClick = { colorDialogState.show() }
                    ) {
                        Text("Pick")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            Card (
                backgroundColor = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp,12.dp,20.dp,8.dp)
                ) {
                    Text("Last collected on:", fontSize = lblTextSize)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(binLastCollectDate.format(dateFormatter), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Button (onClick = { dateDialogState.show() }) {
                        Text("Change Date")
                    }
                }
            }
            Spacer(modifier=Modifier.width(8.dp))
            Card (
                backgroundColor = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp,12.dp,20.dp,8.dp)
                ) {
                    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                    Text("Last collected at:", fontSize = lblTextSize)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(binCollectTime.format(timeFormatter), fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    // Opens Time of First Collection Dialog
//                        IconButton(onClick = { dateDialogState.show() }) {
//                            Icon(
//                                painterResource(R.drawable.icon_time),
//                                "Pick a collection time",
//                                tint = MaterialTheme.colors.onSurface
//                            )
//                        }
                    Spacer(modifier = Modifier.height(4.dp))
                    Button (onClick = { timeDialogState.show() }) {
                        Text("Change Time")
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Card (backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column (modifier = Modifier.padding(20.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "This bin is collected every...",
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Row {
                        // Days between collections (number only)
                        NumberPicker(
                            value = binCollectInterval,
                            range = 1..100,
                            onValueChange = { binCollectInterval = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "day(s)",
                            fontSize = lblTextSize,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(60.dp))

        // Save Button
        Button(
            content = { Text("Save") },
            onClick = {
                val collectAt = LocalDateTime.of(binLastCollectDate, binCollectTime)
                val bin = bwc.bin
                // Update the given bin with the details supplied
                bin.lastCollectionDate = collectAt
                if (binPrimaryColour != bwc.colours.colorPrimary)
                    bwc.updateColours(BinColours(binPrimaryColour))
                bin.name = binNameStr
                bin.iconResStr = drawableRefStr
                viewModel?.updateBin(bwc)
                navHostController?.navigate(BinScreen.ViewBins.name)
                Log.e("BinNavigation", "Navigated back to ViewBinsScreen")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }

    MaterialDialog (
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text="Ok")
            negativeButton(text="Cancel")
        }
    ) {
        datepicker(
            initialDate = LocalDate.now(),
            title = "Pick collection date for $binNameStr",
            // Only allow dates not in the future
            allowedDateValidator = {
                !it.isAfter(LocalDate.now())
            }
        ) {
            binLastCollectDate = it
        }
    }

    MaterialDialog (
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text="Ok")
            negativeButton(text="Cancel")
        }
    ) {
        timepicker(
            initialTime = LocalTime.now(),
            title = "Pick collection time for $binNameStr",
            is24HourClock = true
        ) {
            binCollectTime = it
        }
    }

    MaterialDialog (
        dialogState = colorDialogState,
        buttons = {
            positiveButton(text="Ok")
            negativeButton(text="Cancel")
        }
    ) {
        colorChooser(
            colors = ALL_COLORS,
            initialSelection = 0
        ) { binPrimaryColour = it }
    }
}

@Preview
@Composable
fun PreviewEditBin() {
    JustBinTimeTheme (darkTheme = true) {
        val bin = BinFactory().makeLandfillBinWithColours()
        Surface {
            EditBinScreen(null, null, bin)
        }
    }
}