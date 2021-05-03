package com.example.compmovel.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Base64
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cmplacesapp.retrofit.ServiceBuilder
import com.example.compmovel.LoginActivity
import com.example.compmovel.MarkerDetailsActivity
import com.example.compmovel.R
import com.example.compmovel.models.ApiService
import com.example.compmovel.models.IncidentResponse
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var lastLocation: Location
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        sharedPreferences = requireActivity().getSharedPreferences("AUTH_PREFERENCES", Context.MODE_PRIVATE)

        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val mapView = root.findViewById<MapView>(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        mapView.getMapAsync(this)

        return root
    }



    override fun onMapReady(map: GoogleMap?) {
        map?.apply {

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            map.isMyLocationEnabled = true;

            val request = ServiceBuilder.buildService(ApiService::class.java)
            val call = request.getIncidents()
            call.enqueue(object : Callback<List<IncidentResponse>> {
                override fun onResponse(call: Call<List<IncidentResponse>>, response: Response<List<IncidentResponse>>) {
                    if (response.code() == 200) {
                        response.body()?.forEach { incident ->
                            if (incident.latitude != null && incident.longitude != null && incident.id != null) {

                                val icon: BitmapDescriptor

                                if (sharedPreferences.getString("user_id", null)?.toInt() == incident.user_id) {
                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.red)
                                } else {
                                    icon = BitmapDescriptorFactory.fromResource(R.drawable.blue)
                                }

                                createMarker(
                                        incident.id,
                                        incident.latitude.toDouble(),
                                        incident.longitude.toDouble(),
                                        incident.title,
                                        incident.description,
                                        incident.image,
                                        map,
                                        icon,
                                        incident
                                )
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "Ocorreu um erro!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<List<IncidentResponse>>, t: Throwable) {
                    println(t.message)
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_LONG).show()
                }

            })

            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.69323, -8.83287), 12f))

            map.setOnMarkerClickListener { marker ->

                val markerDetailsIntent = Intent(context, MarkerDetailsActivity::class.java)
                val incidente = marker.tag  as IncidentResponse
                markerDetailsIntent.putExtra("title", marker.title)
                markerDetailsIntent.putExtra("description", marker.snippet)
                markerDetailsIntent.putExtra("id", incidente.user_id)
                markerDetailsIntent.putExtra("image", incidente.image);
                markerDetailsIntent.putExtra("incident_id", incidente.id);


                startActivity(markerDetailsIntent)

                true
            }

        }
    }

    private fun createMarker(
            id: Int,
            latitude: Double,
            longitude: Double,
            title: String?,
            snippet: String?,
            image: String?,
            map: GoogleMap?,
            icon: BitmapDescriptor,
            incidentResponse: IncidentResponse
    ): Marker? {
        val imageBytes = Base64.decode(image, 0)
        val img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        val marker = map?.addMarker(
                MarkerOptions()
                        .position(LatLng(latitude, longitude))
                        .title(title)
                        .snippet(snippet)
                        .icon(icon)
        )

        marker?.tag = incidentResponse

        return marker
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logoutmenu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {

                val preferences: SharedPreferences = this.requireActivity().getSharedPreferences("AUTH_PREFERENCES", Context.MODE_PRIVATE)
                preferences.edit().remove("user_id").apply()
                preferences.edit().remove("token").apply()

                val intent = Intent(requireContext(), LoginActivity::class.java)
                requireContext().startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
