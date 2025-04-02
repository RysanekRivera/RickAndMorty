package com.example.navigation_core.utils

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavHostController

fun NavHostController.goBack(){
    if (currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED){
        popBackStack()
    }
}