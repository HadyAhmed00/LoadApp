package com.hadyahmed00.loadapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hadyahmed00.loadapp.databinding.ActivityDegailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDegailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDegailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val status = intent.getStringExtra("status")
        val item = intent.getStringExtra("item")
        binding.itemDetail.text = item
        binding.statusDetail.text = status


        binding.okBtn.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        binding.statusImage.setImageResource(
            when (status) {
                "SUCCESSFUL" -> R.drawable.ic_success
                "FAILED" -> R.drawable.ic_failed
                else -> {
                    R.drawable.ic_launcher_foreground
                }
            }
        )

        binding.statusDetail.setTextColor(
            when (status) {
                "SUCCESSFUL" -> R.color.green
                "FAILED" -> R.color.red
                else -> {
                    R.color.white
                }
            }
        )


        Toast.makeText(this, "the staus is and itme is ${status}  ${item}", Toast.LENGTH_SHORT)
            .show()
    }
}