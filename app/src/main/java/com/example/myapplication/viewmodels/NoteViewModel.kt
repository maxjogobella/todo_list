package com.example.myapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.Note
import com.example.myapplication.local.NoteDatabase
import com.example.myapplication.local.dao.NoteDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class NoteViewModel(application: Application, private var database: NoteDao) : AndroidViewModel(application) {

    private var shouldCloseScreen = MutableLiveData<Boolean>()
    private var compositeDisposable = CompositeDisposable()

    fun getShouldCloseScreen() : LiveData<Boolean> = shouldCloseScreen

    init {
        database = NoteDatabase.getInstance(application).noteDao()
    }

    fun getFakeNotes() : List<Note> {
        return database.getFakeNotes()
    }

    fun getJustOneNote(note : Note) : Note = database.getJustOneNote(note.id)

    fun saveNote(note: Note) {
        val disposable = database.add(note)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d("NoteViewModel", "subscribe")
                    shouldCloseScreen.value = true
                }, {
                    Log.d("NoteViewModel", it.message.toString())
                }
            )

        compositeDisposable.add(disposable)
    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}