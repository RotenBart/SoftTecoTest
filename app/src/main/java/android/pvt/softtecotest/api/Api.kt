package android.pvt.softtecotest.api

import android.pvt.softtecotest.entity.Post
import io.reactivex.Single
import retrofit2.http.GET

interface Api {
    @GET("posts")
    fun getPosts(
    ): Single<List<Post>>
}