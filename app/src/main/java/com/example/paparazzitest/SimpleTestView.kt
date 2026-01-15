package com.example.paparazzitest

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.widget.LinearLayout

class SimpleTestView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        orientation = VERTICAL
        setPadding(16, 16, 16, 16)
        
        val textView = TextView(context).apply {
            text = "Paparazzi Context Test View"
            textSize = 18f
            setTextColor(Color.BLACK)
        }
        
        addView(textView)
    }
}
