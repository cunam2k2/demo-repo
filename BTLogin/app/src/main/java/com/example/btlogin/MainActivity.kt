package com.example.btlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var userNameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameTextView: TextView
    private lateinit var sdtTextView: TextView
    private lateinit var submit : Button
    private val list = listOf(
        Account("nam","031231212","nam","nam123"),
        Account("vu","031231212","vu","nam123"),
        Account("van","031231212","van","nam123"),
        Account("thang","031231212","thang","nam123"),
        Account("toan","1243413123","toan","nam123")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userNameEditText = findViewById(R.id.etUsername)
        passwordEditText = findViewById(R.id.etPassword)
        nameTextView = findViewById(R.id.tvName)
        sdtTextView = findViewById(R.id.tvSDT)
        submit = findViewById(R.id.btnSubmit)

        submit.setOnClickListener {
            val username = userNameEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val account = findAccountByUsername(username)
                if (account != null) {
                    if (account.password == password) {
//                        nameTextView.text = "Ho va ten :${account.hoTen}"
//                        sdtTextView.text = "SDT: ${account.sdt}"
                        val intent = Intent(this, PhoneBookActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Sai mat khau", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Khong co tai khoan", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Xin hay dien ca ten dang nhap va mat khau", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun findAccountByUsername(username: String): Account? {
        return list.find { it.username == username }
    }
}