package com.dienvu.commons.views

import android.content.Context
import android.util.AttributeSet
import androidx.biometric.auth.AuthPromptHost
import androidx.constraintlayout.widget.ConstraintLayout
import com.dienvu.commons.extensions.showBiometricPrompt
import com.dienvu.commons.extensions.updateTextColors
import com.dienvu.commons.interfaces.HashListener
import com.dienvu.commons.interfaces.SecurityTab
import kotlinx.android.synthetic.main.tab_biometric_id.view.*

class BiometricIdTab(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs), SecurityTab {
    private lateinit var hashListener: HashListener
    private lateinit var biometricPromptHost: AuthPromptHost

    override fun onFinishInflate() {
        super.onFinishInflate()
        context.updateTextColors(biometric_lock_holder)

        open_biometric_dialog.setOnClickListener {
            biometricPromptHost.activity?.showBiometricPrompt(successCallback = hashListener::receivedHash)
        }
    }

    override fun initTab(
        requiredHash: String,
        listener: HashListener,
        scrollView: MyScrollView,
        biometricPromptHost: AuthPromptHost,
        showBiometricAuthentication: Boolean
    ) {
        this.biometricPromptHost = biometricPromptHost
        hashListener = listener
        if (showBiometricAuthentication) {
            open_biometric_dialog.performClick()
        }
    }

    override fun visibilityChanged(isVisible: Boolean) {}
}
