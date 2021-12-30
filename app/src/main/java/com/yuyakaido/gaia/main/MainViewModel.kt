package com.yuyakaido.gaia.main

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.yuyakaido.gaia.auth.Session
import javax.inject.Inject

class MainViewModel @Inject constructor(
    application: Application
) : ViewModel() {

    val sessions = mutableStateOf(Session.all(application = application))

}