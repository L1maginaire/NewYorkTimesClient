package com.example.guest.newyorktimesclient.helper.rx.schedulers

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun io(): Scheduler
    fun calc(): Scheduler
    fun ui(): Scheduler
}