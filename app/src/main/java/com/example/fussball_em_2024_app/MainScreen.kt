package com.example.fussball_em_2024_app

import MainViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MatchScreen(modifier:Modifier=Modifier){
    val matchViewModel:MainViewModel= viewModel()
    val viewState by matchViewModel.matchState
    Box(modifier = Modifier.fillMaxSize()){
        when{
            viewState.loading->{
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }

            viewState.error !=null ->{
                Text("ERROR OCCURRED")
            }
            else ->{
                //Display Matches
            }
        }
    }
}