package com.example.dialogstest

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dialogstest.databinding.ActivityLevel1Binding
import com.example.dialogstest.entities.AvailableVolumeValues
import kotlin.properties.Delegates

class DialogsLevel1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLevel1Binding

    private var volume by Delegates.notNull<Int>()
    private var color by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel1Binding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.showDefaultAlertDialogButton.setOnClickListener {
            showAlertDialog()
        }

        binding.showSingleChoiceAlertDialogButton.setOnClickListener {
            showSingleChoiceAlertDialog()
        }

        binding.showSingleChoiceWithConfirmationAlertDialogButton.setOnClickListener {
            showSingleChoiceWithConfirmationAlertDialog()
        }

        binding.showMultipleChoiceAlertDialogButton.setOnClickListener {
            showMultipleChoiceAlertDialog()
        }

        binding.showMultipleChoiceWithConfirmationAlertDialogButton.setOnClickListener {
            showMultipleChoiceWithConfirmationAlertDialog()
        }

        volume = savedInstanceState?.getInt(KEY_VOLUME) ?: 50
        color = savedInstanceState?.getInt(KEY_COLOR) ?: Color.RED

        updateUi()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_VOLUME, volume)
        outState.putInt(KEY_COLOR, color)
    }

    private fun showToast(@StringRes resString: Int) {
        Toast.makeText(this, resString, Toast.LENGTH_SHORT).show()
    }

    private fun showAlertDialog() {
        val listener = DialogInterface.OnClickListener { _, which ->
            when (which) {
                AlertDialog.BUTTON_POSITIVE -> showToast(R.string.uninstall_confirmed)
                AlertDialog.BUTTON_NEGATIVE -> showToast(R.string.uninstall_rejected)
                AlertDialog.BUTTON_NEUTRAL -> showToast(R.string.uninstall_ignored)
            }
        }
        val dialog = AlertDialog.Builder(this)
        dialog
            .setCancelable(true)
            .setTitle(R.string.default_alert_title)
            .setIcon(R.mipmap.ic_launcher_round)
            .setMessage(R.string.default_alert_message)
            .setPositiveButton(R.string.action_yes, listener)
            .setNegativeButton(R.string.action_no, listener)
            .setNeutralButton(R.string.action_ignore, listener)
            .setOnCancelListener {
                showToast(R.string.dialog_cancelled)
            }
            .create()
        dialog.show()
    }

    private fun showSingleChoiceAlertDialog() {
        val availableVolume = AvailableVolumeValues.createVolumeValues(volume)
        val volumeList = availableVolume.values
            .map {
                getString(R.string.volume_description, it)
            }.toTypedArray()
        val currentIndex = availableVolume.currentIndex

        val dialog = AlertDialog.Builder(this)
        dialog
            .setTitle(getString(R.string.volume_setup))
            .setSingleChoiceItems(volumeList, currentIndex) { dialog, which ->
                volume = availableVolume.values[which]
                updateUi()
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    private fun showSingleChoiceWithConfirmationAlertDialog() {
        val availableVolume = AvailableVolumeValues.createVolumeValues(volume)
        val volumeList = availableVolume.values
            .map {
                getString(R.string.volume_description, it)
            }.toTypedArray()
        val currentIndex = availableVolume.currentIndex

        val dialog = AlertDialog.Builder(this)
        dialog
            .setTitle(getString(R.string.volume_setup))
            .setSingleChoiceItems(volumeList, currentIndex, null)
            .setPositiveButton(R.string.action_confirm) { dialog, _ ->
                val index = (dialog as AlertDialog).listView.checkedItemPosition
                volume = availableVolume.values[index]
                updateUi()
            }
            .create()
        dialog.show()

    }

    private fun showMultipleChoiceAlertDialog() {
        val arrayNameColors = resources.getStringArray(
            R.array.colors
        )
        val colorComponents = mutableListOf(
            Color.red(this.color),
            Color.green(this.color),
            Color.blue(this.color)
        )
        val checkColors = colorComponents
            .map {
                it > 0
            }.toBooleanArray()

        val dialog = AlertDialog.Builder(this)
        dialog
            .setTitle(R.string.volume_setup)
            .setMultiChoiceItems(arrayNameColors, checkColors) { dialog, which, isChecked ->
                colorComponents[which] = if (isChecked) 255 else 0
                this.color = Color.rgb(
                    colorComponents[0],
                    colorComponents[1],
                    colorComponents[2]
                )
                updateUi()
            }
            .setPositiveButton(R.string.action_close, null)
            .create()
        dialog.show()
    }

    private fun showMultipleChoiceWithConfirmationAlertDialog() {
        val listNameColors = resources.getStringArray(R.array.colors)
        var color = this.color
        val colorComponents = mutableListOf(
            Color.red(this.color),
            Color.green(this.color),
            Color.blue(this.color)
        )
        val checkColors = colorComponents
            .map {
                it > 0
            }.toBooleanArray()

        val dialog = AlertDialog.Builder(this)
        dialog
            .setTitle(R.string.volume_setup)
            .setMultiChoiceItems(listNameColors, checkColors) { dialog, which, isChecked ->
                colorComponents[which] = if (isChecked) 255 else 0
                color = Color.rgb(
                    colorComponents[0],
                    colorComponents[1],
                    colorComponents[2]
                )
            }
            .setPositiveButton(R.string.action_confirm) { dialog, which ->
                this.color = color
                updateUi()
            }
            .create()
        dialog.show()
    }

    private fun updateUi() {
        binding.currentVolumeTextView.text = getString(R.string.current_volume, volume)
        binding.colorView.setBackgroundColor(color)
    }


    companion object {
        @JvmStatic
        private val TAG = DialogsLevel1Activity::class.java.simpleName

        @JvmStatic
        private val KEY_VOLUME = "KEY_VOLUME"

        @JvmStatic
        private val KEY_COLOR = "KEY_COLOR"
    }


}