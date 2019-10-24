package android.pvt.softtecotest.ui

import android.os.Bundle
import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.Post
import android.pvt.softtecotest.mvvm.MVVMState
import android.pvt.softtecotest.mvvm.ViewModelPosts
import android.pvt.softtecotest.recycler.PostGridAdapter
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : FragmentActivity(), PostGridAdapter.OnClickListener {

    var posts: MutableList<Post> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(ViewModelPosts::class.java)
        viewModel.state.observe(this, Observer {
            when (it) {
                is MVVMState.Data -> {
                    Log.e("QQQ1", it.postList.toString())
                    posts.addAll(it.postList)
                }
                is MVVMState.Error -> {
                }
            }
        })

        Log.e("QQQ", posts.toString())
        val recyclerView = findViewById<RecyclerView>(R.id.postRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false
        val adapter = PostGridAdapter(posts, this)
        recyclerView.adapter = adapter


    }
    override fun onItemClick(post: Post){
        Log.e("PostClick", "Вы нажали на пост ${post.title}")
    }

}