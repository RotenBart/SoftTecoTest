package android.pvt.softtecotest.ui

import android.os.Bundle
import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.Post
import android.pvt.softtecotest.mvvm.MVVMState
import android.pvt.softtecotest.mvvm.ViewModelPosts
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class MainActivity : FragmentActivity() {

    var posts: List<Post> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(ViewModelPosts::class.java)
        viewModel.state.observe(this, Observer {
            when (it) {
                is MVVMState.Data -> {
                    posts = it.postList
                }
                is MVVMState.Error -> {
                }
            }
        })

        Log.e("QQQ", posts.toString())

    }
}