package com.example.compmovel.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.compmovel.R
import com.google.android.gms.location.FusedLocationProviderClient
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

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }

            map.isMyLocationEnabled = true;

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

            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.69323, -8.83287), 12f))

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
