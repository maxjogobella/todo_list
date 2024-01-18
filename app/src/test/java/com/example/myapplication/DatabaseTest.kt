package com.example.myapplication

import android.app.Application
import com.example.myapplication.local.dao.NoteDao
import com.example.myapplication.model.Note
import com.example.myapplication.viewmodels.MainViewModel
import com.example.myapplication.viewmodels.NoteViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DatabaseTest {

    @Test
    fun testAddNote() {
        val noteDao = mockk<NoteDao>(relaxed = true)

        val application = mockk<Application>()

        val viewModel = NoteViewModel(application, noteDao)

        val note = Note(priority = 1, title = "Test note", text = "This is a test note")

        runBlocking {
            coEvery { noteDao.getFakeNotes() } returns listOf()

            assert(viewModel.getFakeNotes().isEmpty())

            coEvery { noteDao.add(note) } returns Completable.complete()

            viewModel.saveNote(note)

            coEvery { noteDao.getFakeNotes() } returns listOf(note)

            assert(viewModel.getFakeNotes().size == 1)
            assert(viewModel.getFakeNotes()[0] == note)
        }
    }

    @Test
    fun testRemoveNote() {
        val viewModelNote = mockk<NoteViewModel>(relaxed = true)
        val viewModelMain = mockk<MainViewModel>(relaxed = true)

        val note = Note(priority = 1, title = "Test note", text = "This is a test note")
        every { viewModelNote.getJustOneNote(note)} returns note
        viewModelNote.saveNote(note)
        val retrievedNote = viewModelNote.getJustOneNote(note)
        assert(note == retrievedNote)
        verify { viewModelNote.saveNote(note) }

        Thread.sleep(1000)

        val listBeforeDelete = viewModelMain.getNotes().value
        assert(listBeforeDelete?.size == 1)

        every { viewModelMain.remove(note) } just Runs
        viewModelMain.remove(note)

        Thread.sleep(1000)

        val listAfterDelete = viewModelMain.getNotes().value
        assert(listAfterDelete?.size == 0)

    }
}