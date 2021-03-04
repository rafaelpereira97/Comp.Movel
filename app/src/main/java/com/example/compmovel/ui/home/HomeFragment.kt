package com.example.compmovel.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.compmovel.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class HomeFragment : Fragment(), OnMapReadyCallback {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var lastLocation: Location

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

            createMarker(
                41.691807,
                -8.834451,
                "Viana",
                "Cuidado! Buraco muito fundo na estrada !",
                map
            )
            createMarker(
                42.691807,
                -8.834451,
                "Viana",
                "Cuidado! Buraco muito fundo na estrada !",
                map
            )

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.691807, -8.834451), 12f))


            map.setOnMarkerClickListener { marker ->
                if (marker.isInfoWindowShown) {
                    marker.hideInfoWindow()
                } else {
                    marker.showInfoWindow()
                }
                true
            }

        }
    }

    private fun createMarker(
        latitude: Double,
        longitude: Double,
        title: String?,
        snippet: String?,
        map: GoogleMap?
    ): Marker? {
        val marker = map?.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title(title)
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.warning))
        )
        marker?.tag = "teste"
        return marker
    }

}
