package com.fftools.roomdatabase.ui

import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.fftools.roomdatabase.adapter.NoteAdapter
import com.fftools.roomdatabase.base.BaseActivity
import com.fftools.roomdatabase.databinding.ActivityMainBinding
import com.fftools.roomdatabase.helper.DBHelper
import com.fftools.roomdatabase.model.Note
import com.fftools.roomdatabase.my_interface.NoteItemOnClickListener
import com.fftools.roomdatabase.utils.Constants
import com.fftools.roomdatabase.viewmodel.MainViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(), NoteItemOnClickListener {
    private val viewModel: MainViewModel by viewModel()
    private var adapter: NoteAdapter? = null
    private val dbHelper: DBHelper by inject()

    private val noteLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult? ->
//            viewModel.getNoteList()
        }


    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun initMain() {
        initView()
        initEvent()
        initObservable()
    }


    private fun initObservable() {
        viewModel.noteListDESC.observe(this) {
            adapter?.submitList(it)
        }
    }

    private fun initEvent() {
        binding.lavAdd.setOnClickListener {
            val intent = Intent(this, EditNoteActivity::class.java)
            noteLauncher.launch(intent)
        }
    }

    private fun initView() {
        adapter = NoteAdapter(this)
        binding.rvNote.adapter = adapter
    }

    override fun onClick(note: Note) {
        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra(Constants.EXTRA_NOTE, note)
        noteLauncher.launch(intent)
    }

    override fun onLongClick(note: Note) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Bạn có chắc chắn muốn xóa không ?")
        alertDialog.setPositiveButton("OK") { _, _ ->
            viewModel.deleteNote(note) {
//                viewModel.getNoteList()
            }
        }

        alertDialog.setNegativeButton("Không") { _, _ ->

        }
        val dialog = alertDialog.create()
        dialog.show()
    }
}