package com.rysanek.common_ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rysanek.common_ui.uievents.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {

    val events = Channel<UiEvent>()

    fun showSnackBar(message: String) = viewModelScope.launch {
        events.send(UiEvent.ShowSnackBar(message))
    }

}