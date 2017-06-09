package com.fachri.mapsdirection

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mapbox.services.commons.models.Position


class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val listPlace: List<LatLng> = listOf(LatLng(-6.901745, 107.616378), LatLng(-6.965818, 107.680579),
                LatLng(-6.995806, 107.529517), LatLng(-6.896974, 107.533637))

        mMap.moveCamera(CameraUpdateFactory.newLatLng(listPlace.first()))

        listPlace.forEach {
            mMap.addMarker(MarkerOptions().position(it))
        }


        val listPosition = listPlace.map { Position.fromCoordinates(it.longitude, it.latitude) }.toList()

        MapUtil.getRoute(listPosition) {
            mMap.addPolyline(it)
            Log.d("lineOptions", " size : lineOptions")
        }
    }
}
