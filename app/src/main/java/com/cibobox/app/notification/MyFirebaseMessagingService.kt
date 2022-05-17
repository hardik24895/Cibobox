package com.eisuchi.eisuchi.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.media.AudioAttributes
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.webkit.WebView
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.cibobox.app.R
import com.cibobox.app.ui.activity.HomeActivity
import com.cibobox.app.ui.activity.OrderDetailActivity
import com.eisuchi.eisuchi.data.modal.NotificationModal2

import com.eisuchi.eisuchi.uitils.Constant
import com.eisuchi.eisuchi.uitils.Logger
import com.eisuchi.utils.SessionManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService(){
    private var tts: TextToSpeech?= null
    private lateinit var text: String

    private lateinit var session: SessionManager
   lateinit var webView :WebView
    lateinit var  mTextToSpeech :TextToSpeech
    private companion object {
        private const val TAG = "Notification"
    }

    override fun onCreate() {
        super.onCreate()
        session = SessionManager(context = applicationContext)

        val mReceiver : BroadcastReceiver = object :BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
              //  loadHtml()
                if (intent?.action =="OPEN_NEW_ACTIVITY")
                {
                    if (session.getDataByKeyBoolean("isActive",true)){
                    val i = Intent(applicationContext, HomeActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(i)
                }else {

                    val i = Intent(applicationContext, OrderDetailActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    i.putExtra(Constant.DATA, intent.getStringExtra(Constant.DATA))
                    startActivity(i)
                }
                }

            }
        }

       val mIntentFilter= IntentFilter("OPEN_NEW_ACTIVITY" )
        registerReceiver(mReceiver, mIntentFilter);


    }
    /*var runnable = Runnable {
       loadHtml()
    }
*/

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Logger.e(TAG, "From: " + remoteMessage.from)


        println("data:--- " + Gson().toJson(remoteMessage.data))
        println("notification:-- " + Gson().toJson(remoteMessage.notification))
        val cliclAction = remoteMessage.notification?.clickAction

        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]

            Logger.e(TAG, "Message Notification Data: " + remoteMessage.data)

            if (session.isLoggedIn) buildNotification(
                title,
                message,
                remoteMessage.data,
                cliclAction.toString()
            )
        } else if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val message = remoteMessage.notification?.body


            Logger.e(TAG, "Message Notification: " + remoteMessage.notification)
            if (session.isLoggedIn) buildNotification(title, message, null, cliclAction.toString())
        }
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Logger.e(TAG, "Refreshed token: $token")
    }

    private fun buildNotification(
        title: String?,
        message: String?,
        data: MutableMap<String, String>?,
        clickAction: String
    ) {


        //tts = TextToSpeech(applicationContext, this)
        text = message.toString()
        val intent = Intent(this, HomeActivity::class.java)
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        if (data != null) {
            val bundle = Bundle()
            //  val click_action = data["click_action"]


           val  orderId = data["order_id"]

           /* val order = data.get("order")


            val gson = Gson()
            val jsonElement = gson.toJsonTree(data)
            val modal: NotificationModal2 =
                gson.fromJson(jsonElement, NotificationModal2::class.java)*/




            try {
              /*  val obj = JSONObject(order)
                Log.d("My App", obj.toString())

                val orderId = obj.getString("order_id")
                bundle.putString(Constant.ORDER, modal.ops.toString())
                bundle.putString(Constant.ORDER_ID, orderId)*/


                    val  broadcast =  Intent();
                    broadcast.setAction("OPEN_NEW_ACTIVITY")
                    broadcast.putExtra(Constant.DATA, orderId)
                    applicationContext.sendBroadcast(broadcast)




               /* if (modal.ops == "0" || modal.ops == "1" || modal.ops == "2") {
                    val i = Intent(applicationContext, OrderListActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(i)

                } else {
                    val i = Intent(applicationContext, OrderDetailActivity::class.java)
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    i.putExtra(Constant.DATA, orderId)
                    startActivity(i)
                }*/

            } catch (t: Throwable) {
               // Log.e("My App", "Could not parse malformed JSON: \"" + order.toString() + "\"")
            }


            //   val ops = m.getInt("ops")
            //  val json2 = json.getJSONObject("order")
            //  val order_id = json2.getString("order_id")


            if (title != null) bundle.putString(Constant.TITLE, title)
            intent.putExtra(Constant.DATA, bundle)
        } else {
            val bundle = Bundle()

            intent.putExtra(Constant.DATA, bundle)
        }

        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addParentStack(HomeActivity::class.java)
        stackBuilder.addNextIntent(intent)
        var pendingIntent: PendingIntent? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        }else{
            pendingIntent= stackBuilder.getPendingIntent(
                System.currentTimeMillis().toInt(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_CANCEL_CURRENT
            )
        }
       // val sound =
           // Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + packageName + "/" + R.raw.waterdrop_drop) //Here is FILE_NAME is the name of file that you want to play
       // val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
       // val soundUri =
         //   Uri.parse("android.resource://" + packageName + "/" + R.raw.waterdrop_drop)
        val channelId = "DefualtCibo"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
           // .setSound(soundUri)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()

            val channel = NotificationChannel(
                channelId,
                "Default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
           // channel.setSound(soundUri, attributes)
            manager.createNotificationChannel(channel)
        }
        //val mp: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.waterdrop_drop)
       // mp.start()

        manager.notify(System.currentTimeMillis().toInt(), builder.build())

       // sayText(applicationContext, message)
    }

   /* override fun onInit(status: Int) {
        Log.d(TAG, "onInit: started")
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("hi", "IN"))
            if (result != TextToSpeech.LANG_MISSING_DATA
                && result != TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                addAudioAttributes()
            }
        } else {
            Log.d(TAG, "TTS initialization failed ")
            Toast.makeText(
                applicationContext,
                "Your device don't support text to speech.\n Visit app to download!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun addAudioAttributes() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            tts?.setAudioAttributes(audioAttributes)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val focusRequest =
                AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                    )
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener { focus ->
                        when (focus) {
                            AudioManager.AUDIOFOCUS_GAIN -> {
                            }
                            else -> stopSelf()
                        }
                    }.build()

            when (audioManager.requestAudioFocus(focusRequest)) {
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> speak(audioManager, focusRequest)
                AudioManager.AUDIOFOCUS_REQUEST_DELAYED -> stopSelf()
                AudioManager.AUDIOFOCUS_REQUEST_FAILED -> stopSelf()
            }

        } else {
            val result = audioManager.requestAudioFocus( { focusChange: Int ->
                when(focusChange) {
                    AudioManager.AUDIOFOCUS_GAIN -> {
                    }
                    else -> stopSelf()
                }
            },
                AudioManager.STREAM_NOTIFICATION,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
            )

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                speak(audioManager, null)
            }
        }
    }

    private fun speak(audioManager: AudioManager, focusRequest: AudioFocusRequest?) {
        if (tts != null) {
            Log.d(TAG, "speak: Speaking start.....")
            val speechListener = object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    Log.d(TAG, "onStart: Started syntheses.....")
                }

                override fun onDone(utteranceId: String?) {
                    Log.d(TAG, "onDone: Completed synthesis ")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && focusRequest != null) {
                        audioManager.abandonAudioFocusRequest(focusRequest)
                    }
                    stopSelf()
                }

                override fun onError(utteranceId: String?) {
                    Log.d(TAG, "onError: Error synthesis")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && focusRequest != null) {
                        audioManager.abandonAudioFocusRequest(focusRequest)
                    }
                    stopSelf()
                }
            }
            val paramsMap: HashMap<String, String> = HashMap()
            paramsMap[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "tts_firebase_service"

            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null)
            tts?.setOnUtteranceProgressListener(speechListener)
        }
    }*/

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: Service is destroyed successfully")

        super.onDestroy()
    }


}