package com.example.justbintime

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
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
import com.example.justbintime.viewmodel.SimBinViewModel


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
                    val (topBarTitle, setTopBarTitle) = remember { mutableStateOf("My Bins") }
                    var deleteActionVisible by remember { mutableStateOf(false) }

                    val navigateToAddBin = {
                        Log.d("BinNavigation", "Navigating to AddBinScreen")
                        navController.navigate(BinScreen.AddBin.name)
                    }
                    val navigateToEditBin = { bin: DisplayableBin ->
                        Log.d("BinNavigation", "Navigating to AddBinScreen")
                        viewModel.setVisibleBin(bin)
                        navController.navigate(BinScreen.EditBin.name)
                    }

                    val navigateUp = { currScreen: String ->
                        Log.d("BinNavigation", "Navigating up from $currScreen")
                        navController.navigateUp()
                    }

                    val (topBarDeleteAction, setTopBarDeleteAction) = remember { mutableStateOf({ }) }

                    Scaffold (
                        topBar = {
                            TopAppBar(
                                title = { Text(topBarTitle) },
                                actions = {
                                    if (deleteActionVisible) {
                                        IconButton(onClick = topBarDeleteAction,) {
                                            Icon(Icons.Filled.Delete, "Delete this bin")
                                        }
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController,
                            startDestination = BinScreen.ViewBins.name,
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable(route = BinScreen.ViewBins.name) {
                                ViewBinsScreen(viewModel, navigateToAddBin, navigateToEditBin, setTopBarTitle)
                            }
                            composable(route = BinScreen.AddBin.name) {
                                AddBinScreen(viewModel, navigateUp, setTopBarTitle)
                            }
                            composable(route = BinScreen.EditBin.name) {
                                val bin = viewModel.getVisibleBin()
                                bin?.let {
                                    EditBinScreen(
                                        viewModel, navigateUp,
                                        it, setTopBarTitle,
                                        { deleteActionVisible = true },
                                        setTopBarDeleteAction
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
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