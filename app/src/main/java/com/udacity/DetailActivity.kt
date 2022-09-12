package com.udacity

import android.app.NotificationManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        val notify = ContextCompat.getSystemService(applicationContext,NotificationManager::class.java)
        val download = intent.extras?.getString(File)
        val status = intent.extras?.getString(State)
        Detail_FileName.text = download
        Detail_Status.text = status
        when(status){
            "suc" -> Detail_Status.setTextColor(getColor(R.color.colorAccent))
            "fail" -> Detail_Status.setTextColor(getColor(R.color.colorAccent))
        }
        btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        notify?.cansel()

    }

}
