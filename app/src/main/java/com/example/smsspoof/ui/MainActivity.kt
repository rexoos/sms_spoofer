package com.example.smsspoof.ui

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.smsspoof.R
import com.example.smsspoof.data.SenderRepository
import com.example.smsspoof.model.Sender
import com.example.smsspoof.util.Constants
import com.example.smsspoof.util.PrefsUtils
import com.example.smsspoof.viewmodel.SenderViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: SenderViewModel
    private lateinit var senderAdapter: SenderAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var switchModuleEnabled: Switch
    private lateinit var textViewActiveSender: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[SenderViewModel::class.java]

        setupToolbar()
        setupViews()
        setupRecyclerView()
        setupFab()
        setupModuleToggle()
        observeSenders()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.app_name)
    }

    private fun setupViews() {
        recyclerView = findViewById(R.id.recyclerViewSenders)
        switchModuleEnabled = findViewById(R.id.switchModuleEnabled)
        textViewActiveSender = findViewById(R.id.textViewActiveSender)
    }

    private fun setupRecyclerView() {
        senderAdapter = SenderAdapter { sender ->
            showSenderOptionsDialog(sender)
        }
        recyclerView.adapter = senderAdapter

        // Enable swipe to delete
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val sender = senderAdapter.getSenderAt(position)
                sender?.let {
                    viewModel.deleteSender(it.id)
                    showUndoSnackbar(it)
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { showAddSenderDialog() }
    }

    private fun setupModuleToggle() {
        // Load saved state
        val isEnabled = PrefsUtils.getBoolean(this, Constants.KEY_MODULE_ENABLED, false)
        switchModuleEnabled.isChecked = isEnabled

        switchModuleEnabled.setOnCheckedChangeListener { _, isChecked ->
            PrefsUtils.putBoolean(this, Constants.KEY_MODULE_ENABLED, isChecked)
            // TODO: Notify Xposed module of state change
            val message = if (isChecked) getString(R.string.module_enabled) else getString(R.string.module_disabled)
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observeSenders() {
        viewModel.allSenders.observe(this) { senders ->
            senderAdapter.submitList(senders)
            updateActiveSenderDisplay()
        }
    }

    private fun updateActiveSenderDisplay() {
        val activeSenderId = PrefsUtils.getString(this, Constants.KEY_ACTIVE_SENDER_ID, Constants.DEFAULT_SENDER_ID)
        val sender = senderAdapter.getSenderById(activeSenderId.toLongOrNull() ?: -1L)
        val displayText = if (sender != null) sender.name else activeSenderId
        textViewActiveSender.text = getString(R.string.active_sender_format, displayText)
    }

    private fun showAddSenderDialog() {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_sender, null)
        dialog.setView(dialogView)

        val editTextSenderName = dialogView.findViewById<android.widget.EditText>(R.id.editTextSenderName)
        val editTextSenderId = dialogView.findViewById<android.widget.EditText>(R.id.editTextSenderId)
        val buttonAdd = dialogView.findViewById<android.widget.Button>(R.id.buttonAdd)
        val buttonCancel = dialogView.findViewById<android.widget.Button>(R.id.buttonCancel)

        val alertDialog = dialog.create()

        buttonAdd.setOnClickListener {
            val name = editTextSenderName.text.toString().trim()
            val senderId = editTextSenderId.text.toString().trim()
            if (name.isNotEmpty() && senderId.isNotEmpty()) {
                val newSender = Sender(0, name, senderId, true)
                viewModel.insertSender(newSender)
                alertDialog.dismiss()
            } else {
                editTextSenderName.error = if (name.isEmpty()) getString(R.string.sender_name_required) else null
                editTextSenderId.error = if (senderId.isEmpty()) getString(R.string.sender_id_required) else null
            }
        }

        buttonCancel.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
    }

    private fun showSenderOptionsDialog(sender: Sender) {
        val dialog = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_sender, null)
        dialog.setView(dialogView)

        val editTextSenderName = dialogView.findViewById<android.widget.EditText>(R.id.editTextSenderName)
        val editTextSenderId = dialogView.findViewById<android.widget.EditText>(R.id.editTextSenderId)
        val buttonAdd = dialogView.findViewById<android.widget.Button>(R.id.buttonAdd)
        val buttonCancel = dialogView.findViewById<android.widget.Button>(R.id.buttonCancel)

        // Pre-fill with existing values
        editTextSenderName.setText(sender.name)
        editTextSenderId.setText(sender.senderId)
        buttonAdd.text = getString(R.string.update)

        val alertDialog = dialog.create()

        buttonAdd.setOnClickListener {
            val name = editTextSenderName.text.toString().trim()
            val senderId = editTextSenderId.text.toString().trim()
            if (name.isNotEmpty() && senderId.isNotEmpty()) {
                val updatedSender = Sender(sender.id, name, senderId, sender.isEnabled)
                viewModel.updateSender(updatedSender)
                alertDialog.dismiss()
            } else {
                editTextSenderName.error = if (name.isEmpty()) getString(R.string.sender_name_required) else null
                editTextSenderId.error = if (senderId.isEmpty()) getString(R.string.sender_id_required) else null
            }
        }

        buttonCancel.setOnClickListener { alertDialog.dismiss() }

        alertDialog.show()
    }

    private fun showUndoSnackbar(deletedSender: Sender) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            getString(R.string.sender_deleted, deletedSender.name),
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction(getString(R.string.undo)) {
            viewModel.insertSender(deletedSender)
        }
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorAccent))
        snackbar.show()
    }
}
