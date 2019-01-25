package com.example.guest.newyorktimesclient.di.modules

import com.example.guest.newyorktimesclient.helper.rx.schedulers.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NormalSchedulers : SchedulerProvider {
    override fun io(): Scheduler = Schedulers.io()
    override fun calc(): Scheduler = Schedulers.computation()
    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
    override fun new(): Scheduler = Schedulers.newThread()
}