package com.hadyahmed00.loadapp

import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.hadyahmed00.loadapp.databinding.ActivityDegailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDegailBinding
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDegailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()


        val status = intent.getStringExtra("status")
        val item = intent.getStringExtra("item")
        binding.itemDetail.text = item
        binding.statusDetail.text = if(status == "1") "Successful" else "Failed"
        binding.okBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        binding.statusImage.setImageResource( if(status == "1" ) R.drawable.ic_success else R.drawable.ic_failed)
    }
}
