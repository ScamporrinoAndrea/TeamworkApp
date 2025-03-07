package com.example.teammanagement

import android.app.Application

class MyApplication: Application() {
    lateinit var model: Model

    override fun onCreate() {
        super.onCreate()
        model = Model(this)
    }
}