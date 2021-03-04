package com.example.compmovel.ui.dashboard

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.compmovel.R

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var incidentPicture: ImageView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        takePhoto()

        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
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

    private fun saveIncident(inflater: LayoutInflater, container: ViewGroup?){
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        var title = root.findViewById<EditText>(R.id.incidentTitle)
        var description = root.findViewById<EditText>(R.id.incidentDescription)

        println(title.text)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 12345 && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            incidentPicture.setImageBitmap(imageBitmap)
        }
    }

}