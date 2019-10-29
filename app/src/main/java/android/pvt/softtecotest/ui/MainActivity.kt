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
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.AbsListView
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

    var posts: MutableList<Post> = mutableListOf()
    lateinit var recyclerView: RecyclerView
    lateinit var positionRecyclerView: RecyclerView
    lateinit var gridLayout: GridLayoutManager
    var prevPos: Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(ViewModelPosts::class.java)
        viewModel.state.observe(this, Observer {
            when (it) {
                is MVVMState.Data -> {
                    Log.v("QQQ1", it.postList.toString())
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



        recyclerView = findViewById(R.id.postRecyclerView)
        recyclerView.setHasFixedSize(true)
        positionRecyclerView = findViewById(R.id.positionRecyclerView)
        positionRecyclerView.setHasFixedSize(true)
        gridLayout = GridLayoutManager(this, 6, GridLayoutManager.HORIZONTAL, false)
        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(position: Int): Int {
                return 3
            }
        }
        recyclerView.layoutManager = gridLayout
        positionRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.isNestedScrollingEnabled = false
        positionRecyclerView.isNestedScrollingEnabled = false


        saveLogcatButton.setOnClickListener {
            val filePath = File(getExternalFilesDir(null), "logcat.txt")
            val cmd = arrayOf("logcat", "-f", filePath.absolutePath.toString(), "SPIN:I", "MyApp:D", "*:S")
            if (filePath.exists()) {
                filePath.appendText("Test")
                Runtime.getRuntime().exec(cmd)
            } else {
                filePath.createNewFile()
                Runtime.getRuntime().exec(cmd)
            }
            Log.e("MainActivity", "TESTT")

            //val cmd = "logcat -f" +filePath.absolutePath+ "MainActivity:I MyApp:E *:S"
            Log.d("FILE", filePath.exists().toString())
            Log.d("FILE", filePath.readText())
            val pw = PrintWriter(filePath)
            pw.close()
        }
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position =  gridLayout.findFirstCompletelyVisibleItemPosition()
                if(position<6){
                    positionRecyclerView.scrollToPosition(0)
                    positionRecyclerView[prevPos].itemPosition.setImageResource(R.drawable.position_item_inactive)
                    positionRecyclerView[0].itemPosition.setImageResource(R.drawable.position_item)
                    prevPos = 0
                }else {
                    positionRecyclerView.scrollToPosition(position/6)
                    positionRecyclerView[prevPos].itemPosition.setImageResource(R.drawable.position_item_inactive)
                    positionRecyclerView[position/6].itemPosition.setImageResource(R.drawable.position_item)
                    prevPos = position/6
                }

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        } )


    }

    override fun onItemClick(post: Post) {
        val intent = Intent(this, UserDetailsActivity::class.java)
        intent.putExtra("ID", post.userId)
        Log.e("IDPOST", post.id.toString())
        intent.putExtra("postID", post.id)
        startActivity(intent)
        Log.e("PostClick", "Вы нажали на пост ${post.title}")
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

    fun hasExternalStoragePrivateFile(): Boolean {
        val file = File(getExternalFilesDir(null), "logcat.txt")
        return file.exists()
    }

    fun deleteExternalStoragePrivateFile() {
        val file = File(getExternalFilesDir(null), "logcat.txt")
        file.delete()
    }

    fun setItemWidth(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x / 3
    }
    fun loadImage(){
        val mainImage = findViewById<ImageView>(R.id.mainImage)
        mainImage.load(R.drawable.logcat) {
            crossfade(true)
            placeholder(R.drawable.ic_android_green_36dp)
        }
    }
    fun startAnimation(){
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
}