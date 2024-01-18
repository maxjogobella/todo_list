package com.example.myapplication.viewmodels


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.myapplication.model.Note
import com.example.myapplication.local.NoteDatabase
import com.example.myapplication.local.dao.NoteDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database : NoteDao
    private val compositeDisposable = CompositeDisposable()
    private val errorMessage = MutableLiveData<String>()

    fun getNotes() : LiveData<List<Note>> {
        return database.getNotes()
    }

    fun getErrorMessage() : LiveData<String> = errorMessage

    fun getSortedNotes() : LiveData<List<Note>> {
        return getNotes().map { notes -> notes.sortedByDescending { it.priority }}
    }

    init {
        database = NoteDatabase.getInstance(application).noteDao()
    }

    fun remove(note : Note) {
        val disposable = database.remove(note.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Log.d("MainViewModel", "Removed: " + note.id)
            }, {
                errorMessage.value = it.message.toString()
            })
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}