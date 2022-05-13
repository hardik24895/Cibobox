package com.eisuchi.eisuchi.uitils

import android.content.Intent
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import com.eisuchi.eisuchi.uitils.TTS
import com.eisuchi.utils.SessionManager
import java.util.*

class MyNotificationListner : NotificationListenerService() {
    private var textToSpeech: TextToSpeech? = null
    private var mContent: String? = null
    lateinit var session: SessionManager
    override fun onCreate() {
        super.onCreate()
        session = SessionManager(applicationContext)
        textToSpeech = TextToSpeech(applicationContext) { textToSpeech!!.language = Locale.ENGLISH }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)
        if (sbn.packageName == "com.cibobox.app") {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mContent = sbn.notification.extras.getString("android.text")
                val ttsIntent = Intent(applicationContext, TTS::class.java)
                val text="1 item(s) of Order #33_1 has been delivered. Good Team Work"
               val str = mContent?.replace("_", "underscore");
                ttsIntent.putExtra("text", str)
               // ttsIntent.putExtra("text", "1 item(s) of Order #33_1 has been delivered. Good Team Work")
               if (session.getDataByKeyBoolean(Constant.IS_SOUND, false)){
                    baseContext.startService(ttsIntent)
                }

                //   textToSpeech?.speak(mContent, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }
}