    package com.example.myapplication.local.dao

    import androidx.lifecycle.LiveData
    import androidx.room.Dao
    import androidx.room.Insert
    import androidx.room.Query
    import com.example.myapplication.model.Note
    import io.reactivex.rxjava3.core.Completable

    @Dao
    interface NoteDao {

        @Query("SELECT * FROM note_table WHERE id =:id")
        fun getJustOneNote(id : Int) : Note

        @Query("SELECT * FROM note_table")
        fun getNotes() : LiveData<List<Note>>

        @Query("SELECT * FROM note_table")
        fun getFakeNotes() : List<Note>

        @Insert
        fun add(note : Note) : Completable

        @Query("DELETE FROM note_table WHERE id =:id")
        fun remove(id : Int) : Completable
    }