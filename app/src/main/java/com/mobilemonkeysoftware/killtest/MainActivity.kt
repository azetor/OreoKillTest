package com.mobilemonkeysoftware.killtest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ScrollView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*

/**
 * Created by AR on 09/01/2018.
 */
class MainActivity : AppCompatActivity() {

    private companion object {

        const val EXTRA_START = "extra_start"
        const val EXTRA_TASK_MAIN_APP = "EXTRA_TASK_MAIN_APP"
        const val EXTRA_START_TASK_JOB_SERVICE = "EXTRA_START_TASK_JOB_SERVICE"
        const val EXTRA_TASK_FOREGROUND_JOB_SERVICE = "EXTRA_TASK_FOREGROUND_JOB_SERVICE"
        const val EXTRA_TASK_PROCESS_JOB_SERVICE = "EXTRA_TASK_PROCESS_JOB_SERVICE"
        const val INFO_FORMAT = "%s: %s"
        const val NEW_LINE_FORMAT = "\n%s"
    }

    private val disposables = mutableListOf<Disposable>()

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString(EXTRA_START, start.text.toString())
        outState?.putString(EXTRA_TASK_MAIN_APP, main_app.text.toString())
        outState?.putString(EXTRA_START_TASK_JOB_SERVICE, job_service.text.toString())
        outState?.putString(EXTRA_TASK_FOREGROUND_JOB_SERVICE, foreground_job_service.text.toString())
        outState?.putString(EXTRA_TASK_PROCESS_JOB_SERVICE, process_job_service.text.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            start.text = savedInstanceState.getString(EXTRA_START, "")
            main_app.text = savedInstanceState.getString(EXTRA_TASK_MAIN_APP, "")
            job_service.text = savedInstanceState.getString(EXTRA_START_TASK_JOB_SERVICE, "")
            foreground_job_service.text = savedInstanceState.getString(EXTRA_TASK_FOREGROUND_JOB_SERVICE, "")
            process_job_service.text = savedInstanceState.getString(EXTRA_TASK_PROCESS_JOB_SERVICE, "")
        } else {
            start.text = INFO_FORMAT.format("START", Calendar.getInstance().time.toString())
        }
    }

    override fun onResume() {
        super.onResume()

        Timber.d("onResume")
        Timber.d("Disposables ${disposables.size}")
        disposables
                .add(
                        Task
                                .taskMainAppSubject
                                .map { INFO_FORMAT.format(it, Calendar.getInstance().time.toString()) }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { main_app.text = it })

        disposables
                .add(
                        Task
                                .taskJobServiceSubject
                                .map { INFO_FORMAT.format(it, Calendar.getInstance().time.toString()) }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { job_service.text = it })
        disposables
                .add(
                        Task
                                .taskForegroundJobServiceSubject
                                .map { INFO_FORMAT.format(it, Calendar.getInstance().time.toString()) }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { foreground_job_service.text = it })
        disposables
                .add(
                        Task
                                .taskProcessJobServiceSubject
                                .map { INFO_FORMAT.format(it, Calendar.getInstance().time.toString()) }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { process_job_service.text = it })
        disposables
                .add(
                        Task
                                .logSubject
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { log(it) })
        Timber.d("Disposables after start ${disposables.size}")

        (application as MainApp).runTasks()
    }

    private fun log(message: String) {

        log.append(NEW_LINE_FORMAT.format(message))
        scroll.fullScroll(ScrollView.FOCUS_DOWN)
    }

    override fun onPause() {
        super.onPause()

        Timber.d("onPause")
        Timber.d("Disposables to clean ${disposables.size}")
        disposables.forEach { it.dispose() }
        disposables.clear()
        Timber.d("Disposables after clean ${disposables.size}")
    }

}
