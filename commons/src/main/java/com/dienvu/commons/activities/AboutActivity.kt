package com.dienvu.commons.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import androidx.core.net.toUri
import com.dienvu.commons.R
import com.dienvu.commons.dialogs.ConfirmationAdvancedDialog
import com.dienvu.commons.dialogs.RateStarsDialog
import com.dienvu.commons.extensions.*
import com.dienvu.commons.helpers.*
import com.dienvu.commons.models.FAQItem
import kotlinx.android.synthetic.main.activity_about.*
import java.util.*

class AboutActivity : BaseSimpleActivity()
{
    private var appName = ""
    private var linkColor = 0
    private var firstVersionClickTS = 0L
    private var clicksSinceFirstClick = 0
    private val EASTER_EGG_TIME_LIMIT = 3000L
    private val EASTER_EGG_REQUIRED_CLICKS = 7
    override fun getAppIconIDs() = intent.getIntegerArrayListExtra(APP_ICON_IDS) ?: ArrayList()
    override fun getAppLauncherName() = intent.getStringExtra(APP_LAUNCHER_NAME) ?: ""
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setSupportActionBar(toolbar)
        appName = intent.getStringExtra(APP_NAME) ?: ""
        linkColor = getAdjustedPrimaryColor()

        arrayOf(
            about_faq_icon,
            about_rate_us_icon,
            about_donate_icon,
            about_invite_icon,
            about_contributors_icon,
            about_more_apps_icon,
            about_email_icon,
            about_licenses_icon,
            about_version_icon
        ).forEach {
            it.applyColorFilter(baseConfig.textColor)
        }
        arrayOf(
            about_support,
            about_help_us,
            about_social,
            about_other).forEach {
            it.setTextColor(getAdjustedPrimaryColor())
        }
        arrayOf(
            about_support_holder,
            about_help_us_holder,
            about_social_holder,
            about_other_holder).forEach {
            it.background.applyColorFilter(baseConfig.backgroundColor.getContrastColor())
        }
    }
    override fun onResume()
    {
        super.onResume()
        updateTextColors(about_holder)
        setupEmail()
        //setupFAQ()
        //setupDonate()
        setupMoreApps()
        setupRateUs()
        setupInvite()
        //setupContributors()
        setupLicense()
        setupPlayStore()
        setupFacebook()
        setupTelegram()
        //setupReddit()
        setupVersion()
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left_vector)

        about_faq_holder.beGone()
        about_faq_icon.beGone()
        about_faq.beGone()
        about_contributors_holder.beGone()
        about_contributors_icon.beGone()
        about_contributors.beGone()
        about_reddit_holder.beGone()
        about_reddit.beGone()
        about_reddit_icon.beGone()
        //about_rate_us_holder.beGone()
        //about_rate_us.beGone()
        //about_rate_us_icon.beGone()
        about_more_apps_holder.beGone()
        about_more_apps_icon.beGone()
        about_more_apps.beGone()
        footer.beGone()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        updateMenuItemColors(menu)
        return super.onCreateOptionsMenu(menu)
    }
    private fun setupEmail()
    {
        about_email_holder.setOnClickListener {
            //val msg = "${getString(R.string.before_asking_question_read_faq)}\n\n${getString(R.string.make_sure_latest)}"
            //if (intent.getBooleanExtra(SHOW_FAQ_BEFORE_MAIL, false) && !baseConfig.wasBeforeAskingShown) {
                //baseConfig.wasBeforeAskingShown = true
                //ConfirmationAdvancedDialog(this, msg, 0, R.string.read_faq, R.string.skip) { success ->
                    //if (success) {
                        //about_faq_holder.performClick()
                    //} else {
                        //about_email_holder.performClick()
                    //}
                //}
            //} else {
                val appVersion = String.format(getString(R.string.app_version, intent.getStringExtra(APP_VERSION_NAME)))
                val deviceOS = String.format(getString(R.string.device_os), Build.VERSION.RELEASE)
                val newline = "\n"
                val separator = "------------------------------"
                val body = "$appVersion$newline$deviceOS$newline$separator$newline$newline"

                val address = getString(R.string.my_email)
                val selectorIntent = Intent(ACTION_SENDTO)
                    .setData("mailto:$address".toUri())
                val emailIntent = Intent(ACTION_SEND).apply {
                    putExtra(EXTRA_EMAIL, arrayOf(address))
                    putExtra(EXTRA_SUBJECT, appName)
                    putExtra(EXTRA_TEXT, body)
                    selector = selectorIntent
                }

                try {
                    startActivity(emailIntent)
                } catch (e: ActivityNotFoundException) {
                    toast(R.string.no_app_found)
                } catch (e: Exception) {
                    showErrorToast(e)
                }
            //}
        }
    }
    private fun setupFAQ()
    {
        val faqItems = intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem>
        about_faq_holder.setOnClickListener {
            Intent(applicationContext, FAQActivity::class.java).apply {
                putExtra(APP_ICON_IDS, getAppIconIDs())
                putExtra(APP_LAUNCHER_NAME, getAppLauncherName())
                putExtra(APP_FAQ, faqItems)
                startActivity(this)
            }
        }
    }
    private fun setupDonate()
    {
        if (resources.getBoolean(R.bool.show_donate_in_about)) {
            about_donate_holder.beVisible()
            about_contributors_holder.background = resources.getDrawable(R.drawable.ripple_background, theme)
            about_donate_holder.setOnClickListener {
                launchViewIntent("https://dienvu.com/donate")
            }
        }
        else
        {
            about_donate_holder.beGone()
        }
    }
    private fun setupMoreApps()
    {
        about_more_apps_holder.setOnClickListener{
            launchViewIntent("https://play.google.com")
        }
    }
    private fun setupInvite()
    {
        about_invite_holder.setOnClickListener {
            val text = String.format(getString(R.string.share_text), appName, getStoreUrl())
            Intent().apply {
                action = ACTION_SEND
                putExtra(EXTRA_SUBJECT, appName)
                putExtra(EXTRA_TEXT, text)
                type = "text/plain"
                startActivity(createChooser(this, getString(R.string.invite_via)))
            }
        }
    }
    private fun setupContributors()
    {
        about_contributors_holder.setOnClickListener {
            val intent = Intent(applicationContext, ContributorsActivity::class.java)
            startActivity(intent)
        }
    }
    private fun setupRateUs()
    {
        about_rate_us_holder.setOnClickListener {
            if (baseConfig.wasBeforeRateShown) {
                if (baseConfig.wasAppRated)
                {
                    redirectToRateUs()
                }
                else
                {
                    RateStarsDialog(this)
                }
            }
            else
            {
                baseConfig.wasBeforeRateShown = true
                //val msg = "${getString(R.string.before_rate_read_faq)}\n\n${getString(R.string.make_sure_latest)}"
                //ConfirmationAdvancedDialog(this, msg, 0, R.string.read_faq, R.string.skip) { success ->
                    //if (success) {
                        //about_faq_holder.performClick()
                    //} else {
                        //about_rate_us_holder.performClick()
                    //}
                //}
            }
        }
    }
    private fun setupLicense()
    {
        about_licenses_holder.setOnClickListener{
            Intent(applicationContext, LicenseActivity::class.java).apply {
                putExtra(APP_ICON_IDS, getAppIconIDs())
                putExtra(APP_LAUNCHER_NAME, getAppLauncherName())
                putExtra(APP_LICENSES, intent.getIntExtra(APP_LICENSES, 0))
                startActivity(this)
            }
        }
    }
    private fun setupPlayStore()
    {
        about_google_play_store_holder.setOnClickListener{
            var link = "https://play.google.com/store/apps/details?id=com.dienvu.timemanager.pro"
            try
            {
                packageManager.getPackageInfo("com.android.vending", 0)
                link = "https://play.google.com/store/apps/details?id=com.dienvu.timemanager.pro"
            }
            catch (ignored: Exception)
            {
            }
            launchViewIntent(link)
        }
    }
    private fun setupFacebook()
    {
        about_facebook_holder.setOnClickListener {
            var link = "https://www.facebook.com/102582742547205/posts/pfbid01J2RefLuHY1NKayp3UrkBTQAcZpymMyFiRrJtjjuieYe4cmRX8BCiumEZxGYoW6Ul/"
            try
            {
                packageManager.getPackageInfo("com.facebook.katana", 0)
                link = "https://www.facebook.com/102582742547205/posts/pfbid01J2RefLuHY1NKayp3UrkBTQAcZpymMyFiRrJtjjuieYe4cmRX8BCiumEZxGYoW6Ul/"
            }
            catch (ignored: Exception)
            {
            }
            launchViewIntent(link)
        }
    }
    private fun setupTelegram()
    {
        about_telegram_holder.setOnClickListener {
            var link = "https://t.me/%20WkTj8h_I9r84ZjRl?zdlink=Uo9XRcHoRsba8ZeYRt9dBdHbR6LdSc5jBcrbStDbRcTbSY8i8cblSo8wUo9pOsXbRMLVTN9i8ZeYUc5iRoqsE3OqD3auC3SwN2zSBo8i8c5mS6ba8ZeYDZWsD3GvE30t8drz"
            try {
                packageManager.getPackageInfo("org.telegram.messenger", 0)
                link = "https://t.me/%20WkTj8h_I9r84ZjRl?zdlink=Uo9XRcHoRsba8ZeYRt9dBdHbR6LdSc5jBcrbStDbRcTbSY8i8cblSo8wUo9pOsXbRMLVTN9i8ZeYUc5iRoqsE3OqD3auC3SwN2zSBo8i8c5mS6ba8ZeYDZWsD3GvE30t8drz"
            }
            catch (ignored: Exception)
            {
            }
            launchViewIntent(link)
        }
    }
    private fun setupReddit()
    {
        about_reddit_holder.setOnClickListener {
            launchViewIntent("https://www.reddit.com")
        }
    }
    private fun setupVersion()
    {
        var version = intent.getStringExtra(APP_VERSION_NAME) ?: ""
        if (baseConfig.appId.removeSuffix(".debug").endsWith(".pro"))
        {
            version += " ${getString(R.string.pro)}"
        }
        val fullVersion = String.format(getString(R.string.version_placeholder, version))
        about_version.text = fullVersion
        about_version_holder.setOnClickListener{
            if (firstVersionClickTS == 0L)
            {
                firstVersionClickTS = System.currentTimeMillis()
                Looper.myLooper()?.let { it1 ->
                    Handler(it1).postDelayed({
                        firstVersionClickTS = 0L
                        clicksSinceFirstClick = 0
                    }, EASTER_EGG_TIME_LIMIT)
                }
            }
            clicksSinceFirstClick++
            if (clicksSinceFirstClick >= EASTER_EGG_REQUIRED_CLICKS)
            {
                toast(R.string.hello)
                firstVersionClickTS = 0L
                clicksSinceFirstClick = 0
            }
        }
    }
}
