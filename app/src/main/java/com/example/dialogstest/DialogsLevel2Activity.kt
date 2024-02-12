package com.example.dialogstest

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dialogstest.databinding.ActivityLevel2Binding
import com.example.dialogstest.databinding.PartVolumeBinding
import com.example.dialogstest.databinding.PartVolumeInputBinding
import com.example.dialogstest.entities.AvailableVolumeValues
import kotlin.properties.Delegates.notNull

class DialogsLevel2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLevel2Binding
    private var volume by notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel2Binding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.showCustomAlertDialogButton.setOnClickListener {
            showCustomAlertDialog()
        }

        binding.showCustomSingleChoiceAlertDialogButton.setOnClickListener {
            showCustomSingleChoiceAlertDialog()
        }

        binding.showInputAlertDialogButton.setOnClickListener {
            showInputAlertDialog()
        }

        volume = savedInstanceState?.getInt(KEY_VOLUME) ?: 50
        updateUi()
    }

    private fun showCustomAlertDialog() {
        val dialogBinding = PartVolumeBinding.inflate(layoutInflater)
        dialogBinding.volumeSeekBar.progress = volume

        val dialog = AlertDialog.Builder(this)
        dialog
            .setCancelable(true)
            .setTitle(R.string.volume_setup)
            .setMessage(R.string.volume_setup_message)
            .setView(dialogBinding.root)
            .setPositiveButton(R.string.action_confirm) { dialog, which ->
                volume = dialogBinding.volumeSeekBar.progress
                updateUi()
            }
            .create()
            .show()
    }

    private fun showCustomSingleChoiceAlertDialog() {
        var volume = this.volume
        val listVolume = AvailableVolumeValues.createVolumeValues(volume)
        val adapter = VolumeAdapter(listVolume.values)
        val dialog = AlertDialog.Builder(this)
        dialog
            .setTitle(R.string.volume_setup)
            .setSingleChoiceItems(adapter, listVolume.currentIndex) { _, which ->
                volume = adapter.getItem(which)
            }
            .setPositiveButton(R.string.action_confirm) { _, _ ->
                this.volume = volume
                updateUi()
            }
            .create()
            .show()
    }

    private fun showInputAlertDialog() {
        val binding = PartVolumeInputBinding.inflate(layoutInflater)
        binding.volumeInputEditText.setText(volume.toString())

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.volume_setup)
            .setPositiveButton(R.string.action_confirm, null)
            .setView(binding.root)
            .create()
        dialog.setOnShowListener {
            binding.volumeInputEditText.requestFocus()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                var enteredText = binding.volumeInputEditText.text.toString()
                if (enteredText.isEmpty()) {
                    binding.volumeInputEditText.error = getString(R.string.empty_value)
                    return@setOnClickListener
                }
                val volume = enteredText.toIntOrNull()
                if (volume == null || volume > 100) {
                    binding.volumeInputEditText.error = getString(R.string.invalid_value)
                    return@setOnClickListener
                } else {
                    this.volume = volume
                    updateUi()
                    dialog.dismiss()
                }
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()

    }

    private fun updateUi() {
        binding.currentVolumeTextView.text = getString(R.string.current_volume, volume)
    }

    companion object {
        const val KEY_VOLUME = "KEY_VOLUME"
    }

}