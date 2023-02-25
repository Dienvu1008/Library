package com.dienvu.commons.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.dienvu.commons.extensions.applyColorFilter
import com.dienvu.commons.extensions.getContrastColor
import kotlinx.android.synthetic.main.dialog_color_picker.view.*

class MyFloatingActionButton : FloatingActionButton {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    fun setColors(textColor: Int, accentColor: Int, backgroundColor: Int) {
        backgroundTintList = ColorStateList.valueOf(accentColor)
        applyColorFilter(accentColor.getContrastColor())
    }

}
