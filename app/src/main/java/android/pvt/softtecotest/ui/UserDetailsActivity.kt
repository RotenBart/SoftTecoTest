package android.pvt.softtecotest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.User
import android.pvt.softtecotest.mvvm.MVVMState
import android.pvt.softtecotest.mvvm.ViewModelUser
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_details_user.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where


class UserDetailsActivity : FragmentActivity(), OnMapReadyCallback {
    private lateinit var map: GoogleMap
    private lateinit var realm: Realm
    private lateinit var user: User
    var lat: Double = 0.0
    var lng: Double = 0.0
    var email: String = ""
    var phoneNumber: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_user)
        val id = intent.getIntExtra("ID", 0)
        val viewModel = ViewModelProviders.of(this).get(ViewModelUser::class.java)
        viewModel.load(id)
        viewModel.state.observe(this, Observer {
            when (it) {
                is MVVMState.DataUser -> {
                    user = it.user
                    Log.e("QQQ1", it.toString())
                    getUserInfo(it.user)
                    if (it.user.address != null) {
                        lat = it.user.address!!.geo!!.latitude.toDouble()
                        lng = it.user.address!!.geo!!.longitude.toDouble()
                    }
                    email = it.user.email
                    phoneNumber = it.user.phone
                }
                is MVVMState.Error -> {
                    Log.e("QQQEEE", "ERROR")
                }
            }
        })
        realm = Realm.getDefaultInstance()
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment.view?.visibility = View.INVISIBLE
        findFirstUser()

        userPhone.setOnClickListener {
            callPhone()
        }
        userEmail.setOnClickListener {
            sendEmail()
        }

        userCity.setOnClickListener {
            getCityLocation()
            mapFragment.view?.visibility = View.VISIBLE
        }

        userDataSave.setOnClickListener {
            saveUser(user)
        }

    }


    private fun getUserInfo(user: User) {
        userPostId.text = intent.getIntExtra("postID", 0).toString()
        userId.text = user.id.toString()
        userName.text = user.name
        userNick.text = user.username
        userEmail.text = user.email
        userWebsite.text = user.website
        userPhone.text = user.phone
        userCity.text = user.address?.city
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }

    fun getCityLocation() {
        val city = LatLng(lat, lng)
        map.addMarker(MarkerOptions().position(city))
        val camera = CameraPosition.builder().target(city).zoom(5f).build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(camera))
    }

    fun callPhone() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto: $email")
        startActivity(intent)
    }

    fun saveUser(user: User) {
        realm.executeTransaction { realm ->
            val newUser = realm.createObject<User>(user.id)
            newUser.name = user.name
            newUser.username = user.username
            newUser.email = user.email
            newUser.phone = user.phone
            newUser.address = realm.copyToRealm(user.address)
            newUser.website = user.website
        }
        Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show()
    }
    fun findFirstUser(){
        val person = realm.where<User>().findFirst()
        Log.e("USER", person?.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}