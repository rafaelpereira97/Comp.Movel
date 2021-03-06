package com.example.compmovel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cmplacesapp.retrofit.ServiceBuilder
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

        if(sharedPreference.getString("token",null) != null){
            startApp()
        }


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

        val request = ServiceBuilder.buildService(ApiService::class.java)
        val call = request.login(username = username, password = password)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.code() == 200) {
                    if(response.body()?.token != null) {
                        val sharedPreference =
                            getSharedPreferences("AUTH_PREFERENCES", Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()
                        editor.putString("token", response.body()?.token)
                        editor.putString("user_id", response.body()?.user_id)
                        editor.apply()

                        startApp()
                        finish()
                    }else{
                        showToast("Os dados de login estão incorretos!")
                    }
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
