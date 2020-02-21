package com.computerwizards.listnote

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.computerwizards.listnote.database.NoteDatabase
import java.io.File
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "note-database"

class NoteRepository private constructor(context: Context) {

    private val database: NoteDatabase = Room.databaseBuilder(
        context.applicationContext,
        NoteDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val noteDoa = database.noteDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getNotes(): LiveData<List<Note>> = noteDoa.getNotes()

    fun getNote(id: UUID): LiveData<Note?> = noteDoa.getNote(id)

    fun getPhotoFile(): File = File(filesDir, "image")

    fun updateNote(note: Note) {
        executor.execute {
            noteDoa.updateNote(note)
        }
    }

    fun addNote(note: Note) {
        executor.execute {
            noteDoa.addNote(note)
        }
    }

    companion object {
        private var INSTANCE: NoteRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE =
                    NoteRepository(context)
            }
        }

        fun get(): NoteRepository {
            return INSTANCE
                ?:
                    throw IllegalStateException("CrimeRepository must be initialized")
        }

    }



}