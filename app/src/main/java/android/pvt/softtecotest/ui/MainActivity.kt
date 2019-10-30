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
import coil.transform.CircleCropTransformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_position.view.*
import java.io.File


class MainActivity : FragmentActivity(), PostGridAdapter.OnClickListener, PositionAdapter.OnClickListener {

    private val ITEMS_NUM = 6
    private var posts: MutableList<Post> = mutableListOf()
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var positionRecyclerView: RecyclerView
    private lateinit var gridLayout: GridLayoutManager
    private var prevPos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val positionAdapter = PositionAdapter(this)
        val postAdapter = PostGridAdapter(setItemWidth(), this)

        val viewModel = ViewModelProviders.of(this).get(ViewModelPosts::class.java)
        viewModel.state.observe(this, Observer {
            when (it) {
                is MVVMState.Data -> {
                    posts.addAll(it.postList)
                    positionAdapter.setPostList(it.postList)
                    postAdapter.setPostList(it.postList)
                }
                is MVVMState.Error -> {
                    Log.e("QQQEEE", "ERROR")
                }
            }
        })
        loadImage()
        startAnimation()
        initPostRecycler(postAdapter)
        initPositionRecycler(positionAdapter)
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

    override fun onItemCLick(position: Int, v: View) {
        positionRecyclerView[prevPos].itemPosition.setImageResource(R.drawable.position_item_inactive)
        prevPos = position
        v.itemPosition.setImageResource(R.drawable.position_item)
        Log.e("POSTS", (posts.size / ITEMS_NUM).toString())
        val pos = position + 1
        when {
            pos == 1 -> postRecyclerView.scrollToPosition(pos)
            pos > posts.size / ITEMS_NUM -> postRecyclerView.scrollToPosition(posts.size - 2)
            else -> postRecyclerView.scrollToPosition(pos * ITEMS_NUM - 2)
        }
    }

    private fun setItemWidth(): Int {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x / 3
    }

    private fun loadImage() {
        val mainImage = findViewById<ImageView>(R.id.mainImage)
        mainImage.load(R.drawable.logcat1) {
            crossfade(true)
            transformations(CircleCropTransformation())
            placeholder(R.drawable.ic_android_green_36dp)
        }
        mainImage.setOnClickListener {
            val toast = Toast.makeText(this, "Meow!", Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    private fun saveLogCat() {
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
        Toast.makeText(this, "LogCat saved", Toast.LENGTH_SHORT).show()
//        val pw = PrintWriter(filePath)
//        pw.close()
    }

    private fun startAnimation() {
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

    private fun scrollPositionRecycler() {
        val position = gridLayout.findFirstCompletelyVisibleItemPosition()
        when {
            position < ITEMS_NUM -> setPositionIndicator(0)
            position > posts.size * ITEMS_NUM -> setPositionIndicator(posts.size / ITEMS_NUM + 1)
            else -> setPositionIndicator(position / ITEMS_NUM)
        }
    }

    private fun setPositionIndicator(position: Int) {
        positionRecyclerView.scrollToPosition(position)
        positionRecyclerView[prevPos].itemPosition.setImageResource(R.drawable.position_item_inactive)
        positionRecyclerView[position].itemPosition.setImageResource(R.drawable.position_item)
        prevPos = position
    }

    private fun initPositionRecycler(adapter: PositionAdapter) {
        positionRecyclerView = findViewById(R.id.positionRecyclerView)
        positionRecyclerView.adapter = adapter
        positionRecyclerView.setHasFixedSize(true)
        positionRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        positionRecyclerView.isNestedScrollingEnabled = false
    }

    private fun initPostRecycler(adapter: PostGridAdapter) {
        postRecyclerView = findViewById(R.id.postRecyclerView)
        postRecyclerView.adapter = adapter
        postRecyclerView.setHasFixedSize(true)
        gridLayout = GridLayoutManager(this, ITEMS_NUM, GridLayoutManager.HORIZONTAL, false)
        gridLayout.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return 3
            }
        }
        postRecyclerView.layoutManager = gridLayout
        postRecyclerView.isNestedScrollingEnabled = false
        postRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                scrollPositionRecycler()
            }
        })
    }
}