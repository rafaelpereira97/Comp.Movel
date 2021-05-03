package com.example.compmovel

import android.content.Context
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cmplacesapp.retrofit.ServiceBuilder
import com.example.compmovel.models.ApiService
import com.example.compmovel.models.DeleteincidentResponse
import com.example.compmovel.models.IncidentResponse
import com.example.compmovel.models.UpdateincidentResponse
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.android.synthetic.main.activity_marker_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MarkerDetailsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marker_details)

        val markerTitle = intent.extras?.get("title")
        val markerDescription = intent.extras?.get("description")
        val id = intent.extras?.get("id")
        val image = intent.extras?.get("image")

        val incidentTitle = findViewById<EditText>(R.id.incidentTitle)
        incidentTitle.setText(markerTitle.toString())

        val incidentDescription = findViewById<EditText>(R.id.incidentDescription)
        incidentDescription.setText(markerDescription.toString())

        val decodedString: ByteArray = Base64.decode(image.toString(), Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        val imagemIncidente = findViewById<ImageView>(R.id.imagemIncidente)
        imagemIncidente.setImageBitmap(decodedByte)

        showButtons(id.toString())

        deleteIncidentButton.setOnClickListener {
            val request = ServiceBuilder.buildService(ApiService::class.java)
            val call = request.deleteIncident(intent.extras?.get("incident_id") as Int)
            call.enqueue(object : Callback<DeleteincidentResponse> {
                override fun onResponse(call: Call<DeleteincidentResponse>, response: Response<DeleteincidentResponse>) {
                    if (response.code() == 200) {
                        Toast.makeText(this@MarkerDetailsActivity, "Editado com Sucesso!", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<DeleteincidentResponse>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(this@MarkerDetailsActivity, t.message, Toast.LENGTH_LONG).show()
                }

            })
        }

        editButton.setOnClickListener {
            val request = ServiceBuilder.buildService(ApiService::class.java)
            val call = request.updateIncident(intent.extras?.get("incident_id") as Int, incidentTitle.text.toString(), incidentDescription.text.toString())
            call.enqueue(object : Callback<UpdateincidentResponse> {
                override fun onResponse(call: Call<UpdateincidentResponse>, response: Response<UpdateincidentResponse>) {
                    if (response.code() == 200) {
                        Toast.makeText(this@MarkerDetailsActivity, "Editado com Sucesso!", Toast.LENGTH_LONG).show()

                    }
                }

                override fun onFailure(call: Call<UpdateincidentResponse>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(this@MarkerDetailsActivity, t.message, Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    fun showButtons(id:String){
        sharedPreferences = getSharedPreferences("AUTH_PREFERENCES", Context.MODE_PRIVATE)

        if(sharedPreferences.getString("user_id",null) != id){
            deleteIncidentButton.visibility = View.INVISIBLE
        }else{
            deleteIncidentButton.visibility = View.VISIBLE
        }

        if(sharedPreferences.getString("user_id",null) != id){
            editButton.visibility = View.INVISIBLE
        }else{
            editButton.visibility = View.VISIBLE
        }
    }

}