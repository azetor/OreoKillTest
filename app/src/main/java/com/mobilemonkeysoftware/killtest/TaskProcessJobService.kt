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
class TaskProcessJobService : JobService() {

    companion object {

        private const val ID = 0x0003

        fun start(context: Context) {
            val component = ComponentName(context, TaskProcessJobService::class.java)
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

    override fun onStopJob(params: JobParameters?): Boolean {

        Timber.d("TaskProcessJobService onStopJob")
        return true
    }

    override fun onStartJob(params: JobParameters?): Boolean {

        Timber.d("TaskProcessJobService onStartJob")
        Task.createTaskProcessJobService()
        return true
    }

}