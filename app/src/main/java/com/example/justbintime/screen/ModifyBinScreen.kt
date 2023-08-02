package com.example.justbintime.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.justbintime.ProvideFloatingActionButton
import com.example.justbintime.R
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.data.obj.Bin
import com.example.justbintime.data.obj.BinColours
import com.example.justbintime.data.obj.BinIcon
import com.example.justbintime.data.obj.BinReminder
import com.example.justbintime.notifications.AndroidReminderScheduler
import com.example.justbintime.notifications.ReminderScheduler
import com.example.justbintime.permissions.PermissionProviderFactory
import com.example.justbintime.permissions.PermissionResultDialog
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.viewmodel.AppViewModel
import com.example.justbintime.viewmodel.BinViewModel
import com.example.justbintime.viewmodel.IBinHolder
import com.example.justbintime.viewmodel.SimBinViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun ModifyBinScreen(
    binViewModel: IBinHolder,
    navigateUp: () -> Boolean,
    binOrig: DisplayableBin,
    modifyMode: BinModifyMode,
) {
    val isCreating = modifyMode == BinModifyMode.MODE_ADD

    val mutableOriginalBin by remember { mutableStateOf(binOrig) }

    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()
    val colorDialogState = rememberMaterialDialogState()

    val iconName = remember { mutableStateOf(mutableOriginalBin.icon.drawableName) }

    val binNameStr = remember { mutableStateOf(mutableOriginalBin.getName()) }
    val binPrimaryColour = remember { mutableStateOf(mutableOriginalBin.colours.colorPrimary) }
    val binLastCollectDate = remember { mutableStateOf(mutableOriginalBin.getLastCollectionDate()) }
    val binCollectTime = remember { mutableStateOf(mutableOriginalBin.getLastCollectionTime()) }
    val binCollectIntervalDays = remember { mutableStateOf(mutableOriginalBin.getCollectInterval()) }
    val binReminderSetting = remember { mutableStateOf(mutableOriginalBin.getReminderSetting()) }

    val lblTextSize = 14.sp

    // Add a FAB to the Homepage Scaffold
    ProvideFloatingActionButton {
        SaveBinFab(
            binViewModel,
            isCreating, mutableOriginalBin, binNameStr,
            binLastCollectDate, binCollectTime, binCollectIntervalDays,
            binReminderSetting, binPrimaryColour, iconName, navigateUp
        )
    }

    LazyColumn (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight().padding(24.dp)
    ) {
        //item { TitleText(isCreating) }
        item { BinNameCard(binNameStr) }
        item { BinLastCollectDateTimeCards(
                binLastCollectDate,
                binCollectTime,
                lblTextSize,
                showDateDialog = { dateDialogState.show() },
                showTimeDialog = { timeDialogState.show() }
            )
        }
        item { BinCollectIntervalCard(binCollectIntervalDays, lblTextSize) }
        item { BinColourAndIconCard(binPrimaryColour, colorDialogState, iconName) }
        item { BinNotificationsCard(binReminderSetting) }
    }

    val listOfColors = binViewModel.getColours()
    // Setup the dialog pickers
    DateDialogSetup(dateDialogState, binLastCollectDate, binNameStr)
    TimeDialogSetup(timeDialogState, binCollectTime, binNameStr)
    ColorDialogSetup(colorDialogState, listOfColors, binPrimaryColour, binNameStr)
}


@Composable
fun SaveBinFab(
    binViewModel: IBinHolder,
    isCreating: Boolean,
    binMut: DisplayableBin,
    binNameStr: MutableState<String>,
    binLastCollectDate: MutableState<LocalDate>,
    binCollectTime: MutableState<LocalTime>,
    binCollectIntervalDays: MutableState<Int>,
    binReminderSetting: MutableState<Boolean>,
    binPrimaryColour: MutableState<Color>,
    iconName: MutableState<String>,
    navigateUp: () -> Boolean
) {
    val context = LocalContext.current
    val reminderScheduler = AndroidReminderScheduler(context)

    FloatingActionButton(
        backgroundColor = MaterialTheme.colors.secondary,
        onClick = {
            val collectAt = LocalDateTime.of(binLastCollectDate.value, binCollectTime.value)
            // Make a new bin, updating the properties according to the form
            val newBin = binMut.bin.copy(
                name = binNameStr.value,
                daysBetweenCollections = binCollectIntervalDays.value
            )
            // Update the collection date (and importantly, recalculate nextCollectionDate)
            newBin.setCollectionDate(collectAt)

            cancelAndOrScheduleBinReminder(
                binViewModel,
                binMut.reminders,
                newBin,
                binReminderSetting.value,
                reminderScheduler
            )

            addBinColourIconReferences(
                binViewModel,
                newBin,
                binPrimaryColour.value,
                iconName.value
            )

            // Push bin changes to the repository
            if (isCreating) {
                binViewModel.insertBin(newBin)
            } else {
                binViewModel.updateBin(newBin)
            }

            navigateUp()
        }
    ) {
        Icon(painterResource(R.drawable.icon_save), contentDescription = "Save this Bin")
    }
}


fun cancelAndOrScheduleBinReminder(
    viewModel: IBinHolder,
    oldReminders: List<BinReminder>,
    bin: Bin,
    userChoseToSendReminder: Boolean,
    reminderScheduler: ReminderScheduler
) {
    // If the bin was previously sending reminders, cancel them all
    if (bin.sendReminder) {
        for (reminder: BinReminder in oldReminders) {
            viewModel.deleteBinReminder(reminder)
            reminderScheduler.cancel(reminder)
        }
    }
    // Then, if the bin is now set to send reminders, create a new one
    if (userChoseToSendReminder) {
        val reminder = BinReminder(bin)
        reminderScheduler.schedule(reminder)
        viewModel.upsertBinReminder(reminder)
    }
    bin.sendReminder = userChoseToSendReminder
}


fun addBinColourIconReferences(viewModel: IBinHolder, bin: Bin, binPrimaryColour: Color, iconName: String) {
    // Try to get the ID of the existing Colour Scheme
    var bcId = viewModel.getBinColoursId(binPrimaryColour)
    // If it can't be found, make a new one and add it
    if (bcId == null) {
        val colours = BinColours(binPrimaryColour)
        viewModel.insertColour(colours)
        bcId = viewModel.getBinColoursId(binPrimaryColour)
    }
    // Then, set the relationship between Bin and BinColours
    bin.binColoursId = bcId ?: 1

    // Try to get the ID of the existing Icon
    val iconId = viewModel.getBinIconId(iconName)
    // If the icon can't be found, then we can't add another
    // without knowing the Drawable Resource String identifier
//                    if (iconId == null) {
//                        val iconResStr = BinIcon.nameToResourceString(iconNameValue)
//                        val icon = BinIcon(0, iconResStr, iconNameValue)
//                        viewModel?.addIcon(icon)
//                        iconId = viewModel?.getBinColoursId(iconNameValue)
//                    }
    // Then, set the reference from Bin to the Icon
    bin.binIconId = iconId ?: 1
}

@Composable
fun TitleText(creating: Boolean) {
    val titleText = if (creating) "Add a new Bin"
                    else          "Edit Bin"

    // Title Text
    Text(
        text = titleText,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp, horizontal = 24.dp)
    )
}


@Composable
fun BinNameCard(binNameStr: MutableState<String>) {
    Card (
        backgroundColor = MaterialTheme.colors.secondary,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            Text("This bin is called...", modifier = Modifier.align(Alignment.Start))
            Spacer(Modifier.height(12.dp))
            // Bin Name Text Input
            TextField(
                value = binNameStr.value,
                textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 20.sp),
                onValueChange = { binNameStr.value = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun BinCollectIntervalCard(binCollectIntervalDays: MutableState<Int>, lblTextSize: TextUnit) {
    // Choose Bin Collection Interval Card
    val resources = LocalContext.current.resources
    val collectIntervalLbl = resources.getString(R.string.label_bin_collected_interval_days)
    val daysLbl = resources.getString(R.string.label_day_days)

    Card (backgroundColor = MaterialTheme.colors.secondary,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column (modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
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
                        value = binCollectIntervalDays.value,
                        range = 1..100,
                        onValueChange = { binCollectIntervalDays.value = it }
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
}

// Setting-up Date/Time/Color picker Dialogs

@Composable
fun DateDialogSetup(
    dateDialogState: MaterialDialogState,
    binLastCollectDate: MutableState<LocalDate>,
    binNameStr: MutableState<String>,
) {
    // Set-up Dialog Window for Date picker
    MaterialDialog (
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text="Ok")
            negativeButton(text="Cancel")
        }
    ) {
        datepicker(
            initialDate = binLastCollectDate.value,
            title = "Pick collection date for \"${binNameStr.value}\"",
            // Only allow dates not in the future
            allowedDateValidator = {
                !it.isAfter(LocalDate.now())
            }
        ) {
            binLastCollectDate.value = it
        }
    }
}

@Composable
fun TimeDialogSetup(
    timeDialogState: MaterialDialogState,
    binCollectTime: MutableState<LocalTime>,
    binNameStr: MutableState<String>,
) {
    // Set-up Dialog Window for Time picker
    MaterialDialog (
        dialogState = timeDialogState,
        buttons = {
            positiveButton(text="Ok")
            negativeButton(text="Cancel")
        }
    ) {
        timepicker(
            initialTime = binCollectTime.value,
            title = "Pick collection time for \"${binNameStr.value}\"",
            is24HourClock = true
        ) {
            binCollectTime.value = it
        }
    }
}

@Composable
fun ColorDialogSetup(
    colorDialogState: MaterialDialogState,
    listOfColors: List<Color>,
    binPrimaryColour: MutableState<Color>,
    binNameStr: MutableState<String>
) {
    var initialIndex = 0
    for (i in listOfColors.indices) {
        if (listOfColors[i] == binPrimaryColour.value) {
            initialIndex = i
        }
    }

    MaterialDialog (
        dialogState = colorDialogState,
        buttons = {
            positiveButton(text="Ok")
            negativeButton(text="Cancel")
        }
    ) {
        Text(
            "Pick a Primary Colour for \"${binNameStr.value}\"",
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp
        )
        colorChooser(
            colors = listOfColors,
            initialSelection = initialIndex
        ) {
            binPrimaryColour.value = it
        }
    }
}

// More UI cards
@Composable
fun BinLastCollectDateTimeCards(
    binLastCollectDate: MutableState<LocalDate>,
    binLastCollectTime: MutableState<LocalTime>,
    lblTextSize: TextUnit,
    showDateDialog: () -> Unit,
    showTimeDialog: () -> Unit,
) {
    // Choose Last Collection Date/Time
    Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        Card (
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp,16.dp,20.dp,12.dp)
            ) {
                Text("Last collected on:", fontSize = lblTextSize)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    binLastCollectDate.value.format(dateFormatter),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button (onClick = { showDateDialog() }) {
                    Text("Change Date")
                }
            }
        }
        Spacer(modifier= Modifier.width(8.dp))
        Card (
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp,16.dp,20.dp,12.dp)
            ) {
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                Text("Last collected at:", fontSize = lblTextSize)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    binLastCollectTime.value.format(timeFormatter),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Opens Time of First Collection Dialog
                Button (onClick = { showTimeDialog() }) {
                    Text("Change Time")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BinColourAndIconCard(
    binPrimaryColour: MutableState<Color>,
    colorDialogState: MaterialDialogState,
    binIconName: MutableState<String>
) {
    Card (backgroundColor = MaterialTheme.colors.secondary,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column (modifier = Modifier.padding(20.dp)) {
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
                BinColourBlobs(BinColours(binPrimaryColour.value)) {
                    colorDialogState.show()
                }
            }
            Spacer(Modifier.height(24.dp))

            Row {
                Text("Icon:", Modifier.align(Alignment.CenterVertically))
                Spacer(Modifier.weight(1f))

                var dropdownMenuExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = dropdownMenuExpanded,
                    onExpandedChange = { dropdownMenuExpanded = !dropdownMenuExpanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = binIconName.value,
                        onValueChange = {},
                        leadingIcon = { DrawIconFromName(binIconName.value) },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = dropdownMenuExpanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier
                            .width(200.dp)
                            .align(Alignment.CenterVertically)
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownMenuExpanded,
                        onDismissRequest = { dropdownMenuExpanded = false }
                    ) {
                        BinIcon.NAME_LIST.forEach { listIconName ->
                            DropdownMenuItem(
                                onClick = {
                                    binIconName.value = listIconName
                                    dropdownMenuExpanded = false
                                }
                            ) {
                                DrawIconFromName(listIconName)
                                Spacer(Modifier.width(4.dp))
                                Text(listIconName)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BinNotificationsCard(binReminderSetting: MutableState<Boolean>) {
    val context = LocalContext.current
    val notificationPermissionGotten by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        == PERMISSION_GRANTED)
        } else {
            mutableStateOf(true)
        }
    }
    val alarmPermissionGotten by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(context, Manifest.permission.SCHEDULE_EXACT_ALARM)
                        == PERMISSION_GRANTED)
        } else {
            mutableStateOf(true)
        }
    }
    val anyPermissionMissing by
        remember { mutableStateOf(!alarmPermissionGotten || !notificationPermissionGotten) }

    Card (backgroundColor = MaterialTheme.colors.secondary,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column (modifier = Modifier.padding(20.dp)) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "Send reminder notification 24 hours before bin is due",
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .alpha(if (anyPermissionMissing) 0.5f else 1f)
                )
                Spacer(Modifier.weight(1f))
                Checkbox(
                    checked = binReminderSetting.value,
                    onCheckedChange = {
                        binReminderSetting.value = it
                    },
                    enabled = !anyPermissionMissing,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colors.secondary
                    )
                )
            }
            if (anyPermissionMissing) {
                val vm = viewModel<AppViewModel>()
                vm.visiblePermissionDialogQueue.clear()
                if (!notificationPermissionGotten)
                    vm.visiblePermissionDialogQueue.add(Manifest.permission.POST_NOTIFICATIONS)
                if (!alarmPermissionGotten)
                    vm.visiblePermissionDialogQueue.add(Manifest.permission.SCHEDULE_EXACT_ALARM)
                ButtonToGetPermissions(Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}


@Composable
fun ButtonToGetPermissions(modifier: Modifier) {
    val viewModel = viewModel<AppViewModel>()
    val dialogQueue = viewModel.visiblePermissionDialogQueue

    val permissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissionMap ->
            permissionMap.keys.forEach {
                permission -> viewModel.onPermissionResult(
                    permission,
                    permissionMap[permission] == true
                )
            }
        }
    )

    val context = LocalContext.current

    dialogQueue.forEach { permission ->
        PermissionProviderFactory.from(permission)?.let {
            PermissionResultDialog(
                permissionTextProvider = it,
                isPermanentlyDenied = viewModel.isPermanentlyDenied(permission),
                onDismiss = viewModel::dismissDialog,
                onOkay = {
                    viewModel.dismissDialog()
                    if (viewModel.shouldRetry(permission)) {
                        permissionResultLauncher.launch(arrayOf(permission))
                        Log.d("PermissionLauncher", "Requesting permission $permission")
                    }
                },
                onGotoAppSettings = { openAppSettings(context) }
            )
        }
    }

    Button (
        onClick = {
            val firstPermission = viewModel.visiblePermissionDialogQueue.first()
            permissionResultLauncher.launch(arrayOf(firstPermission))
            Log.d("PermissionLauncher", "Requesting permission $firstPermission")
        },
        modifier = modifier
    ) {
        Text("Grant Permissions")
    }
}

fun openAppSettings(context: Context) {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    ).also {
        startActivity(context, it, null)
    }
}
















// TODO - figure out how best to store Content Description for each Icon

@Composable
fun DrawIconFromName(iconName: String?) {
    val drawableString = iconName?.let { BinIcon.nameToResourceString(it) }
    val iconDrawableId = drawableString?.let {
        BinIcon.getDrawableResourceId(LocalContext.current, it)
    }
    iconDrawableId?.let {
        Image(
            painterResource(it),
            "Preview bin icon",
            modifier = Modifier
                .width(25.dp)
                .aspectRatio(1f)
        )
        Spacer(Modifier.width(4.dp))
    }
}

@Preview
@Composable
fun PreviewModifyBin() {
    val simBinViewModel = SimBinViewModel()
    JustBinTimeTheme(darkTheme = true) {
        val bin = BinFactory().makeLandfillBinWithColours()
        Surface {
            ModifyBinScreen(
                simBinViewModel,
                { true },
                bin,
                BinModifyMode.MODE_EDIT,
//                PaddingValues()
            )
        }
    }
}


