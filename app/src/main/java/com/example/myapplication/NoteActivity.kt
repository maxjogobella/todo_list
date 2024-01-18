package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.ActivityNoteBinding
import com.example.myapplication.model.Note
import com.example.myapplication.viewmodels.NoteViewModel

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    private lateinit var radioButtonLow: RadioButton
    private lateinit var radioButtonMedium: RadioButton
    private lateinit var radioButtonHigh: RadioButton
    private lateinit var viewModel : NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]
        observe()
        notifyErrorInputText()


        binding.buttonSaveNote.setOnClickListener {
            saveNote()
        }
    }

    private fun notifyErrorInputText() {
        binding.titleInput.doOnTextChanged { text, _, _, _ ->
            if (text!!.length > 100) {
                binding.titleInput.error = "No more!"
            } else if (text.length < 100) {
                binding.titleInput.error = null
            }
        }

        binding.noteInput.doOnTextChanged { text, _, _, _ ->
            if (text!!.length > 500) {
                binding.titleInput.error = "No more!"
            } else if (text.length < 500) {
                binding.titleInput.error = null
            }
        }
    }

    private fun observe() {
        viewModel.getShouldCloseScreen().observe(this) {shouldCloseScreen ->
            if (shouldCloseScreen) {
                finish()
            }
        }
    }

    private fun saveNote() {
        val titleText = binding.titleInput.text.toString().trim()
        val noteText = binding.noteInput.text.toString().trim()
        val priority = getPriority()

        if (priority == -1 || titleText.isEmpty() || noteText.isEmpty()) {
            Toast.makeText(this,
                "You didn't chose the priority or didn't write any text",
                Toast.LENGTH_SHORT).show()

        } else {
           val note = Note(text = noteText, title = titleText, priority = priority)
           viewModel.saveNote(note)
        }
    }

    private fun getPriority(): Int {
        return when {
            radioButtonLow.isChecked -> 1
            radioButtonMedium.isChecked -> 2
            radioButtonHigh.isChecked -> 3
            else -> -1
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, NoteActivity::class.java)
        }
    }

    private fun initViews() {
        radioButtonLow = binding.buttonLow
        radioButtonMedium = binding.buttonMedium
        radioButtonHigh = binding.buttonHigh
    }
}