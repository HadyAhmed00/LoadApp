package com.hadyahmed00.loadapp

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract.Attendees.query
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.hadyahmed00.loadapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedItem = ""
    private var downloadID: Long = 0


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var customBottn = CustomView(this)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel)
        )

        /*
        * if(selectedURL != null) {
                custom_button.buttonState = ButtonState.Loading
                download()

            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    getString(R.string.please_select_file),
                    Toast.LENGTH_LONG)
                toast.show()
            }*/
        binding.customButton.setOnClickListener {

            when (binding.radioGroup.checkedRadioButtonId) {
                R.id.glide -> {
                    binding.customButton.buttonState = ButtonState.Loading
                    download(glideUrl)
                    selectedItem = "Glide"
                }
                R.id.load_app -> {
                    binding.customButton.buttonState = ButtonState.Loading
                    download(loadAppUrl)
                    selectedItem = "Load App"
                }
                R.id.retrofit -> {
                    binding.customButton.buttonState = ButtonState.Loading
                    download(retrofitUrl)
                    selectedItem = "Retrofit"
                }
                else -> {
                    Toast.makeText(this, "Nothing is Selected", Toast.LENGTH_SHORT).show()
                }

            }
        }

        binding.downloadButton.setOnClickListener {

        }

    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = DownloadManager.Query()
            query.setFilterById(id!!)
            val cursor = downloadManager.query(query)
            binding.customButton.buttonState = ButtonState.Completed
            var downloadStat = "0"
            if (cursor != null && cursor.moveToNext()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                cursor.close()
                if (status == DownloadManager.STATUS_SUCCESSFUL) downloadStat = "1"
            }
            val notificationManager = context?.let {
                ContextCompat.getSystemService(
                    it,
                    NotificationManager::class.java
                )
            } as NotificationManager

            notificationManager.sendNotification(
                context.getText(
                    when (selectedItem) {
                        "Retrofit" -> R.string.notification_description_Retrofit
                        "Load App" -> R.string.notification_description_Project
                        "Glide" -> R.string.notification_description_Glide
                        else -> {
                            R.string.notification_description_Project
                        }
                    }
                ).toString(),
                context,
                selectedItem,
                downloadStat
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun download(URL: String) {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val loadAppUrl =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val glideUrl =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val retrofitUrl =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
//            notificationChannel.description = getString(R.string.notification_description)
            val notificationManager = application.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}