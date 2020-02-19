package com.computerwizards.listnote

import android.app.Application

class NoteListApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NoteRepository.initialize(this)
    }

}