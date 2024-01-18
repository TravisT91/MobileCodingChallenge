package com.hopskipdrive.mobileCodingChallenge.myrides.presentation.dialog

import android.R.attr
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.content.ContextCompat
import com.hopskipdrive.mobileCodingChallenge.R
import com.hopskipdrive.mobileCodingChallenge.databinding.DialogHsdDialogBinding


class HSDDialog private constructor(context: Context, themeId: Int) : Dialog(context, themeId) {
    companion object{
        const val DIALOG_BUTTON_LIMIT = 6
    }
    class Builder(private val context: Context) {
        private var title: String? = null
        private var message: String? = null
        private var buttons = mutableListOf<HSDDialogButton>()

        fun setTitle(title: String) : Builder{
            this.title = title
            return this
        }

        fun setMessage(message: String) : Builder{
            this.message = message
            return this
        }

        fun addButton(hsdDialogButton: HSDDialogButton) : Builder{
            if (buttons.size > DIALOG_BUTTON_LIMIT - 1 ) {
                throw IllegalStateException("HSDDialog button limit exceeded. Max = $DIALOG_BUTTON_LIMIT")
            } else {
                buttons.add(hsdDialogButton)
            }
            return this
        }

        fun removeButton(hsdDialogButton: HSDDialogButton) : Builder{
            buttons.remove(hsdDialogButton)
            return this
        }

        fun removeAllButtons() : Builder {
            buttons.clear()
            return this
        }

        fun build() : HSDDialog {
            val dialog = HSDDialog(context, android.R.style.ThemeOverlay)
            val dialogView = DialogHsdDialogBinding.inflate(LayoutInflater.from(context))
            dialogView.apply {
                closeButton.setOnClickListener {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                }
                title.text = this@Builder.title
                message.text = this@Builder.message
                buttons.forEachIndexed { index, hsdButton ->
                    val buttonBackground = when (hsdButton.hsdButtonType){
                        HSDButtonType.FILLED -> R.drawable.pill_background_primary
                        HSDButtonType.OUTLINED -> R.drawable.pill_background_outlined_primary
                        HSDButtonType.TEXT_ONLY -> null
                    }
                    val button = Button(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT)
                        text = hsdButton.text
                        setTextColor(hsdButton.textColor)
                        setOnClickListener {
                            hsdButton.onClick()
                        }
                        background = if (buttonBackground == null) {
                            null
                        }
                        else {
                            ContextCompat.getDrawable(context, buttonBackground)
                        }
                    }
                    buttonContainer.addView(button)
                    if (index != buttons.size - 1) {
                        buttonContainer.addView(Space(context).apply {
                            val scale = context.resources.displayMetrics.density
                            val width = (1 * scale + 0.5f).toInt()
                            val height = (16 * scale + 0.5f).toInt()
                            layoutParams = LinearLayout.LayoutParams(width, height)
                        })
                    }
                }
            }
            dialog.apply {
                setContentView(dialogView.root)
            }
            return dialog
        }
    }
    data class HSDDialogButton(val text: String, val textColor: Int, val onClick: () -> Unit, val hsdButtonType: HSDButtonType)
    enum class HSDButtonType {
        FILLED,
        OUTLINED,
        TEXT_ONLY
    }
}