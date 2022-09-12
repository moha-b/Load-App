package com.udacity

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

fun NotificationManager.cansel(){
    cancelAll()
}

@SuppressLint("UnspecifiedImmutableFlag")
fun NotificationManager.send(context: Context, message: String, path: String, state: String){

    val intent = Intent(context,DetailActivity::class.java)
    intent.putExtra(File,path)
    intent.putExtra(State,state)
    val awaiting = PendingIntent.getActivity(
        context,ID,intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        context,context.getString(R.string.channel)
    ).setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentText(path).setContentTitle(context.getString(R.string.notification_title))
        .setContentIntent(awaiting).setAutoCancel(true)
        .addAction(R.drawable.ic_assistant_black_24dp,context.getString(R.string.notify_state),awaiting)
    notify(ID,builder.build())
}
