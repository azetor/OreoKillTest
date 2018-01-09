package com.mobilemonkeysoftware.killtest

import android.app.Application
import timber.log.Timber

/**
 * Created by AR on 09/01/2018.
 */
class MainApp : Application() {

    private var start = true

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

    fun runTasks() {

        if (start) {
            start = false
            Task.createTaskMainApp()
            TaskJobService.start(this)
        }
    }

}