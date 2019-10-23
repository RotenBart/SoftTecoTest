package android.pvt.softtecotest.repository

import android.pvt.softtecotest.entity.Post
import io.reactivex.Single

interface PostRepository {
    fun getPosts(): Single<List<Post>>
}