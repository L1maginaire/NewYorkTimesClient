package com.example.guest.newyorktimesclient.helper

import android.content.Context
import com.example.guest.newyorktimesclient.utils.NetworkChecker
import javax.inject.Inject

open class ImageLoader
@Inject constructor(private val context: Context, private val networkChecker: NetworkChecker){
    open fun loadPics(){
        if (networkChecker.isNetAvailable(context)){
            //load
        }
    }
}