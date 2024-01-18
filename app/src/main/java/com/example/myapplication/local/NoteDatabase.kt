package com.example.myapplication.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.model.Note
import com.example.myapplication.local.dao.NoteDao
import com.example.myapplication.util.Constants.DB_NAME

    @Database(
        entities = [Note::class],
        version = 2,
        exportSchema = false)

abstract class NoteDatabase : RoomDatabase() {

    companion object {

        private var database: NoteDatabase? = null
        private val MIGRATION_1_2 : Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE note_table ADD COLUMN title TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getInstance(context: Context): NoteDatabase {
            synchronized(this) {
                database?.let { return it }
                val instance = Room.databaseBuilder(
                    context,
                    NoteDatabase::class.java,
                    DB_NAME
                ).addMigrations(MIGRATION_1_2).build()
                database = instance
                return instance
            }
        }
    }

    abstract fun noteDao() : NoteDao

}