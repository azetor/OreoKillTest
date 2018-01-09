package com.mobilemonkeysoftware.killtest

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import timber.log.Timber

/**
 * Created by AR on 09/01/2018.
 */
class TaskForegroundJobService : JobService() {

    companion object {

        private const val ID = 0x0002

        fun start(context: Context) {
            val component = ComponentName(context, TaskForegroundJobService::class.java)
            context
                    .getSystemService(JobScheduler::class.java)
                    .schedule(JobInfo.Builder(ID, component)
                            .apply {
                                setMinimumLatency(1 * 1) // 1 * 1000
                                setOverrideDeadline(1 * 1) // 1 * 1000
                            }
                            .build())
        }
    }

    override fun onCreate() {
        super.onCreate()

        // TODO
    }

    override fun onStopJob(params: JobParameters?): Boolean {

        Timber.d("TaskForegroundJobService onStopJob")
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        Timber.d("TaskForegroundJobService onStartJob")
        Task.createTaskForegroundJobService()
        return true
    }

}