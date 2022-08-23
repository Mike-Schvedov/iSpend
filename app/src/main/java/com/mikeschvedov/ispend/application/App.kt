package com.mikeschvedov.ispend.application

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        res = resources
    }
    companion object{

        private var mInstance: App? = null
        private var res: Resources? = null

        fun getInstance(): App? {
            return mInstance
        }

        fun getRes(): Resources? {
            return res
        }
    }

}