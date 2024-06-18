package com.example.btlogin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class DetailActivity : AppCompatActivity() {
    private lateinit var nameTextView : TextView
    private lateinit var phoneTextView : TextView
    private lateinit var birthTextView : TextView
    private lateinit var addressTextView : TextView
    private lateinit var dialButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        nameTextView = findViewById(R.id.name)
        phoneTextView = findViewById(R.id.phone)
        birthTextView = findViewById(R.id.birth)
        addressTextView = findViewById(R.id.address)

        val bundle = intent.extras
        val phoneNumber =  bundle?.getString("phone number")
        nameTextView.append(bundle?.getString("name"))
        phoneTextView.append(phoneNumber)
        birthTextView.append(bundle?.getString("birth"))
        addressTextView.append(bundle?.getString("address"))

        dialButton = findViewById(R.id.dial)
        dialButton.setOnClickListener {
            val dial = Intent(Intent.ACTION_DIAL)
            dial.setData(Uri.parse("tel:$phoneNumber"))
            startActivity(dial)
        }


    }
}