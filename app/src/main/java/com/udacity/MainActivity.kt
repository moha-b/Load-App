package com.udacity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var downloader: DownloadManager
    private var glide:Long =0
    private var load:Long =0
    private var retroift:Long =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        downloader = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        Channel(getString(R.string.channel),getString(R.string.notification_title))
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            if(group.checkedRadioButtonId != -1) {
                custom_button.state(ButtonState.Clicked)
            }
            when(group.checkedRadioButtonId){
                R.id.glide-> download(Glide)
                R.id.c3-> download(C3)
                R.id.retrofit-> download(Retrofit)
                else -> Toast.makeText(this,"Select item", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            var mouse = downloader.query(DownloadManager.Query().setFilterById(id!!))
            if (mouse.moveToNext()){
                val  currentState = mouse.getInt(mouse.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val notificationManager = ContextCompat.getSystemService(context!!,NotificationManager::class.java)
                mouse.close()
                when(currentState){
                    DownloadManager.STATUS_SUCCESSFUL->{
                        notificationManager?.send(context,context.getString(R.string.notification_description),context.getString(
                            when (id) {
                                    glide -> R.string.glide
                                load -> R.string.C3
                                else -> R.string.retrofit
                            }
                        ),
                            "suc"
                        )
                    }
                    DownloadManager.STATUS_FAILED->{
                        notificationManager?.send(context,context.getString(R.string.notification_description),context.getString(
                            when (id) {
                                glide -> R.string.glide
                                load -> R.string.C3
                                else -> R.string.retrofit
                            }
                        ),
                            "fail"
                        )

                    }
                }
            }
        }
    }

    private fun download(url: String) {
        val request =
            DownloadManager.Request(Uri.parse(C3))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
        when(url){
            Glide-> glide = downloader.enqueue(request)
            C3-> load =  downloader.enqueue(request)
            Retrofit-> retroift = downloader.enqueue(request)
        }
    }

    private fun Channel(id: String,name: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notifyChannel = NotificationChannel(
                id,name,NotificationManager.IMPORTANCE_HIGH)
                .apply {
                    setShowBadge(false)
                }
            notifyChannel.let {
                it.enableLights(true)
                it.enableVibration(true)
                it.lightColor = Color.CYAN
                it.description = applicationContext.getString(R.string.notification_description)
            }
            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notifyChannel)

        }
    }
}
