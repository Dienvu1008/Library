package com.dienvu.commons.activities

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dienvu.commons.R
import com.dienvu.commons.extensions.*
import com.dienvu.commons.helpers.SIDELOADING_TRUE
import com.dienvu.commons.helpers.SIDELOADING_UNCHECKED

abstract class BaseSplashActivity : AppCompatActivity()
{
    abstract fun initActivity()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (baseConfig.appSideloadingStatus == SIDELOADING_UNCHECKED)
        {
            if (checkAppSideloading())
            {
                return
            }
        }
        else if (baseConfig.appSideloadingStatus == SIDELOADING_TRUE)
        {
            showSideloadingDialog()
            return
        }
        baseConfig.apply {
            if (isUsingAutoTheme) {
                val isUsingSystemDarkTheme = isUsingSystemDarkTheme()
                isUsingSharedTheme = false
                textColor = resources.getColor(
                    if (isUsingSystemDarkTheme) R.color.theme_dark_text_color
                    else R.color.theme_light_text_color)
                backgroundColor = resources.getColor(
                    if (isUsingSystemDarkTheme) R.color.theme_dark_background_color
                    else R.color.theme_light_background_color)
                navigationBarColor =
                    if (isUsingSystemDarkTheme) Color.BLACK
                    else -2
            }
        }
        //if (!baseConfig.isUsingAutoTheme && isThankYouInstalled())
        if (!baseConfig.isUsingAutoTheme)
        {
            getSharedTheme {
                if (it != null) {
                    baseConfig.apply {
                        wasSharedThemeForced = true
                        isUsingSharedTheme = true
                        wasSharedThemeEverActivated = true

                        textColor = it.textColor
                        backgroundColor = it.backgroundColor
                        primaryColor = it.primaryColor
                        navigationBarColor = it.navigationBarColor
                        accentColor = it.accentColor
                    }
                    if (baseConfig.appIconColor != it.appIconColor)
                    {
                        baseConfig.appIconColor = it.appIconColor
                        checkAppIconColor()
                    }
                }
                initActivity()
            }
        }
        else
        {
            initActivity()
        }
    }
}
