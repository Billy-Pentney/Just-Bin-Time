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
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.justbintime.R
import com.example.justbintime.data.`object`.BinColours
import com.example.justbintime.data.`object`.BinColours.Companion.ALL_COLORS
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.data.`object`.BinIcon
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.viewmodel.BinViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun EditBinScreen(viewModel: BinViewModel?, navHostController: NavHostController?, binOrig: DisplayableBin) {

    val binMut by remember { mutableStateOf(binOrig) }
    val drawableRefStr by remember { mutableStateOf(binMut.getIconResStr()) }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    val colorDialogState = rememberMaterialDialogState()

    var binNameStr by remember { mutableStateOf(binMut.getName()) }
    var binPrimaryColour by remember { mutableStateOf(binMut.colours.colorPrimary) }
    var binLastCollectDate by remember { mutableStateOf(binMut.getLastCollectionDate()) }
    var binCollectTime by remember { mutableStateOf(binMut.getLastCollectionTime()) }
    var binCollectIntervalDays by remember { mutableStateOf(binMut.getCollectInterval()) }

    val lblTextSize = 14.sp

    Column (
        modifier = Modifier
            .fillMaxHeight()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Edit Bin",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 40.dp, horizontal = 24.dp)
        )

        Button (
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            onClick = {
                viewModel?.deleteBin(binMut)
                navHostController?.navigateUp()
            }
        ) {
            Text("Delete")
        }

        Spacer(modifier = Modifier.height(12.dp))


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

        val resources = LocalContext.current.resources
        val collectIntervalLbl = resources.getString(R.string.label_bin_collected_interval_days)
        val daysLbl = resources.getString(R.string.label_day_days)

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
                        collectIntervalLbl,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Row {
                        // Days between collections (number only)
                        NumberPicker(
                            value = binCollectIntervalDays,
                            range = 1..100,
                            onValueChange = { binCollectIntervalDays = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            daysLbl,
                            fontSize = lblTextSize,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                    }
                }
            }
        }

        // Choose Bin Icon...
//        val context = LocalContext.current
//        val iconResStrs = context.resources.getStringArray(R.array.BinDrawableIcons).toList()
//        val iconDrawables = iconResStrs.map {
//            val resId = context.resources.getIdentifier(it, "drawable", context.packageName)
//            Image(painterResource(id = resId), contentDescription = null)
//        }
//
//        ListItemPicker(
//            value = iconDrawables[0],
//            onValueChange = { },
//            list = iconDrawables
//        )

        Spacer(Modifier.height(60.dp))

        // Save Button
        Button(
            content = { Text("Save") },
            onClick = {
                val collectAt = LocalDateTime.of(binLastCollectDate, binCollectTime)
                // Make a new bin, updating the properties according to the form
                val bin = binMut.bin.copy(
                    name = binNameStr,
                    daysBetweenCollections = binCollectIntervalDays
                )
                bin.daysBetweenCollections = binCollectIntervalDays
                // Update the collection date (and importantly, recalculate nextCollectionDate)
                bin.setCollectionDate(collectAt)

                // If the colour has changed, make a new colour scheme
                var colours = binMut.colours
                if (binPrimaryColour != binMut.colours.colorPrimary)
                    colours = BinColours(binPrimaryColour)

                var icon = binMut.icon
                if (drawableRefStr != binMut.getIconResStr())
                    icon = BinIcon(0, drawableRefStr)

                // Push changes to the repo
                val newDispBin = DisplayableBin(bin, colours, icon)
                viewModel?.updateBin(newDispBin)
                
                navHostController?.navigateUp()
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
            initialDate = binLastCollectDate,
            title = "Pick collection date for \"$binNameStr\"",
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
            initialTime = binCollectTime,
            title = "Pick collection time for \"$binNameStr\"",
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