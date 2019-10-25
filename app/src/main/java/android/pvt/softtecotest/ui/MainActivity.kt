package android.pvt.softtecotest.ui

import android.os.Bundle
import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.Post
import android.pvt.softtecotest.mvvm.MVVMState
import android.pvt.softtecotest.mvvm.ViewModelPosts
import android.pvt.softtecotest.recycler.PostGridAdapter
import android.util.Log
import android.widget.GridView
import android.widget.HorizontalScrollView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : FragmentActivity(), PostGridAdapter.OnClickListener {

    var posts: MutableList<Post> = mutableListOf()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: PostGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(ViewModelPosts::class.java)
        viewModel.state.observe(this, Observer {
            when (it) {
                is MVVMState.Data -> {
                    Log.e("QQQ1", it.postList.toString())
                    //posts.addAll(it.postList)
                    recyclerView.adapter = PostGridAdapter(it.postList, this)
                }
                is MVVMState.Error -> {
                    Log.e("QQQEEE", "ERROR")
                }
            }
        })

        recyclerView = findViewById<RecyclerView>(R.id.postRecyclerView)
        recyclerView.setHasFixedSize(true)

        val gridLayout = GridLayoutManager(this, 6, GridLayoutManager.HORIZONTAL, false )
            gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                return 3
            }
        }

        recyclerView.layoutManager = gridLayout


        recyclerView.isNestedScrollingEnabled = false
        //recyclerView.adapter = adapter


    }
    override fun onItemClick(post: Post){
        Log.e("PostClick", "Вы нажали на пост ${post.title}")
    }

}