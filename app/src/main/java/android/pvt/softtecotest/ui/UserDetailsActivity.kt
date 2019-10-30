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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_details_user.*

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var viewModel: ViewModelUser
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_user)
        val id = intent.getIntExtra("ID", 0)
        viewModel = ViewModelProviders.of(this).get(ViewModelUser::class.java)
        viewModel.load(id)
        viewModel.state.observe(this, Observer {
            when (it) {
                is MVVMState.DataUser -> {
                    setUserInfo(it.user)
                    user = it.user
                    setToolbar(it.user.id)
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
            var lat: Double? = 0.0
            var lng: Double? = 0.0
            if (user.address?.geo?.latitude != null && user.address?.geo?.longitude != null) {
                lat = user.address?.geo?.latitude?.toDouble()
                lng = user.address?.geo?.longitude?.toDouble()
            }
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

    private fun setToolbar(id: Int) {
        val str = "contact #"
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolBar.title = str.plus(id.toString())
        toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setUserInfo(user: User) {
        userPostId.text = intent.getIntExtra("postID", 0).toString()
        userName.text = user.name
        userNick.text = user.username
        userEmail.text = user.email
        userWebsite.text = user.website
        userPhone.text = user.phone
        userCity.text = user.address?.city
    }

    private fun callPhone() {
        val intent = Intent(Intent.ACTION_DIAL)
        val phoneNumber = user.phone
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }

    private fun sendEmail() {
        val email = user.email
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto: $email")
        startActivity(intent)
    }

    private fun openWebsite() {
        val intent = Intent(Intent.ACTION_VIEW)
        val website = user.website
        val url = "http://"
        intent.data = Uri.parse(url.plus(website))
        startActivity(intent)
    }
}