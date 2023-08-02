package com.example.justbintime

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.LocalOwnersProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.BinPhraseGenerator
import com.example.justbintime.data.DisplayableBin
import com.example.justbintime.screen.AddBinButton
import com.example.justbintime.screen.AddBinScreen
import com.example.justbintime.screen.DisplayBin
import com.example.justbintime.screen.EditBinScreen
import com.example.justbintime.screen.ViewBinsScreen
import com.example.justbintime.ui.theme.JustBinTimeTheme
import com.example.justbintime.viewmodel.BinViewModel
import com.example.justbintime.viewmodel.BinViewModelFactory
import com.example.justbintime.viewmodel.MainScaffoldViewModel
import com.example.justbintime.viewmodel.SimBinViewModel
import kotlinx.coroutines.flow.Flow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the bin-repository and construct a view model
        val app = (application as BinApplication)
        val bvmfactory = BinViewModelFactory(app.repo)
        val viewModel = ViewModelProvider(this,bvmfactory)[BinViewModel::class.java]



        setContent {
            JustBinTimeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Load the phrases from the Resources
                    BinPhraseGenerator.initArrays(LocalContext.current)

                    val navController = rememberNavController()

                    val navigateToAddBin = {
                        Log.d("BinNavigation", "Navigating to AddBinScreen")
                        navController.navigate(Screen.AddBin.name)
                    }
                    val navigateToEditBin = { bin: DisplayableBin ->
                        Log.d("BinNavigation", "Navigating to EditBinScreen")
                        viewModel.setVisibleBin(bin)
                        navController.navigate(Screen.EditBin.name)
                    }

                    val navigateUp = { currScreen: String ->
                        Log.d("BinNavigation", "Navigating up from $currScreen")
                        navController.navigateUp()
                    }

                    Scaffold(
                        topBar = {
                            MyTopAppBar(navController.currentBackStackEntryFlow) {
                                navController.navigateUp()
                            }
                        },
                        floatingActionButton = {
                            MyFloatingActionButton(navController.currentBackStackEntryFlow)
                        },
                        floatingActionButtonPosition = FabPosition.End
                    ) {
                        padding ->
                        NavHost(
                            navController,
                            startDestination = Screen.ViewBins.name,
                            modifier = Modifier.padding(padding)
                        ) {
                            composable(route = Screen.ViewBins.name) {
                                ViewBinsScreen(
                                    viewModel,
                                    navigateToAddBin,
                                    navigateToEditBin
                                )
                            }
                            composable(route = Screen.AddBin.name) {
                                AddBinScreen(viewModel, navigateUp)
                            }
                            composable(route = Screen.EditBin.name) {
                                val bin = viewModel.getVisibleBin()
                                bin?.let {
                                    EditBinScreen(viewModel, it, navigateUp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MyFloatingActionButton(currentBackStackEntryFlow: Flow<NavBackStackEntry>) {
    val currentContentBackStackEntry by produceState(
        initialValue = null as NavBackStackEntry?,
        producer = {
            currentBackStackEntryFlow.collect { value = it }
        }
    )
    val stateHolder = rememberSaveableStateHolder()
    currentContentBackStackEntry?.LocalOwnersProvider(stateHolder) {
        val scaffoldViewModel = viewModel<MainScaffoldViewModel>()
        scaffoldViewModel.fabState?.let { it() }
    }
}

@Composable
fun MyTopAppBar(
    currentBackStackEntryFlow: Flow<NavBackStackEntry>,
    navigateUp: () -> Unit
) {
    val currentContentBackStackEntry by produceState(
        initialValue = null as NavBackStackEntry?,
        producer = {
            currentBackStackEntryFlow.collect { value = it }
        }
    )

    val stateHolder = rememberSaveableStateHolder()
    currentContentBackStackEntry?.LocalOwnersProvider(stateHolder) {
        val scaffoldViewModel = viewModel<MainScaffoldViewModel>()

        TopAppBar(
            navigationIcon = if (scaffoldViewModel.visibleTopBarBackButton) {
//                currentContentBackStackEntry?.LocalOwnersProvider(stateHolder) {
//                    if (scaffoldViewModel.visibleTopBarBackButton) {
                    { BackNavIconButton { navigateUp() } }
                    }
            else {
                 null }
//                }
            ,
            title = {
//                val stateHolder = rememberSaveableStateHolder()
//                currentContentBackStackEntry?.LocalOwnersProvider(stateHolder) {
//                    val scaffoldViewModel = viewModel<MainScaffoldViewModel>()
                    scaffoldViewModel.topBarTitleState?.let { it() }
//                }
            },
            actions = {
//                val stateHolder = rememberSaveableStateHolder()
//                currentContentBackStackEntry?.LocalOwnersProvider(stateHolder) {
//                    val scaffoldViewModel = viewModel<MainScaffoldViewModel>()
                    scaffoldViewModel.topBarActionState?.let { it() }
//                }
            }
        )
    }
}

@Composable
fun BackNavIconButton(
    navigateUp: () -> Unit
) {
    IconButton(
        onClick = { navigateUp() },
        content = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Arrow pointing backwards"
            )
        }
    )
}

@Composable
fun ProvideAppBarAction(actions: @Composable RowScope.() -> Unit) {
    val actionViewModel = viewModel<MainScaffoldViewModel>()
    SideEffect { actionViewModel.topBarActionState = actions }
}

@Composable
fun ProvideAppBarTitle(title: @Composable () -> Unit) {
    val actionViewModel = viewModel<MainScaffoldViewModel>()
    SideEffect { actionViewModel.topBarTitleState = title }
}

@Composable
fun ProvideFloatingActionButton(fab: @Composable () -> Unit) {
    val actionViewModel = viewModel<MainScaffoldViewModel>()
    Log.d("FAB_SET", "Set the fab")
    SideEffect { actionViewModel.fabState = fab }
}

@Composable
fun SetVisibilityForNavBackButton(visible: Boolean) {
    val actionViewModel = viewModel<MainScaffoldViewModel>()
    SideEffect { actionViewModel.visibleTopBarBackButton = visible }
}











@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val exampleState = BinFactory().makeUiState()
    BinPhraseGenerator.initArrays(LocalContext.current)
    // Make an object which holds the Bin State, pretending to be a ViewModel
    val simViewModel = SimBinViewModel(exampleState)

    JustBinTimeTheme {
        DisplayBin(simViewModel, { }, BinFactory().makeLandfillBinWithColours())
        AddBinButton { }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark() {
    val exampleState = BinFactory().makeUiState()
    BinPhraseGenerator.initArrays(LocalContext.current)

    val simViewModel = SimBinViewModel(exampleState)

    JustBinTimeTheme {
        DisplayBin(simViewModel, {}, BinFactory().makeLandfillBinWithColours())
//        AddBinButton(navController = navController)
//        Row(modifier = Modifier
//            .padding(20.dp)
//            .fillMaxWidth(),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            AddBinButton(navController)
//            Button (onClick = { })
//            { Text("example")}
            // Add ability to restore the default three bins
//            AddDefaultBinsButton(simViewModel)
//        }
    }
}