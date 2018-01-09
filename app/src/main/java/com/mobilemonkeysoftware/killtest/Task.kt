package com.mobilemonkeysoftware.killtest

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.io.Serializable
import java.util.concurrent.TimeUnit

/**
 * Created by AR on 09/01/2018.
 */
class Task : Serializable {

    companion object {

        private const val TASK_MAIN_APP = "TASK_MAIN_APP"
        private const val TASK_JOB_SERVICE = "TASK_JOB_SERVICE"
        private const val TASK_FOREGROUND_JOB_SERVICE = "TASK_FOREGROUND_JOB_SERVICE"
        private const val TASK_PROCESS_JOB_SERVICE = "TASK_PROCESS_JOB_SERVICE"

        val taskMainAppSubject: Subject<String> = PublishSubject.create()
        val taskJobServiceSubject: Subject<String> = PublishSubject.create()
        val taskForegroundJobServiceSubject: Subject<String> = PublishSubject.create()
        val taskProcessJobServiceSubject: Subject<String> = PublishSubject.create()

        val logSubject: Subject<String> = PublishSubject.create()

        fun createTaskMainApp() {
            create(TASK_MAIN_APP).subscribe(taskMainAppSubject)
        }

        private fun create(name: String): Observable<String> = Observable
                .interval(1, TimeUnit.SECONDS)
                .map { name }
                .doOnSubscribe { log("Task $name doOnSubscribe") }
                .doOnNext { log("Task $name doOnNext") }
                .doOnError { log("Task $name doOnError", it) }
                .doOnComplete { log("Task $name doOnComplete") }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())

        private fun log(message: String, e: Throwable? = null) {
            Timber.d(message)
            e?.let { Timber.e(it, message) }
            logSubject.onNext(message)
        }

        fun createTaskJobService() {
            create(TASK_JOB_SERVICE).subscribe(taskJobServiceSubject)
        }

        fun createTaskForegroundJobService() {
            create(TASK_FOREGROUND_JOB_SERVICE).subscribe(taskForegroundJobServiceSubject)
        }

        fun createTaskProcessJobService() {
            create(TASK_PROCESS_JOB_SERVICE).subscribe(taskProcessJobServiceSubject)
        }
    }

}