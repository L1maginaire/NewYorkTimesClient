package com.example.guest.newyorktimesclient.di.scope

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import javax.inject.Scope

/**
 * Created by l1maginaire on 2/28/18.
 */
@Scope
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity