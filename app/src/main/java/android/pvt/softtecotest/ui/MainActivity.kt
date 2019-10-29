package android.pvt.softtecotest.ui


import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.pvt.softtecotest.R
import android.pvt.softtecotest.entity.Post
import android.pvt.softtecotest.mvvm.MVVMState
import android.pvt.softtecotest.mvvm.ViewModelPosts
import android.pvt.softtecotest.recycler.PositionAdapter
import android.pvt.softtecotest.recycler.PostGridAdapter
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_position.view.*
import java.io.File
import java.io.PrintWriter


class MainActivity : FragmentActivity(), PostGridAdapter.OnClickListener, PositionAdapter.OnClickListener {

    private var posts: MutableList<Post> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var positionRecyclerView: RecyclerView
    private lateinit var gridLayout: GridLayoutManager
    private var prevPos: Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(ViewModelPosts::class.java)
        viewModel.state.observe(this, Observer {
            when (it) {
                is MVVMState.Data -> {
                    posts.addAll(it.postList)
                    recyclerView.adapter = PostGridAdapter(it.postList, setItemWidth(), this)
                    positionRecyclerView.adapter = PositionAdapter(it.postList, this)
                }
                is MVVMState.Error -> {
                    Log.e("QQQEEE", "ERROR")
                }
            }
        })
        loadImage()
        startAnimation()
        initPostRecycler()
        initPositionRecycler()
        saveLogcatButton.setOnClickListener {
            saveLogCat()
        }
    }

    override fun onItemClick(post: Post) {
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra("ID", post.userId)
        intent.putExtra("postID", post.id)
        startActivity(intent)
    }

    override fun onItemCLick(position: Int, v:View) {
        positionRecyclerView[prevPos].itemPosition.setImageResource(R.drawable.position_item_inactive)
        prevPos = position
        v.itemPosition.setImageResource(R.drawable.position_item)
        Log.e("POSTS", (posts.size/6).toString())
        val pos = position+1
        if (pos == 1) {
            recyclerView.scrollToPosition(pos)
            Log.e("POSGRIDFIRST", (gridLayout.findFirstVisibleItemPosition()+1).toString())
            Log.e("POSGRIDLAST", (gridLayout.findLastVisibleItemPosition()+1).toString())

        } else if(pos>posts.size/6){
            recyclerView.scrollToPosition(posts.size-2)
        } else {
            recyclerView.scrollToPosition(pos*6-2)
            Log.e("POSGRIDFIRST", (gridLayout.findFirstVisibleItemPosition()+1).toString())
            Log.e("POSGRIDLAST", (gridLayout.findLastVisibleItemPosition()+1).toString())
        }
        Toast.makeText(this, "Переход к item $pos", Toast.LENGTH_SHORT).show()
    }

    private fun setItemWidth(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x / 3
    }
    private fun loadImage(){
        val mainImage = findViewById<ImageView>(R.id.mainImage)
        mainImage.load(R.drawable.logcat) {
            crossfade(true)
            placeholder(R.drawable.ic_android_green_36dp)
        }
        mainImage.setOnClickListener {
            val toast = Toast.makeText(this,"Meow!",Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }
    private fun saveLogCat(){
        val filePath = File(getExternalFilesDir(null), "logcat.txt")
        val cmd = arrayOf("logcat", "-f", filePath.absolutePath.toString(), "SPIN:I", "MyApp:D", "*:S")
        if (filePath.exists()) {
            filePath.appendText("Test")
            Runtime.getRuntime().exec(cmd)
        } else {
            filePath.createNewFile()
            Runtime.getRuntime().exec(cmd)
        }
        Log.d("FILE", filePath.readText())
        val pw = PrintWriter(filePath)
        pw.close()
    }
    private fun startAnimation(){
        val anim = RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = 3
        anim.duration = 1000
        mainImage.startAnimation(anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            var i = 0
            override fun onAnimationEnd(p0: Animation?) {
                Toast.makeText(applicationContext, "You can now save LogCat!", Toast.LENGTH_SHORT).show()
                Log.e("SPIN", "You can now save LogCat!")
                saveLogcatButton.visibility = View.VISIBLE
            }

            override fun onAnimationRepeat(p0: Animation?) {
                i++
                Toast.makeText(applicationContext, "$i spin", Toast.LENGTH_SHORT).show()
                Log.e("SPIN", "$i spin")
            }

            override fun onAnimationStart(p0: Animation?) {
                Toast.makeText(applicationContext, "Let's spin a while", Toast.LENGTH_SHORT).show()
                Log.e("SPIN", "Let's spin a while")
            }
        })
    }
    private fun scrollPositionRecycler(){
        val position =  gridLayout.findFirstCompletelyVisibleItemPosition()
        if(position<6){
            positionRecyclerView.scrollToPosition(0)
            positionRecyclerView[prevPos].itemPosition.setImageResource(R.drawable.position_item_inactive)
            positionRecyclerView[0].itemPosition.setImageResource(R.drawable.position_item)
            prevPos = 0
        }else if(position>posts.size*6){
            positionRecyclerView.scrollToPosition(posts.size/6+1)
            positionRecyclerView[prevPos].itemPosition.setImageResource(R.drawable.position_item_inactive)
            positionRecyclerView[posts.size/6+1].itemPosition.setImageResource(R.drawable.position_item)
        } else {
            positionRecyclerView.scrollToPosition(position/6)
            positionRecyclerView[prevPos].itemPosition.setImageResource(R.drawable.position_item_inactive)
            positionRecyclerView[position/6].itemPosition.setImageResource(R.drawable.position_item)
            prevPos = position/6
        }
    }
    private fun initPositionRecycler(){
        positionRecyclerView = findViewById(R.id.positionRecyclerView)
        positionRecyclerView.setHasFixedSize(true)
        positionRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        positionRecyclerView.isNestedScrollingEnabled = false
    }
    private fun initPostRecycler(){
        recyclerView = findViewById(R.id.postRecyclerView)
        recyclerView.setHasFixedSize(true)
        gridLayout = GridLayoutManager(this, 6, GridLayoutManager.HORIZONTAL, false)
        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 3
            }
        }
        recyclerView.layoutManager = gridLayout
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                scrollPositionRecycler()
            }
        } )
    }
}