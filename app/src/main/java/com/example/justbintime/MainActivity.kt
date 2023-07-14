package com.example.justbintime

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.justbintime.data.BinFactory
import com.example.justbintime.data.BinPhraseGenerator
import com.example.justbintime.screen.AddBinButton
import com.example.justbintime.screen.AddBinScreen
import com.example.justbintime.screen.DisplayBin
import com.example.justbintime.screen.EditBinScreen
import com.example.justbintime.screen.ModifyBinScreen
import com.example.justbintime.screen.ViewBinScreen
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

                    val nhc = rememberNavController()

                    // Display the bin state
                    ViewBinScreen(viewModel, nhc)

                    Scaffold { paddingValues ->
                        NavHost(nhc, startDestination = BinScreen.ViewBins.name, modifier = Modifier.padding(paddingValues)) {
                            composable(route = BinScreen.ViewBins.name) {
                                ViewBinScreen(viewModel, nhc)
                            }
                            composable(route = BinScreen.AddBin.name) {
                                AddBinScreen(viewModel, nhc)
                            }
                            composable(route = BinScreen.EditBin.name) {
                                val bin = viewModel.getVisibleBin()
                                bin?.let { EditBinScreen(viewModel, nhc, it) }
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
    val navController = rememberNavController()
    // Make an object which holds the Bin State, pretending to be a ViewModel
    val simViewModel = SimBinViewModel(exampleState)

    JustBinTimeTheme {
        DisplayBin(simViewModel, navController, BinFactory().makeLandfillBinWithColours())
        AddBinButton(navController)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark() {
    val exampleState = BinFactory().makeUiState()
    BinPhraseGenerator.initArrays(LocalContext.current)

    val navController = rememberNavController()
    val simViewModel = SimBinViewModel(exampleState)

    JustBinTimeTheme {
        DisplayBin(simViewModel, navController, BinFactory().makeLandfillBinWithColours())
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