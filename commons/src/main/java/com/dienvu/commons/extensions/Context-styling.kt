package com.dienvu.commons.extensions

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.view.ViewGroup
import androidx.loader.content.CursorLoader
import com.dienvu.commons.R
import com.dienvu.commons.helpers.*
import com.dienvu.commons.models.SharedTheme
import com.dienvu.commons.views.*


fun Context.getProperTextColor() = if (baseConfig.isUsingSystemTheme) {
    resources.getColor(R.color.you_neutral_text_color, theme)
} else {
    baseConfig.textColor
}
fun Context.getProperBackgroundColor() = if (baseConfig.isUsingSystemTheme) {
    resources.getColor(R.color.you_background_color, theme)
} else {
    baseConfig.backgroundColor
}
fun Context.getProperPrimaryColor() = when {
    baseConfig.isUsingSystemTheme -> resources.getColor(R.color.you_primary_color, theme)
    isWhiteTheme() || isBlackAndWhiteTheme() -> baseConfig.accentColor
    else -> baseConfig.primaryColor
}
fun Context.updateTextColors(viewGroup: ViewGroup) {
    val textColor = when {
        baseConfig.isUsingSystemTheme -> getProperTextColor()
        else -> baseConfig.textColor
    }

    val backgroundColor = baseConfig.backgroundColor
    val accentColor = when {
        isWhiteTheme() || isBlackAndWhiteTheme() -> baseConfig.accentColor
        else -> getProperPrimaryColor()
    }

    val cnt = viewGroup.childCount
    (0 until cnt).map { viewGroup.getChildAt(it) }.forEach {
        when (it) {
            is MyTextView -> it.setColors(textColor, accentColor, backgroundColor)
            is MyAppCompatSpinner -> it.setColors(textColor, accentColor, backgroundColor)
            is MySwitchCompat -> it.setColors(textColor, accentColor, backgroundColor)
            is MyCompatRadioButton -> it.setColors(textColor, accentColor, backgroundColor)
            is MyAppCompatCheckbox -> it.setColors(textColor, accentColor, backgroundColor)
            is MyEditText -> it.setColors(textColor, accentColor, backgroundColor)
            is MyAutoCompleteTextView -> it.setColors(textColor, accentColor, backgroundColor)
            is MyFloatingActionButton -> it.setColors(textColor, accentColor, backgroundColor)
            is MySeekBar -> it.setColors(textColor, accentColor, backgroundColor)
            is MyButton -> it.setColors(textColor, accentColor, backgroundColor)
            is MyTextInputLayout -> it.setColors(textColor, accentColor, backgroundColor)
            is ViewGroup -> updateTextColors(it)
        }
    }
}
fun Context.updateTextColors(viewGroup: ViewGroup, tmpTextColor: Int = 0, tmpAccentColor: Int = 0) {
    val textColor = if (tmpTextColor == 0) baseConfig.textColor else tmpTextColor
    val backgroundColor = baseConfig.backgroundColor
    val accentColor = if (tmpAccentColor == 0) {
        when {
            isWhiteTheme() || isBlackAndWhiteTheme() -> baseConfig.accentColor
            else -> baseConfig.primaryColor
        }
    } else {
        tmpAccentColor
    }

    val cnt = viewGroup.childCount
    (0 until cnt).map { viewGroup.getChildAt(it) }.forEach {
        when (it) {
            is MyTextView -> it.setColors(textColor, accentColor, backgroundColor)
            is MyAppCompatSpinner -> it.setColors(textColor, accentColor, backgroundColor)
            is MySwitchCompat -> it.setColors(textColor, accentColor, backgroundColor)
            is MyCompatRadioButton -> it.setColors(textColor, accentColor, backgroundColor)
            is MyAppCompatCheckbox -> it.setColors(textColor, accentColor, backgroundColor)
            is MyEditText -> it.setColors(textColor, accentColor, backgroundColor)
            is MyAutoCompleteTextView -> it.setColors(textColor, accentColor, backgroundColor)
            is MyFloatingActionButton -> it.setColors(textColor, accentColor, backgroundColor)
            is MySeekBar -> it.setColors(textColor, accentColor, backgroundColor)
            is MyButton -> it.setColors(textColor, accentColor, backgroundColor)
            is MyTextInputLayout -> it.setColors(textColor, accentColor, backgroundColor)
            is ViewGroup -> updateTextColors(it, textColor, accentColor)
        }
    }
}
fun Context.getPopupMenuTheme(): Int {
    return if (isSPlus() && baseConfig.isUsingSystemTheme) {
        R.style.AppTheme_YouPopupMenuStyle
    } else if (isWhiteTheme()) {
        R.style.AppTheme_PopupMenuLightStyle
    } else {
        R.style.AppTheme_PopupMenuDarkStyle
    }
}
fun Context.getBottomNavigationBackgroundColor(): Int {
    val baseColor = baseConfig.backgroundColor
    val bottomColor = when {
        baseConfig.isUsingSystemTheme -> resources.getColor(R.color.you_status_bar_color, theme)
        baseColor == Color.WHITE -> resources.getColor(R.color.bottom_tabs_light_background, theme)
        else -> baseConfig.backgroundColor.lightenColor(4)
    }
    return bottomColor
}
fun Context.getProperStatusBarColor() = when {
    baseConfig.isUsingSystemTheme -> resources.getColor(R.color.you_status_bar_color, theme)
    else -> baseConfig.primaryColor
}
fun Context.getSharedTheme(callback: (sharedTheme: SharedTheme?) -> Unit) {
    if (!isThankYouInstalled()) {
        callback(null)
    } else {
        val cursorLoader = getMyContentProviderCursorLoader()
        ensureBackgroundThread {
            callback(getSharedThemeSync(cursorLoader))
        }
    }
}

fun Context.getSharedThemeSync(cursorLoader: CursorLoader): SharedTheme? {
    val cursor = cursorLoader.loadInBackground()
    cursor?.use {
        if (cursor.moveToFirst()) {
            try {
                val textColor = cursor.getIntValue(MyContentProvider.COL_TEXT_COLOR)
                val backgroundColor = cursor.getIntValue(MyContentProvider.COL_BACKGROUND_COLOR)
                val primaryColor = cursor.getIntValue(MyContentProvider.COL_PRIMARY_COLOR)
                val accentColor = cursor.getIntValue(MyContentProvider.COL_ACCENT_COLOR)
                val appIconColor = cursor.getIntValue(MyContentProvider.COL_APP_ICON_COLOR)
                val navigationBarColor = cursor.getIntValueOrNull(MyContentProvider.COL_NAVIGATION_BAR_COLOR) ?: INVALID_NAVIGATION_BAR_COLOR
                val lastUpdatedTS = cursor.getIntValue(MyContentProvider.COL_LAST_UPDATED_TS)
                return SharedTheme(textColor, backgroundColor, primaryColor, appIconColor, navigationBarColor, lastUpdatedTS, accentColor)
            } catch (e: Exception) {
            }
        }
    }
    return null
}

fun Context.isBlackAndWhiteTheme() = baseConfig.textColor == Color.WHITE
            && baseConfig.primaryColor == Color.BLACK
            && baseConfig.backgroundColor == Color.BLACK

fun Context.isWhiteTheme() = baseConfig.textColor == DARK_GREY
            && baseConfig.primaryColor == Color.WHITE
            && baseConfig.backgroundColor == Color.WHITE

fun Context.isUsingSystemDarkTheme() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_YES != 0

fun Context.getAdjustedPrimaryColor() = when {
    isWhiteTheme() || isBlackAndWhiteTheme() -> baseConfig.accentColor
    else -> baseConfig.primaryColor
}
fun Context.getTimePickerDialogTheme() = when {
    baseConfig.isUsingSystemTheme -> if (isUsingSystemDarkTheme()) {
        R.style.MyTimePickerMaterialTheme_Dark
    } else {
        R.style.MyDateTimePickerMaterialTheme
    }
    baseConfig.backgroundColor.getContrastColor() == Color.WHITE -> R.style.MyDialogTheme_Dark
    else -> R.style.MyDialogTheme
}




