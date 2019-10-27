package android.pvt.softtecotest.ui

import android.os.Bundle
import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.User
import android.pvt.softtecotest.mvvm.MVVMState
import android.pvt.softtecotest.mvvm.ViewModelUser
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_details_user.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class UserDetailsActivity : FragmentActivity(), OnMapReadyCallback {
    lateinit var map: GoogleMap
    var lat: Double = 0.0
    var lng: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_user)
        val id = intent.getIntExtra("ID", 0)
        val viewModel = ViewModelProviders.of(this).get(ViewModelUser::class.java)
        viewModel.load(id)
        viewModel.state.observe(this, Observer {
            when (it) {
                is MVVMState.DataUser -> {
                    Log.e("QQQ1", it.toString())
                    getUserInfo(it.user)
                    lat = it.user.address.geo.latitude.toDouble()
                    lng = it.user.address.geo.longitude.toDouble()
                    Log.e("LAT", it.user.address.geo.latitude + it.user.address.geo.longitude)
                }
                is MVVMState.Error -> {
                    Log.e("QQQEEE", "ERROR")
                }
            }
        })
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.view?.visibility = View.INVISIBLE

        userEmail.setOnClickListener{

        }

        userCity.setOnClickListener {
            getCityLocation()
            mapFragment.view?.visibility = View.VISIBLE
        }

    }


    private fun getUserInfo(user: User) {
        val postId = findViewById<TextView>(R.id.userPostId)
        val userId = findViewById<TextView>(R.id.userId)
        val userName = findViewById<TextView>(R.id.userName)
        val userNick = findViewById<TextView>(R.id.userNick)
        val userEmail = findViewById<TextView>(R.id.userEmail)
        val userWebsite = findViewById<TextView>(R.id.userWebsite)
        val userPhone = findViewById<TextView>(R.id.userPhone)
        val userCity = findViewById<TextView>(R.id.userCity)

        postId.text = intent.getIntExtra("postID", 0).toString()
        userId.text = user.id.toString()
        userName.text = user.name
        userNick.text = user.username
        userEmail.text = user.email
        userWebsite.text = user.website
        userPhone.text = user.phone
        userCity.text = user.address.city
    }
    override fun onMapReady(googleMap: GoogleMap) {
            map = googleMap
    }
    fun getCityLocation(){
        val city = LatLng(lat, lng)
        map.addMarker(MarkerOptions().position(city))
        val camera = CameraPosition.builder().target(city).zoom(5f).build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(camera))
    }
}