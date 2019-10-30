package android.pvt.softtecotest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.User
import android.pvt.softtecotest.mvvm.MVVMState
import android.pvt.softtecotest.mvvm.ViewModelUser
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_details_user.*

class UserDetailsActivity : FragmentActivity() {
    private lateinit var user: User
    private var lat: Double? = 0.0
    private var lng: Double? = 0.0
    private var email: String = ""
    private var website: String = ""
    private var phoneNumber: String = ""
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
                    setUserInfo(it.user)
                    if (it.user.address?.geo?.latitude != null && it.user.address?.geo?.longitude != null) {
                        lat = it.user.address?.geo?.latitude?.toDouble()
                        lng = it.user.address?.geo?.longitude?.toDouble()
                    }
                    email = it.user.email
                    phoneNumber = it.user.phone
                    website = it.user.website
                }
                is MVVMState.Error -> {
                    Log.e("QQQEEE", "ERROR")
                }
            }
        })

        userPhone.setOnClickListener {
            callPhone()
        }

        userEmail.setOnClickListener {
            sendEmail()
        }

        userWebsite.setOnClickListener {
            openWebsite()
        }

        userCity.setOnClickListener {
            val bundle = Bundle()
            lat?.let { it1 -> bundle.putDouble("lat", it1) }
            lng?.let { it1 -> bundle.putDouble("lng", it1) }
            val fragment = MapFragment()
            fragment.arguments = bundle
            supportFragmentManager.beginTransaction().add(R.id.map, fragment).commit()
        }

        userDataSave.setOnClickListener {
            viewModel.saveUser(user)
            Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUserInfo(user: User) {
        userPostId.text = intent.getIntExtra("postID", 0).toString()
        val str = "contact #"
        userId.text = str.plus(user.id.toString())
        userName.text = user.name
        userNick.text = user.username
        userEmail.text = user.email
        userWebsite.text = user.website
        userPhone.text = user.phone
        userCity.text = user.address?.city
    }

    private fun callPhone() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto: $email")
        startActivity(intent)
    }

    private fun openWebsite() {
        val intent = Intent(Intent.ACTION_VIEW)
        val url = "http://"
        intent.data = Uri.parse(url.plus(website))
        startActivity(intent)
    }
}