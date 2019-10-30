package android.pvt.softtecotest.ui

import android.os.Bundle
import android.pvt.softtecotest.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var city: LatLng
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)
        val lat = this.arguments?.getDouble("lat")
        val lng = this.arguments?.getDouble("lng")
        if (lat != null && lng != null) {
            city = LatLng(lat, lng)
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return rootView
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mMap.addMarker(MarkerOptions().position(city))
        val camera = CameraPosition.builder().target(city).zoom(5f).build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(camera))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mMap.clear()
    }
}