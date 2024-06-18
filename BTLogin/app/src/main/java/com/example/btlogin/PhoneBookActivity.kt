package com.example.btlogin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.AdapterView
import android.widget.ArrayAdapter


class PhoneBookActivity : AppCompatActivity() {
    data class Contact(val name: String, val phoneNumber: String, val birth: String, val address: String)

    private val contactList = listOf(
        Contact("nam", "0312231231", "22/05/2002", "HN"),
        Contact("vu", "023123122", "22/05/2002", "HN"),
        Contact("van", "2131231231", "22/05/2002", "HN"),
        Contact("thanh", "123131213", "22/05/2002", "HN")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_book)

        val listViewContacts = findViewById<ListView>(R.id.listView)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, contactList.map { it.name })
        listViewContacts.adapter = adapter

        listViewContacts.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val temp = contactList[position]
            val intent = Intent(this@PhoneBookActivity, DetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString("name", temp.name)
            bundle.putString("phone number", temp.phoneNumber)
            bundle.putString("birth", temp.birth)
            bundle.putString("address", temp.address)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }
}