package com.example.compmovel.ui.dashboard

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.compmovel.R
import com.example.compmovel.models.ApiService
import com.example.compmovel.models.InsertincidentResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var incidentPicture: ImageView
    private lateinit var bitmap: Bitmap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        getLastKnownLocation()

        takePhoto()

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        incidentPicture = root.findViewById(R.id.imageView4)

        val sendIncidentButton = root.findViewById<Button>(R.id.sendIncidentButton)

        sendIncidentButton.setOnClickListener {
            saveIncident(inflater,container);
        }

        return root
    }

    private fun takePhoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val frag: Fragment = this
        frag.startActivityForResult(intent, 12345)
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            latitude = location.latitude
            longitude = location.longitude
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveIncident(inflater: LayoutInflater, container: ViewGroup?){
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)


        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
        val image:ByteArray = bos.toByteArray()

        val base64Encoded = java.util.Base64.getEncoder().encodeToString(image)



        //Params to send in POST REQUEST
        val title = root.findViewById<EditText>(R.id.incidentTitle).getText().toString()
        val description = root.findViewById<EditText>(R.id.incidentDescription).getText().toString()
        val incidentImgBase64 = base64Encoded
        val latitude = latitude
        val longitude = longitude

        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.117:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        val service = retrofit.create(ApiService::class.java)
        val call = service.insertIncident(
                title = title,
                description = description,
                image = incidentImgBase64,
                latitude = latitude.toString(),
                longitude = longitude.toString())

        call.enqueue(object : Callback<InsertincidentResponse>{
            override fun onResponse(call: Call<InsertincidentResponse>, response: Response<InsertincidentResponse>) {
                if(response.code() == 200){
                    Toast.makeText(requireContext(),"Incidente Inserido com Sucesso!",Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(),"Ocorreu um erro!",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<InsertincidentResponse>, t: Throwable) {
                println(t.message)
                Toast.makeText(requireContext(),t.message,Toast.LENGTH_LONG).show()
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 12345 && resultCode == RESULT_OK) {
            bitmap = data?.extras?.get("data") as Bitmap
            incidentPicture.setImageBitmap(bitmap)
        }
    }

}