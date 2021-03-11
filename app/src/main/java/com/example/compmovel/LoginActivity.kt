package com.example.compmovel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.compmovel.models.ApiService
import com.example.compmovel.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreference = getSharedPreferences("AUTH_PREFERENCES",Context.MODE_PRIVATE)
        println(sharedPreference.getString("token","defaultName"))


        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.toolbar))

        val btnLogin = findViewById<Button>(R.id.loginButton)
        val username = findViewById<EditText>(R.id.editTextTextPersonName)
        val password = findViewById<EditText>(R.id.editTextTextPassword)

        btnLogin.setOnClickListener {
            login(username.text.toString(),password.text.toString())
        }

    }

    fun login(username: String, password: String){
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.0.117:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        val service = retrofit.create(ApiService::class.java)
        val call = service.login(username = username, password = password)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.code() == 200) {
                    val sharedPreference = getSharedPreferences("AUTH_PREFERENCES",Context.MODE_PRIVATE)
                    val editor = sharedPreference.edit()
                    editor.putString("token",response.body()?.token)
                    editor.apply()

                    startApp()
                    finish()
                }

                if (response.code() == 401) {
                    showToast("Os dados de login estão incorretos!")
                }

            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    println(t.message)
            }
        })
    }

    fun startApp(){
        var loginIntent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(loginIntent)
    }

    fun showToast(message : String){
        Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
    }

}
